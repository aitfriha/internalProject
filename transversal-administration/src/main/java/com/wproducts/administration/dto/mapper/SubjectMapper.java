package com.wproducts.administration.dto.mapper;

import com.wproducts.administration.controller.request.SubjectAddRequest;
import com.wproducts.administration.controller.request.SubjectUpdateRequest;
import com.wproducts.administration.dto.model.SubjectDto;
import com.wproducts.administration.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    /**
     * Map dto to model
     *
     * @param subjectDto subjectDto
     * @return Subject
     */
    @Mapping(source = "subjectId", target="_id")
    @Mapping(target = "subjectCreatedAt", ignore=true)
    @Mapping(target = "subjectUpdatedAt", ignore=true)
    @Mapping(target = "subjectParent", ignore=true)
    Subject dtoToModel(SubjectDto subjectDto);
    /**
     * Map model to dto
     *
     * @param subject subject
     * @return SubjectDto
     */
    @Mapping(source = "_id", target="subjectId")
    @Mapping(target = "subjectCreatedAt", ignore=true)
    @Mapping(target = "subjectUpdatedAt", ignore=true)
    @Mapping(target = "subjectParent", ignore=true)
    SubjectDto modelToDto(Subject subject);

    /**
     * Map add request to dto
     *
     * @param subjectAddRequest subjectAddRequest
     * @return SubjectDto
     */
    @Mapping(target = "subjectParent", ignore=true)
    SubjectDto addRequestToDto(SubjectAddRequest subjectAddRequest);

    /**
     * Map update request to dto
     *
     * @param subjectUpdateRequest subjectUpdateRequest
     * @return SubjectDto
     */
    SubjectDto updateRequestToDto(SubjectUpdateRequest subjectUpdateRequest);
}
