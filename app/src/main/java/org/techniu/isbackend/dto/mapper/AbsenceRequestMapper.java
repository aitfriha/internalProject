package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.AbsenceRequestAddrequest;
import org.techniu.isbackend.controller.request.AbsenceRequestUpdaterequest;
import org.techniu.isbackend.dto.model.AbsenceRequestDto;
import org.techniu.isbackend.entity.AbsenceRequest;

@Mapper(componentModel = "spring")
public interface AbsenceRequestMapper {
    /**
     * Map dto to model
     *
     * @param absenceRequestDto absenceRequestDto
     * @return AbsenceRequest
     */
    @Mapping(source = "absenceRequestId", target="_id")
    AbsenceRequest dtoToModel(AbsenceRequestDto absenceRequestDto);

    /**
     * Map AbsenceRequest to AbsenceRequestDo
     *
     * @param absenceRequestAddrequest absenceRequestAddrequest
     * @return AbsenceRequestDto
     */
    AbsenceRequestDto addRequestToDto(AbsenceRequestAddrequest absenceRequestAddrequest);

    /**
     * Map AbsenceRequest to AbsenceRequestDo
     *
     * @param absenceRequestUpdaterequest absenceRequestUpdaterequest
     * @return AbsenceRequestDto
     */
    AbsenceRequestDto updateRequestToDto(AbsenceRequestUpdaterequest absenceRequestUpdaterequest);

    /**
     * Map absenceRequest to absenceRequestDo
     *
     * @param absenceRequest absenceRequest
     * @return AbsenceRequestDto
     */
    @Mapping(source = "_id", target="absenceRequestId")
    AbsenceRequestDto modelToDto(AbsenceRequest absenceRequest);
}
