package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.CommercialActionTypeAddrequest;
import org.techniu.isbackend.controller.request.CommercialActionTypeUpdaterequest;
import org.techniu.isbackend.dto.model.CommercialActionTypeDto;
import org.techniu.isbackend.entity.CommercialActionType;

@Mapper(componentModel = "spring")
public interface CommercialActionTypeMapper {
    /**
     * Map dto to model
     *
     * @param commercialActionTypeDto commercialActionTypeDto
     * @return CommercialActionType
     */
    @Mapping(source = "actionTypeId", target="_id")
    CommercialActionType dtoToModel(CommercialActionTypeDto commercialActionTypeDto);

    /**
     * Map CommercialActionType to CommercialActionTypeDo
     *
     * @param commercialActionTypeAddrequest commercialActionTypeAddrequest
     * @return CommercialActionTypeDto
     */
    CommercialActionTypeDto addRequestToDto(CommercialActionTypeAddrequest commercialActionTypeAddrequest);

    /**
     * Map CommercialActionType to CommercialActionTypeDo
     *
     * @param commercialActionTypeUpdaterequest commercialActionTypeUpdaterequest
     * @return CommercialActionTypeDto
     */
    CommercialActionTypeDto updateRequestToDto(CommercialActionTypeUpdaterequest commercialActionTypeUpdaterequest);

    /**
     * Map commercialActionType to commercialActionTypeDo
     *
     * @param commercialActionType commercialActionType
     * @return CommercialActionTypeDto
     */
    @Mapping(source = "_id", target="actionTypeId")
    CommercialActionTypeDto modelToDto(CommercialActionType commercialActionType);
}
