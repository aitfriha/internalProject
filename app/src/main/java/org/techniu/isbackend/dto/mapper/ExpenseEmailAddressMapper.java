package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.ExpenseEmailAddressAddRequest;
import org.techniu.isbackend.controller.request.ExpenseEmailAddressUpdateRequest;
import org.techniu.isbackend.dto.model.ExpenseEmailAddressDto;
import org.techniu.isbackend.entity.ExpenseEmailAddress;

@Mapper(componentModel = "spring")
public interface ExpenseEmailAddressMapper {
    /**
     * Map dto to model
     *
     * @param expenseEmailAddressDto
     * @return ExpenseEmailAddress
     */
    @Mapping(source = "id", target="_id")
    ExpenseEmailAddress dtoToModel(ExpenseEmailAddressDto expenseEmailAddressDto);

    /**
     * Map ExpenseEmailAddressAddRequest to expenseEmailAddressDto
     *
     * @param expenseEmailAddressAddRequest
     * @return ExpenseEmailAddressDto
     */
    ExpenseEmailAddressDto addRequestToDto(ExpenseEmailAddressAddRequest expenseEmailAddressAddRequest);


    /**
     * Map ExpenseEmailAddressUpdateRequest to ExpenseEmailAddressDto
     *
     * @param expenseEmailAddressUpdateRequest
     * @return ExpenseEmailAddressDto
     */
    ExpenseEmailAddressDto updateRequestToDto(ExpenseEmailAddressUpdateRequest expenseEmailAddressUpdateRequest);

    /**
     * Map model to dto
     *
     * @param expenseEmailAddress
     * @return ExpenseEmailAddressDto
     */
    @Mapping(source = "_id", target="id")
    ExpenseEmailAddressDto modelToDto(ExpenseEmailAddress expenseEmailAddress);
}
