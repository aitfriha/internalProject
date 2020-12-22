package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.EconomicStaffMonthAddrequest;
import org.techniu.isbackend.controller.request.EconomicStaffMonthUpdaterequest;
import org.techniu.isbackend.dto.model.EconomicStaffMonthDto;
import org.techniu.isbackend.entity.EconomicStaffMonth;

@Mapper(componentModel = "spring")
public interface EconomicStaffMonthMapper {
    /**
     * Map dto to model
     *
     * @param economicStaffMonthDto economicStaffMonthDto
     * @return EconomicStaffMonth
     */
    @Mapping(source = "economicStaffMonthId", target="_id")
    EconomicStaffMonth dtoToModel(EconomicStaffMonthDto economicStaffMonthDto);

    /**
     * Map EconomicStaffMonth to EconomicStaffMonthDo
     *
     * @param economicStaffMonthAddrequest economicStaffMonthAddrequest
     * @return EconomicStaffMonthDto
     */
    EconomicStaffMonthDto addRequestToDto(EconomicStaffMonthAddrequest economicStaffMonthAddrequest);

    /**
     * Map EconomicStaffMonth to EconomicStaffMonthDo
     *
     * @param economicStaffMonthUpdaterequest economicStaffMonthUpdaterequest
     * @return EconomicStaffMonthDto
     */
    EconomicStaffMonthDto updateRequestToDto(EconomicStaffMonthUpdaterequest economicStaffMonthUpdaterequest);

    /**
     * Map economicStaffMonth to economicStaffMonthDo
     *
     * @param economicStaffMonth economicStaffMonth
     * @return EconomicStaffMonthDto
     */
    @Mapping(source = "_id", target="economicStaffMonthId")
    EconomicStaffMonthDto modelToDto(EconomicStaffMonth economicStaffMonth);
}
