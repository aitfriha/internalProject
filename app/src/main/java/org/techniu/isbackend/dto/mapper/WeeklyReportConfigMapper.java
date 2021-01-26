package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.WeeklyReportConfigUpdateRequest;
import org.techniu.isbackend.dto.model.WeeklyReportConfigDto;
import org.techniu.isbackend.entity.WeeklyReportConfig;

@Mapper(componentModel = "spring")
public interface WeeklyReportConfigMapper {
    /**
     * Map dto to model
     *
     * @param configurationDto
     * @return Configuration
     */
    @Mapping(source = "id", target="_id")
    WeeklyReportConfig dtoToModel(WeeklyReportConfigDto configurationDto);

    /**
     * Map ConfigurationUpdateRequest to ConfigurationDto
     *
     * @param weeklyReportConfigUpdateRequest
     * @return ConfigurationDto
     */
    WeeklyReportConfigDto updateRequestToDto(WeeklyReportConfigUpdateRequest weeklyReportConfigUpdateRequest);

    /**
     * Map model to dto
     *
     * @param configuration
     * @return ConfigurationDto
     */
    @Mapping(source = "_id", target="id")
    WeeklyReportConfigDto modelToDto(WeeklyReportConfig configuration);
}
