package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.TravelRequestAddRequest;
import org.techniu.isbackend.controller.request.TravelRequestUpdateRequest;
import org.techniu.isbackend.dto.model.TravelRequestDto;
import org.techniu.isbackend.entity.TravelRequest;

@Mapper(componentModel = "spring")
public interface TravelRequestMapper {
    /**
     * Map dto to model
     *
     * @param travelRequestDto
     * @return TravelRequest
     */
    @Mapping(source = "id", target="_id")
    @Mapping(target = "requester", ignore=true)
    @Mapping(target = "status", ignore=true)
    TravelRequest dtoToModel(TravelRequestDto travelRequestDto);

    /**
     * Map TravelRequestAddRequest to TravelRequestDto
     *
     * @param travelRequestAddRequest
     * @return TravelRequestDto
     */
    TravelRequestDto addRequestToDto(TravelRequestAddRequest travelRequestAddRequest);

    /**
     * Map TravelRequestUpdateRequest to TravelRequestDto
     *
     * @param travelRequestUpdateRequest
     * @return TravelRequestDto
     */
    TravelRequestDto updateRequestToDto(TravelRequestUpdateRequest travelRequestUpdateRequest);


    /**
     * Map model to dto
     *
     * @param travelRequest
     * @return TravelRequestDto
     */
    @Mapping(source = "_id", target="id")
    @Mapping(source = "requester.staffId", target="requesterId")
    @Mapping(source = "requester.staffContract.personalNumber", target="requesterPersonalNumber")
    @Mapping(source = "requester.photo", target="requesterAvatar")
    @Mapping(source = "requester.firstName", target="requesterName")
    @Mapping(source = "requester.fatherFamilyName", target="requesterFatherFamilyName")
    @Mapping(source = "requester.motherFamilyName", target="requesterMotherFamilyName")
    @Mapping(source = "requester.staffContract.company.name", target="requesterCompany")
    @Mapping(source = "requester.companyEmail", target="requesterCompanyEmail")
    @Mapping(source = "status._id", target="requestStatusId")
    @Mapping(source = "status.code", target="requestStatusCode")
    @Mapping(source = "status.name", target="requestStatusName")
    @Mapping(source = "status.masterValue", target="requestStatusMasterValue")
    TravelRequestDto modelToDto(TravelRequest travelRequest);
}
