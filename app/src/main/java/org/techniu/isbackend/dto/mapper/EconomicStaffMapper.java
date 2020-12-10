package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.EconomicStaffAddrequest;
import org.techniu.isbackend.controller.request.EconomicStaffUpdaterequest;
import org.techniu.isbackend.dto.model.EconomicStaffDto;
import org.techniu.isbackend.entity.EconomicStaff;

@Mapper(componentModel = "spring")
public interface EconomicStaffMapper {
    /**
     * Map dto to model
     *
     * @param economicStaffDto economicStaffDto
     * @return EconomicStaff
     */
    @Mapping(source = "economicStaffId", target="_id")
    EconomicStaff dtoToModel(EconomicStaffDto economicStaffDto);

    /**
     * Map EconomicStaff to EconomicStaffDo
     *
     * @param economicStaffAddrequest economicStaffAddrequest
     * @return EconomicStaffDto
     */
    EconomicStaffDto addRequestToDto(EconomicStaffAddrequest economicStaffAddrequest);

    /**
     * Map EconomicStaff to EconomicStaffDo
     *
     * @param economicStaffUpdaterequest economicStaffUpdaterequest
     * @return EconomicStaffDto
     */
    EconomicStaffDto updateRequestToDto(EconomicStaffUpdaterequest economicStaffUpdaterequest);

    /**
     * Map economicStaff to economicStaffDo
     *
     * @param economicStaff economicStaff
     * @return EconomicStaffDto
     */
    @Mapping(source = "_id", target="economicStaffId")
    EconomicStaffDto modelToDto(EconomicStaff economicStaff);
}
