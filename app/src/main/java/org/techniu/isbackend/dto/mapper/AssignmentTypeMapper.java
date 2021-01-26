package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.AssignmentTypeAddRequest;
import org.techniu.isbackend.controller.request.AssignmentTypeUpdateRequest;
import org.techniu.isbackend.dto.model.AssignmentTypeDto;
import org.techniu.isbackend.entity.AssignmentType;

@Mapper(componentModel = "spring")
public interface AssignmentTypeMapper {
    /**
     * Map dto to model
     *
     * @param assignmentTypeDto
     * @return Currency
     */
    @Mapping(source = "id", target="_id")
    AssignmentType dtoToModel(AssignmentTypeDto assignmentTypeDto);

    /**
     * Map AssignmentTypeAddRequest to AssignmentTypeDto
     *
     * @param assignmentTypeAddRequest
     * @return AssignmentTypeDto
     */
    AssignmentTypeDto addRequestToDto(AssignmentTypeAddRequest assignmentTypeAddRequest);

    /**
     * Map AssignmentTypeUpdateRequest to AssignmentTypeDto
     *
     * @param assignmentTypeUpdateRequest
     * @return AssignmentTypeDto
     */
    AssignmentTypeDto updateRequestToDto(AssignmentTypeUpdateRequest assignmentTypeUpdateRequest);

    /**
     * Map AssignmentType to AssignmentTypeDto
     *
     * @param assignmentType
     * @return AssignmentTypeDto
     */
    @Mapping(source = "_id", target="id")
    AssignmentTypeDto modelToDto(AssignmentType assignmentType);
}
