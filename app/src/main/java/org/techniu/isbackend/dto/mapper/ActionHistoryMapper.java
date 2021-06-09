package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.ActionHistoryAddrequest;
import org.techniu.isbackend.controller.request.ActionHistoryUpdaterequest;
import org.techniu.isbackend.dto.model.ActionHistoryDto;
import org.techniu.isbackend.entity.ActionHistory;

@Mapper(componentModel = "spring")
public interface ActionHistoryMapper {
    /**
     * Map dto to model
     *
     * @param actionHistoryDto actionHistoryDto
     * @return ActionHistory
     */
    @Mapping(source = "actionHistoryId", target="_id")
    ActionHistory dtoToModel(ActionHistoryDto actionHistoryDto);

    /**
     * Map ActionHistory to ActionHistoryDo
     *
     * @param actionHistoryAddrequest actionHistoryAddrequest
     * @return ActionHistoryDto
     */
    ActionHistoryDto addRequestToDto(ActionHistoryAddrequest actionHistoryAddrequest);

    /**
     * Map ActionHistory to ActionHistoryDo
     *
     * @param actionHistoryUpdaterequest actionHistoryUpdaterequest
     * @return ActionHistoryDto
     */
    ActionHistoryDto updateRequestToDto(ActionHistoryUpdaterequest actionHistoryUpdaterequest);

    /**
     * Map actionHistory to actionHistoryDo
     *
     * @param actionHistory actionHistory
     * @return ActionHistoryDto
     */
    @Mapping(source = "_id", target="actionHistoryId")
    ActionHistoryDto modelToDto(ActionHistory actionHistory);
}
