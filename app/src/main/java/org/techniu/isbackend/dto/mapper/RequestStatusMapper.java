package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.RequestStatusAddRequest;
import org.techniu.isbackend.controller.request.RequestStatusUpdateRequest;
import org.techniu.isbackend.dto.model.RequestStatusDto;
import org.techniu.isbackend.entity.RequestStatus;

@Mapper(componentModel = "spring")
public interface RequestStatusMapper {
    /**
     * Map dto to model
     *
     * @param requestStatusDto
     * @return RequestStatus
     */
    @Mapping(source = "id", target="_id")
    RequestStatus dtoToModel(RequestStatusDto requestStatusDto);

    /**
     * Map RequestStatusAddRequest to RequestStatusDto
     *
     * @param requestStatusAddRequest
     * @return RequestStatusDto
     */
    RequestStatusDto addRequestToDto(RequestStatusAddRequest requestStatusAddRequest);

    /**
     * Map RequestStatusUpdateRequest to RequestStatusDto
     *
     * @param requestStatusUpdateRequest
     * @return RequestStatusDto
     */
    RequestStatusDto updateRequestToDto(RequestStatusUpdateRequest requestStatusUpdateRequest);

    /**
     * Map model to dto
     *
     * @param requestStatus
     * @return RequestStatusDto
     */
    @Mapping(source = "_id", target="id")
    RequestStatusDto modelToDto(RequestStatus requestStatus);
}
