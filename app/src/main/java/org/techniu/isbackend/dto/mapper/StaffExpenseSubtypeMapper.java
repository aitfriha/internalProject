package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.StaffExpensesSubtypeAddRequest;
import org.techniu.isbackend.controller.request.StaffExpensesSubtypeUpdateRequest;
import org.techniu.isbackend.dto.model.StaffExpenseSubtypeDto;
import org.techniu.isbackend.entity.StaffExpenseSubtype;

@Mapper(componentModel = "spring")
public interface StaffExpenseSubtypeMapper {
    /**
     * Map dto to model
     *
     * @param staffExpenseSubtypeDto
     * @return StaffExpenseSubtype
     */
    @Mapping(source = "id", target="_id")
    StaffExpenseSubtype dtoToModel(StaffExpenseSubtypeDto staffExpenseSubtypeDto);

    /**
     * Map StaffExpensesSubtypeAddRequest to StaffExpenseSubtypeDto
     *
     * @param staffExpensesSubtypeAddRequest
     * @return StaffExpenseSubtypeDto
     */
    StaffExpenseSubtypeDto addRequestToDto(StaffExpensesSubtypeAddRequest staffExpensesSubtypeAddRequest);

    /**
     * Map StaffExpensesSubtypeUpdateRequest to StaffExpenseSubtypeDto
     *
     * @param staffExpensesSubtypeUpdateRequest
     * @return StaffExpenseSubtypeDto
     */
    StaffExpenseSubtypeDto updateRequestToDto(StaffExpensesSubtypeUpdateRequest staffExpensesSubtypeUpdateRequest);



    /**
     * Map model to dto
     *
     * @param staffExpenseSubtype
     * @return StaffExpenseSubtypeDto
     */
    @Mapping(source = "_id", target="id")
    StaffExpenseSubtypeDto modelToDto(StaffExpenseSubtype staffExpenseSubtype);
}
