package com.internal.translation.service;

import com.mongodb.client.DistinctIterable;


import com.internal.translation.dto.mapper.TranslateSentenceMapper;
import com.internal.translation.dto.model.TranslateSentenceDto;
import com.internal.translation.model.DefaultSentence;
import com.internal.translation.model.TranslateSentence;
import com.internal.translation.repository.DefaultSentenceRepository;
import com.internal.translation.repository.TranslateSentenceRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TranslateSentenceServiceImpl implements TranslateSentenceService {

    private final TranslateSentenceRepository translateSentenceRepository;
    private final DefaultSentenceRepository defaultSentenceRepository;
    private final TranslateSentenceMapper translateSentenceMapper = Mappers.getMapper(TranslateSentenceMapper.class);


    private final MongoTemplate mongoTemplate;

    public TranslateSentenceServiceImpl(DefaultSentenceRepository defaultSentenceRepository, TranslateSentenceRepository translateSentenceRepository, MongoTemplate mongoTemplate) {
        this.translateSentenceRepository = translateSentenceRepository;
        this.defaultSentenceRepository = defaultSentenceRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(TranslateSentenceDto translateSentenceDto) {

        DefaultSentence defaultSentence = defaultSentenceRepository.findByCode(translateSentenceDto.getDefaultSentenceCode());

        if (defaultSentence != null) {

            TranslateSentence translateSentence = new TranslateSentence()
                    .setCountryLanguageCode(translateSentenceDto.getCountryLanguageCode())
                    .setDefaultSentence(defaultSentence)
                    .setTranslation(translateSentenceDto.getTranslation());

            translateSentenceRepository.save(translateSentence);
        }

    }

    /**
     * Update TranslateSentenceDto
     *
     * @param translateSentenceDto - translateSentenceDto
     */
    @Override
    public void updateTranslateSentence(TranslateSentenceDto translateSentenceDto) {


        if (translateSentenceDto.getTranslateSentenceId() != null) {

            Optional<TranslateSentence> translateSentence = translateSentenceRepository
                    .findById(translateSentenceDto.getTranslateSentenceId());

            if (translateSentence.isPresent()) {
                TranslateSentence translateSentenceModel = translateSentence.get();
                translateSentenceModel.setTranslation(translateSentenceDto.getTranslation());

                // Update TranslateSentence translation
                translateSentenceRepository.save(translateSentenceModel);

            } else {
                throw exception(ExceptionType.ENTITY_NOT_FOUND);

            }

        } else {
            // Create a new translate sentence
            save(translateSentenceDto);
        }

    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.TranslateSentence, exceptionType, args);
    }

    /**
     * delete TranslateSentence
     *
     * @param id id
     */

    public void removeParam(String id) {
        Optional<TranslateSentence> translateSentence = translateSentenceRepository.findById(id);
        if (translateSentence.isPresent()) {
            translateSentenceRepository.deleteById(id);
        } else {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
    }

    /**
     * all TranslateSentence
     *
     * @return List TranslateSentenceDto
     */

    public List<TranslateSentenceDto> getAllTranslateSentence() {
        List<TranslateSentence> translateSentences = translateSentenceRepository.findAll();
        ArrayList<TranslateSentenceDto> list = new ArrayList<>();
        for (TranslateSentence a : translateSentences) {
            TranslateSentenceDto t = new TranslateSentenceDto()
                    .setTranslateSentenceId(a.get_id())
                    .setDefaultSentenceCode(a.getDefaultSentence().get_id())
                    .setCountryLanguageCode(a.getCountryLanguageCode())
                    .setTranslation(a.getTranslation())
                    .setDefaultSentenceValue(a.getDefaultSentence().getValue())
                    .setDefaultSentenceCode(a.getDefaultSentence().getCode());
            list.add(t);
        }
        return list;
    }

    /**
     * one TranslateSentence
     *
     * @return TranslateSentenceDto
     */
    public TranslateSentenceDto getOneTranslateSentence(String id) {
        Optional<TranslateSentence> translateSentence = Optional.ofNullable(translateSentenceRepository.findTranslateSentenceBy_id(id));
        if (translateSentence.isPresent()) {
            TranslateSentence translateSentenceModel = translateSentence.get();

            return new TranslateSentenceDto()
                    .setTranslateSentenceId(translateSentenceModel.get_id())
                    .setDefaultSentenceCode(translateSentenceModel.getDefaultSentence().get_id())
                    .setCountryLanguageCode(translateSentenceModel.getCountryLanguageCode())
                    .setTranslation(translateSentenceModel.getTranslation())
                    .setDefaultSentenceValue(translateSentenceModel.getDefaultSentence().getValue())
                    .setDefaultSentenceCode(translateSentenceModel.getDefaultSentence().getCode());
        }

        throw exception(ExceptionType.ENTITY_NOT_FOUND);
    }

    /**
     * addTranslateSentencesList
     *
     * @param translateSentenceDto translate sentence DTO
     */
    public void addTranslateSentencesList(TranslateSentenceDto translateSentenceDto) {

        for (Map.Entry<String, String> translateSentenceItem : translateSentenceDto.getTranslateSentencesList().entrySet()) {

            // when we have a translate sentence with the same countryLanguageCode & DefaultSentence we update the translation

            // Get defaultSentence
            Optional<DefaultSentence> defaultSentence = Optional.ofNullable(defaultSentenceRepository.findByCode(translateSentenceItem.getKey()));
            if (defaultSentence.isPresent()) {

                Optional<TranslateSentence> translateSentence = Optional.ofNullable(
                        translateSentenceRepository
                                .findTranslateSentenceByDefaultSentenceAndCountryLanguageCode(
                                        defaultSentence.get(), translateSentenceDto.getCountryLanguageCode()));
                if (translateSentence.isPresent()) {
                    // We update the translation:
                    translateSentence.get().setTranslation(translateSentenceItem.getValue());
                    translateSentenceRepository.save(translateSentence.get());
                } else {
                    // We add a new translation
                    TranslateSentence translateSentence1 = new TranslateSentence().setCountryLanguageCode(translateSentenceDto.getCountryLanguageCode())
                            .setDefaultSentence(defaultSentence.get()).setTranslation(translateSentenceItem.getValue());
                    translateSentenceRepository.save(translateSentence1);
                }

            }

        }
    }


    /**
     * TranslateSentence By CountryLanguage
     *
     * @return List TranslateSentenceDto
     */
    public List<TranslateSentenceDto> getAllTranslateSentenceByCountryLanguage(String countryLanguage) {

        ArrayList<TranslateSentenceDto> translateSentenceDtoArrayList = new ArrayList<>();
        List<TranslateSentence> translateSentences = translateSentenceRepository.findAllByCountryLanguageCode(countryLanguage);
        List<DefaultSentence> defaultSentenceList = defaultSentenceRepository.findAll();

        for (DefaultSentence defaultSentence : defaultSentenceList) {

            TranslateSentenceDto translateSentenceDto = new TranslateSentenceDto()
                    // Set default sentence code
                    .setDefaultSentenceCode(defaultSentence.getCode())
                    // Set default sentence value
                    .setDefaultSentenceValue(defaultSentence.getValue())
                    // Set country language code
                    .setCountryLanguageCode(countryLanguage);

            // test if the translate sentences list contains this default sentence and country language
            TranslateSentence translateSentence = new TranslateSentence().setDefaultSentence(defaultSentence).setCountryLanguageCode(countryLanguage);
            int index = translateSentences.indexOf(translateSentence);
            if (index != -1) {
                translateSentenceDto.setTranslateSentenceId(translateSentences.get(index).get_id());
                translateSentenceDto.setTranslation(translateSentences.get(index).getTranslation());
            }

            translateSentenceDtoArrayList.add(translateSentenceDto);
        }

        return translateSentenceDtoArrayList;
    }

    public List<String> getDistinctTranslateSentenceCountryLanguages() {

        DistinctIterable<String> distinctIterable = mongoTemplate.getCollection("translateSentence").distinct("countryLanguageCode", String.class);

        List<String> list = new ArrayList<>();

        for (String str : distinctIterable) {
            list.add(str);
        }

        return list;
    }

}
