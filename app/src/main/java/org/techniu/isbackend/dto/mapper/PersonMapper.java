package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.dto.model.PersonDto;
import org.techniu.isbackend.entity.Person;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    /**
     * Map dto to model
     *
     * @param personDto
     * @return Person
     */
    @Mapping(target = "personType", ignore=true)
    Person dtoToModel(PersonDto personDto);

    /**
     * Map model to dto
     *
     * @param person
     * @return PersonDto
     */
    @Mapping(source = "personType._id", target="personTypeId")
    @Mapping(source = "personType.name", target="personTypeName")
    PersonDto modelToDto(Person person);
}
