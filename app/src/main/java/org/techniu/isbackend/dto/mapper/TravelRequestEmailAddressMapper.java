package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.TravelRequestEmailAddressAddRequest;
import org.techniu.isbackend.controller.request.TravelRequestEmailAddressUpdateRequest;
import org.techniu.isbackend.dto.model.TravelRequestEmailAddressDto;
import org.techniu.isbackend.entity.TravelRequestEmailAddress;

@Mapper(componentModel = "spring")
public interface TravelRequestEmailAddressMapper {
    /**
     * Map dto to model
     *
     * @param emailAddressDto
     * @return EmailAddress
     */
    @Mapping(source = "id", target="_id")
    TravelRequestEmailAddress dtoToModel(TravelRequestEmailAddressDto emailAddressDto);

    /**
     * Map EmailAddressAddRequest to EmailAddressDto
     *
     * @param travelRequestEmailAddressAddRequest
     * @return EmailAddressDto
     */
    TravelRequestEmailAddressDto addRequestToDto(TravelRequestEmailAddressAddRequest travelRequestEmailAddressAddRequest);


    /**
     * Map EmailAddressUpdateRequest to EmailAddressDto
     *
     * @param travelRequestEmailAddressUpdateRequest
     * @return TravelRequestEmailAddressDto
     */
    TravelRequestEmailAddressDto updateRequestToDto(TravelRequestEmailAddressUpdateRequest travelRequestEmailAddressUpdateRequest);

    /**
     * Map model to dto
     *
     * @param emailAddress
     * @return TravelRequestEmailAddressDto
     */
    @Mapping(source = "_id", target="id")
    TravelRequestEmailAddressDto modelToDto(TravelRequestEmailAddress emailAddress);
}
