package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.dto.model.WeeklyReportDto;
import org.techniu.isbackend.entity.WeeklyReport;

@Mapper(componentModel = "spring")
public interface WeeklyReportMapper {
    /**
     * Map dto to model
     *
     * @param weeklyReportDto
     * @return WeeklyReport
     */
    @Mapping(target = "staff", ignore=true)
    WeeklyReport dtoToModel(WeeklyReportDto weeklyReportDto);

    /**
     * Map model to dto
     *
     * @param weeklyReport
     * @return WeeklyReportDto
     */
    WeeklyReportDto modelToDto(WeeklyReport weeklyReport);
}
