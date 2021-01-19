package com.wproducts.administration.dto.mapper;

import com.wproducts.administration.controller.request.ActionAddRequest;
import com.wproducts.administration.controller.request.ActionUpdateRequest;
import com.wproducts.administration.dto.model.ActionDto;
import com.wproducts.administration.model.Action;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface ActionMapper {
    /**
     * Map dto to model
     *
     * @param actionDto actionDto
     * @return Action
     */
    @Mapping(source = "actionId", target="_id")
    Action dtoToModel(ActionDto actionDto);
    /**
     * Map model to dto
     *
     * @param action action
     * @return ActionDto
     */
    @Mapping(source = "_id", target="actionId")
    @Mapping(target = "actionCreatedAt", ignore=true)
    @Mapping(target = "actionUpdatedAt", ignore=true)
    ActionDto modelToDto(Action action);

    /**
     * Map add request to dto
     *
     * @param actionAddRequest actionAddRequest
     * @return ActionDto
     */
    ActionDto addRequestToDto(ActionAddRequest actionAddRequest);

    /**
     * Map update request to dto
     *
     * @param actionUpdateRequest actionUpdateRequest
     * @return ActionDto
     */
    ActionDto updateRequestToDto(ActionUpdateRequest actionUpdateRequest);

}
