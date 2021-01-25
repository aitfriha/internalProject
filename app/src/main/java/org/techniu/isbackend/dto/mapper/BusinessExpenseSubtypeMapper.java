package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.BusinessExpensesSubtypeAddRequest;
import org.techniu.isbackend.controller.request.BusinessExpensesSubtypeUpdateRequest;
import org.techniu.isbackend.dto.model.BusinessExpenseSubtypeDto;
import org.techniu.isbackend.entity.BusinessExpenseSubtype;

@Mapper(componentModel = "spring")
public interface BusinessExpenseSubtypeMapper {
    /**
     * Map dto to model
     *
     * @param businessExpenseSubtypeDto
     * @return BusinessExpenseSubtype
     */
    @Mapping(source = "id", target="_id")
    BusinessExpenseSubtype dtoToModel(BusinessExpenseSubtypeDto businessExpenseSubtypeDto);

    /**
     * Map BusinessExpensesSubtypeAddRequest to BusinessExpenseSubtypeDto
     *
     * @param businessExpensesSubtypeAddRequest
     * @return BusinessExpenseSubtypeDto
     */
    BusinessExpenseSubtypeDto addRequestToDto(BusinessExpensesSubtypeAddRequest businessExpensesSubtypeAddRequest);

    /**
     * Map BusinessExpensesSubtypeUpdateRequest to BusinessExpenseSubtypeDto
     *
     * @param businessExpensesSubtypeUpdateRequest
     * @return BusinessExpenseSubtypeDto
     */
    BusinessExpenseSubtypeDto updateRequestToDto(BusinessExpensesSubtypeUpdateRequest businessExpensesSubtypeUpdateRequest);

    /**
     * Map model to dto
     *
     * @param businessExpenseSubtype
     * @return BusinessExpenseSubtypeDto
     */
    @Mapping(source = "_id", target="id")
    BusinessExpenseSubtypeDto modelToDto(BusinessExpenseSubtype businessExpenseSubtype);
}
