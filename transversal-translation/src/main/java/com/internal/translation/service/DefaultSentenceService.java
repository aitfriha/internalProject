package com.internal.translation.service;


import com.internal.translation.dto.model.DefaultSentenceDto;

import java.util.List;

public interface DefaultSentenceService {

    /**
     * Save defaultSentence
     *
     * @param defaultSentenceDto defaultSentenceDto
     */
    void save(DefaultSentenceDto defaultSentenceDto);

    /**
     * Update defaultSentence
     *
     * @param defaultSentenceDto defaultSentenceDto
     */
    void updateDefaultSentence(DefaultSentenceDto defaultSentenceDto);

    /**
     * Save defaultSentences list
     *
     * @param defaultSentenceDto defaultSentenceDto
     */
    void addDefaultSentencesList(DefaultSentenceDto defaultSentenceDto);

    /**
     * Delete defaultSentence
     *
     * @param defaultSentenceId defaultSentenceId
     */
    void deleteDefaultSentence(String defaultSentenceId);

    /**
     * Get all defaultSentences
     *
     * @return List<DefaultSentenceDto>
     */
    List<DefaultSentenceDto> getAllDefaultSentence();

    /**
     * Get a defaultSentence
     *
     * @param defaultSentenceId defaultSentenceId
     * @return DefaultSentenceDto
     */
    DefaultSentenceDto getDefaultSentence(String defaultSentenceId);
}
