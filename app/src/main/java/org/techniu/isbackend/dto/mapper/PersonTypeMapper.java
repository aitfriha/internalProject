package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.PersonTypeAddRequest;
import org.techniu.isbackend.controller.request.PersonTypeUpdateRequest;
import org.techniu.isbackend.dto.model.PersonTypeDto;
import org.techniu.isbackend.entity.PersonType;

@Mapper(componentModel = "spring")
public interface PersonTypeMapper {
    /**
     * Map dto to model
     *
     * @param personTypeDto
     * @return PersonType
     */
    @Mapping(source = "id", target="_id")
    PersonType dtoToModel(PersonTypeDto personTypeDto);

    /**
     * Map PersonTypeAddRequest to PersonTypeDto
     *
     * @param personTypeAddRequest
     * @return PersonTypeDto
     */
    PersonTypeDto addRequestToDto(PersonTypeAddRequest personTypeAddRequest);

    /**
     * Map PersonTypeUpdateRequest to PersonTypeDto
     *
     * @param personTypeUpdateRequest
     * @return PersonTypeDto
     */
    PersonTypeDto updateRequestToDto(PersonTypeUpdateRequest personTypeUpdateRequest);

    /**
     * Map model to dto
     *
     * @param personType
     * @return PersonTypeDto
     */
    @Mapping(source = "_id", target="id")
    PersonTypeDto modelToDto(PersonType personType);
}
