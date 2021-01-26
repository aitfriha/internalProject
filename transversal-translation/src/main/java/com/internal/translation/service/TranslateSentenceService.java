package com.internal.translation.service;


import com.internal.translation.dto.model.TranslateSentenceDto;

import java.util.List;

public interface TranslateSentenceService {
    /**
     * Register a new TranslateSentence
     *
     * @param translateSentenceDto
     */
    void save(TranslateSentenceDto translateSentenceDto);

    /**
     * Update TranslateSentence
     *
     * @param translateSentenceDto
     */
    void updateTranslateSentence(TranslateSentenceDto translateSentenceDto);


    /**
     * delete TranslateSentence
     * @param id
     */
    void removeParam(String id);

    /**
     * all TranslateSentence
     *
     * @return
     */
    List<TranslateSentenceDto> getAllTranslateSentence();

    /**
     * one TranslateSentence
     * @param id
     * @return
     */
    TranslateSentenceDto getOneTranslateSentence(String id);

    /**
     * addTranslateSentencesList
     * @param translateSentenceDto
     * @return
     */
    void addTranslateSentencesList(TranslateSentenceDto translateSentenceDto);

    /**
     * list TranslateSentence by country language
     * @return
     */
    List<TranslateSentenceDto> getAllTranslateSentenceByCountryLanguage(String countryLanguage);

    /**
     * list TranslateSentence distinct country languages
     * @return
     */
    List<String> getDistinctTranslateSentenceCountryLanguages();
}
