package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.dto.model.WeeklyWorkDto;
import org.techniu.isbackend.entity.WeeklyWork;

@Mapper(componentModel = "spring")
public interface WeeklyWorkMapper {
    /**
     * Map dto to model
     *
     * @param weeklyWorkDto
     * @return WeeklyWork
     */
    @Mapping(source = "id", target="_id")
    @Mapping(target = "staff", ignore=true)
    WeeklyWork dtoToModel(WeeklyWorkDto weeklyWorkDto);

    /**
     * Map model to dto
     *
     * @param weeklyWork
     * @return WeeklyWorkDto
     */
    @Mapping(source = "_id", target="id")
    WeeklyWorkDto modelToDto(WeeklyWork weeklyWork);
}
