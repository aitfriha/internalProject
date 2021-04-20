package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.CommercialActionAddrequest;
import org.techniu.isbackend.controller.request.CommercialActionUpdaterequest;
import org.techniu.isbackend.dto.model.CommercialActionDto;
import org.techniu.isbackend.entity.CommercialAction;

@Mapper(componentModel = "spring")
public interface CommercialActionMapper {
    /**
     * Map dto to model
     *
     * @param commercialActionDto commercialActionDto
     * @return CommercialAction
     */
    @Mapping(source = "commercialActionId", target="_id")
    CommercialAction dtoToModel(CommercialActionDto commercialActionDto);

    /**
     * Map commercialActionRequest to commercialActionDo
     *
     * @param commercialActionAddrequest commercialActionAddrequest
     * @return CommercialActionDto
     */
    CommercialActionDto addRequestToDto(CommercialActionAddrequest commercialActionAddrequest);
    /**
     * Map commercialActionRequest to commercialActionDo
     *
     * @param commercialActionUpdaterequest commercialActionUpdaterequest
     * @return CommercialActionDto
     */
    CommercialActionDto updateRequestToDto(CommercialActionUpdaterequest commercialActionUpdaterequest);

    /**
     * Map commercialAction to commercialActionDo
     *
     * @param commercialAction commercialAction
     * @return CommercialActionDto
     */
    @Mapping(source = "_id", target="commercialActionId")
    CommercialActionDto modelToDto(CommercialAction commercialAction);
}
