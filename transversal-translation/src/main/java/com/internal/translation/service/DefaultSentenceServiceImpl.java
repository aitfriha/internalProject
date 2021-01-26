package com.internal.translation.service;

import com.internal.translation.dto.mapper.DefaultSentenceMapper;
import com.internal.translation.dto.model.DefaultSentenceDto;
import com.internal.translation.model.DefaultSentence;
import com.internal.translation.repository.DefaultSentenceRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class DefaultSentenceServiceImpl implements DefaultSentenceService {

    private final DefaultSentenceRepository defaultSentenceRepository;
    private final DefaultSentenceMapper defaultSentenceMapper = Mappers.getMapper(DefaultSentenceMapper.class);

    public DefaultSentenceServiceImpl(DefaultSentenceRepository defaultSentenceRepository) {
        this.defaultSentenceRepository = defaultSentenceRepository;
    }

    @Override
    public void save(DefaultSentenceDto defaultSentenceDto) {

        Optional<DefaultSentence> defaultSentence = Optional.ofNullable(defaultSentenceRepository.findByCode(defaultSentenceDto.getCode()));

        // If defaultsentence already exists we raise DUPLICATE_ENTITY exception
        if (defaultSentence.isPresent()) {
            throw exception(ExceptionType.DUPLICATE_ENTITY);
        }

        defaultSentenceRepository.save(
                defaultSentenceMapper.dtoToModel(defaultSentenceDto));
    }

    @Override
    public void updateDefaultSentence(DefaultSentenceDto defaultSentenceDto) {

        Optional<DefaultSentence> defaultSentence = defaultSentenceRepository.findById(
                defaultSentenceDto.getDefaultSentenceId());

        // If defaultsentence doesn't exist we raise ENTITY_NOT_FOUND exception
        if (!defaultSentence.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        // Update defaultSentence in database
        defaultSentenceRepository.save(
                defaultSentenceMapper.dtoToModel(defaultSentenceDto));
    }

    @Override
    public void addDefaultSentencesList(DefaultSentenceDto defaultSentenceDto) {

        for (Map.Entry<String, String> defaultSentenceItem : defaultSentenceDto.getDefaultSentencesList().entrySet()) {

            // Test if defaultsentence already exists
            Optional<DefaultSentence> defaultSentence = Optional.ofNullable(defaultSentenceRepository.findByCode(defaultSentenceItem.getKey()));

            // If exists we update the value field if it's changed
            if (defaultSentence.isPresent()) {

                if (!defaultSentence.get().getValue().equals(defaultSentenceItem.getValue())) {

                    defaultSentence.get().setValue(defaultSentenceItem.getValue());

                    defaultSentenceRepository.save(defaultSentence.get());
                }

                // If it doesn't exist we create a new defaultSentence
            } else {

                DefaultSentence newDefaultSentence = new DefaultSentence()
                        .setCode(defaultSentenceItem.getKey())
                        .setValue(defaultSentenceItem.getValue());

                defaultSentenceRepository.save(newDefaultSentence);
            }

        }
    }

    public void deleteDefaultSentence(String id) {

        Optional<DefaultSentence> defaultSentence = defaultSentenceRepository.findById(id);

        // If defaultsentence doesn't exists
        if (!defaultSentence.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        defaultSentenceRepository.deleteById(id);

    }

    public List<DefaultSentenceDto> getAllDefaultSentence() {

        // Get all defaultSentences
        List<DefaultSentence> defaultSentences = defaultSentenceRepository.findAll();

        // Create a list of all defaultSentences dto
        ArrayList<DefaultSentenceDto> defaultSentenceDtos = new ArrayList<>();

        for (DefaultSentence defaultSentence : defaultSentences) {

            defaultSentenceDtos.add(
                    defaultSentenceMapper.modelToDto(defaultSentence));
        }

        return defaultSentenceDtos;
    }

    public DefaultSentenceDto getDefaultSentence(String id) {

        Optional<DefaultSentence> defaultSentence = Optional.ofNullable(defaultSentenceRepository.findDefaultSentenceBy_id(id));

        // If defaultsentence doesn't exists
        if (!defaultSentence.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        return defaultSentenceMapper.modelToDto(defaultSentence.get());

    }

    /**
     * Returns a RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.DefaultSentence, exceptionType, args);
    }
}
