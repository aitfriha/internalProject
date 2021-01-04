package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.LocalBankHolidayAddrequest;
import org.techniu.isbackend.controller.request.LocalBankHolidayUpdaterequest;
import org.techniu.isbackend.dto.model.LocalBankHolidayDto;
import org.techniu.isbackend.entity.LocalBankHoliday;

@Mapper(componentModel = "spring")
public interface LocalBankHolidayMapper {
    /**
     * Map dto to model
     *
     * @param localBankHolidayDto localBankHolidayDto
     * @return LocalBankHoliday
     */
    @Mapping(source = "localBankHolidayId", target="_id")
    LocalBankHoliday dtoToModel(LocalBankHolidayDto localBankHolidayDto);

    /**
     * Map LocalBankHoliday to LocalBankHolidayDo
     *
     * @param localBankHolidayAddrequest localBankHolidayAddrequest
     * @return LocalBankHolidayDto
     */
    LocalBankHolidayDto addRequestToDto(LocalBankHolidayAddrequest localBankHolidayAddrequest);

    /**
     * Map LocalBankHoliday to LocalBankHolidayDo
     *
     * @param localBankHolidayUpdaterequest localBankHolidayUpdaterequest
     * @return LocalBankHolidayDto
     */
    LocalBankHolidayDto updateRequestToDto(LocalBankHolidayUpdaterequest localBankHolidayUpdaterequest);

    /**
     * Map localBankHoliday to localBankHolidayDo
     *
     * @param localBankHoliday localBankHoliday
     * @return LocalBankHolidayDto
     */
    @Mapping(source = "_id", target="localBankHolidayId")
    LocalBankHolidayDto modelToDto(LocalBankHoliday localBankHoliday);
}
