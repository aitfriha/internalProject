package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.ExpenseStatusAddRequest;
import org.techniu.isbackend.controller.request.ExpenseStatusUpdateRequest;
import org.techniu.isbackend.dto.model.ExpenseStatusDto;
import org.techniu.isbackend.entity.ExpenseStatus;

@Mapper(componentModel = "spring")
public interface ExpenseStatusMapper {
    /**
     * Map dto to model
     *
     * @param expenseStatusDto
     * @return ExpenseStatus
     */
    @Mapping(source = "id", target="_id")
    ExpenseStatus dtoToModel(ExpenseStatusDto expenseStatusDto);

    /**
     * Map ExpenseStatusAddRequest to ExpenseStatusDto
     *
     * @param expenseStatusAddRequest
     * @return ExpenseStatusDto
     */
    ExpenseStatusDto addRequestToDto(ExpenseStatusAddRequest expenseStatusAddRequest);

    /**
     * Map ExpenseStatusUpdateRequest to ExpenseStatusDto
     *
     * @param expenseStatusUpdateRequest
     * @return ExpenseStatusDto
     */
    ExpenseStatusDto updateRequestToDto(ExpenseStatusUpdateRequest expenseStatusUpdateRequest);

    /**
     * Map model to dto
     *
     * @param expenseStatus
     * @return ExpenseStatusDto
     */
    @Mapping(source = "_id", target="id")
    ExpenseStatusDto modelToDto(ExpenseStatus expenseStatus);
}
