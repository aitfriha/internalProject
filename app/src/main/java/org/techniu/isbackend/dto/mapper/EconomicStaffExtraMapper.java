package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.EconomicStaffExtraAddrequest;
import org.techniu.isbackend.controller.request.EconomicStaffExtraUpdaterequest;
import org.techniu.isbackend.dto.model.EconomicStaffExtraDto;
import org.techniu.isbackend.entity.EconomicStaffExtra;

@Mapper(componentModel = "spring")
public interface EconomicStaffExtraMapper {
    /**
     * Map dto to model
     *
     * @param economicStaffExtraDto economicStaffExtraDto
     * @return EconomicStaffExtra
     */
    @Mapping(source = "economicStaffExtraId", target="_id")
    EconomicStaffExtra dtoToModel(EconomicStaffExtraDto economicStaffExtraDto);

    /**
     * Map EconomicStaffExtra to EconomicStaffExtraDo
     *
     * @param economicStaffExtraAddrequest economicStaffExtraAddrequest
     * @return EconomicStaffExtraDto
     */
    EconomicStaffExtraDto addRequestToDto(EconomicStaffExtraAddrequest economicStaffExtraAddrequest);

    /**
     * Map EconomicStaffExtra to EconomicStaffExtraDo
     *
     * @param economicStaffExtraUpdaterequest economicStaffExtraUpdaterequest
     * @return EconomicStaffExtraDto
     */
    EconomicStaffExtraDto updateRequestToDto(EconomicStaffExtraUpdaterequest economicStaffExtraUpdaterequest);

    /**
     * Map economicStaffExtra to economicStaffExtraDo
     *
     * @param economicStaffExtra economicStaffExtra
     * @return EconomicStaffExtraDto
     */
    @Mapping(source = "_id", target="economicStaffExtraId")
    EconomicStaffExtraDto modelToDto(EconomicStaffExtra economicStaffExtra);
}
