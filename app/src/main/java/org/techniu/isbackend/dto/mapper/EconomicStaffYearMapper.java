package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.EconomicStaffYearAddrequest;
import org.techniu.isbackend.controller.request.EconomicStaffYearUpdaterequest;
import org.techniu.isbackend.dto.model.EconomicStaffYearDto;
import org.techniu.isbackend.entity.EconomicStaffYear;

@Mapper(componentModel = "spring")
public interface EconomicStaffYearMapper {
    /**
     * Map dto to model
     *
     * @param economicStaffYearDto economicStaffYearDto
     * @return EconomicStaffYear
     */
    @Mapping(source = "economicStaffYearId", target="_id")
    EconomicStaffYear dtoToModel(EconomicStaffYearDto economicStaffYearDto);

    /**
     * Map EconomicStaffYear to EconomicStaffYearDo
     *
     * @param economicStaffYearAddrequest economicStaffYearAddrequest
     * @return EconomicStaffYearDto
     */
    EconomicStaffYearDto addRequestToDto(EconomicStaffYearAddrequest economicStaffYearAddrequest);

    /**
     * Map EconomicStaffYear to EconomicStaffYearDo
     *
     * @param economicStaffYearUpdaterequest economicStaffYearUpdaterequest
     * @return EconomicStaffYearDto
     */
    EconomicStaffYearDto updateRequestToDto(EconomicStaffYearUpdaterequest economicStaffYearUpdaterequest);

    /**
     * Map economicStaffYear to economicStaffYearDo
     *
     * @param economicStaffYear economicStaffYear
     * @return EconomicStaffYearDto
     */
    @Mapping(source = "_id", target="economicStaffYearId")
    EconomicStaffYearDto modelToDto(EconomicStaffYear economicStaffYear);
}
