package com.wproducts.administration.service;

import com.wproducts.administration.controller.request.SubjectAddRequest;
import com.wproducts.administration.dto.model.SubjectDto;

import java.util.List;

public interface SubjectService {
    /**
     * Register a new Subject
     *
     * @param subjectAddRequest - subjectAddRequest
     */
    void save(SubjectAddRequest subjectAddRequest);

    /**
     * Update Subject
     *
     * @param subjectDto - subjectDto
     */
    void updateSubject(SubjectDto subjectDto);

    /**
     * delete Subject
     *
     * @param id - id
     */
    void removeSubject(String id);

    /**
     * all SubjectsDto
     *
     * @return List SubjectsDto
     */
    List<SubjectDto> getAllSubjects();

    /**
     * one SubjectDto
     *
     * @param id - id
     * @return SubjectDto
     */
    SubjectDto getOneSubject(String id);
}
