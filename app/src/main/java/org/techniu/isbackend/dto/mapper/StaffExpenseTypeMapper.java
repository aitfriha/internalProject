package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.StaffExpensesTypeAddRequest;
import org.techniu.isbackend.controller.request.StaffExpensesTypeUpdateRequest;
import org.techniu.isbackend.dto.model.StaffExpenseTypeDto;
import org.techniu.isbackend.entity.StaffExpenseType;

@Mapper(componentModel = "spring")
public interface StaffExpenseTypeMapper {
    /**
     * Map dto to model
     *
     * @param staffExpenseTypeDto
     * @return StaffExpenseType
     */
    @Mapping(source = "id", target="_id")
    @Mapping(target = "subtypes", ignore=true)
    StaffExpenseType dtoToModel(StaffExpenseTypeDto staffExpenseTypeDto);

    /**
     * Map StaffExpensesTypeAddRequest to StaffExpenseTypeDto
     *
     * @param staffExpensesTypeAddRequest
     * @return StaffExpenseTypeDto
     */
    StaffExpenseTypeDto addRequestToDto(StaffExpensesTypeAddRequest staffExpensesTypeAddRequest);

    /**
     * Map StaffExpensesTypeUpdateRequest to StaffExpenseTypeDto
     *
     * @param staffExpensesTypeUpdateRequest
     * @return StaffExpenseTypeDto
     */
    StaffExpenseTypeDto updateRequestToDto(StaffExpensesTypeUpdateRequest staffExpensesTypeUpdateRequest);


    /**
     * Map model to dto
     *
     * @param staffExpenseType
     * @return StaffExpenseTypeDto
     */
    @Mapping(source = "_id", target="id")
    @Mapping(target = "subtypes", ignore=true)
    StaffExpenseTypeDto modelToDto(StaffExpenseType staffExpenseType);
}
