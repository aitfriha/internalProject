package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.BusinessExpensesTypeAddRequest;
import org.techniu.isbackend.controller.request.BusinessExpensesTypeUpdateRequest;
import org.techniu.isbackend.dto.model.BusinessExpenseTypeDto;
import org.techniu.isbackend.entity.BusinessExpenseType;

@Mapper(componentModel = "spring")
public interface BusinessExpenseTypeMapper {
    /**
     * Map dto to model
     *
     * @param businessExpenseTypeDto
     * @return StaffExpenseType
     */
    @Mapping(source = "id", target="_id")
    @Mapping(target = "subtypes", ignore=true)
    BusinessExpenseType dtoToModel(BusinessExpenseTypeDto businessExpenseTypeDto);

    /**
     * Map BusinessExpensesTypeAddRequest to BusinessExpenseTypeDto
     *
     * @param businessExpensesTypeAddRequest
     * @return BusinessExpenseTypeDto
     */
    BusinessExpenseTypeDto addRequestToDto(BusinessExpensesTypeAddRequest businessExpensesTypeAddRequest);

    /**
     * Map BusinessExpensesTypeUpdateRequest to BusinessExpenseTypeDto
     *
     * @param businessExpensesTypeUpdateRequest
     * @return BusinessExpenseTypeDto
     */
    BusinessExpenseTypeDto updateRequestToDto(BusinessExpensesTypeUpdateRequest businessExpensesTypeUpdateRequest);


    /**
     * Map model to dto
     *
     * @param businessExpenseType
     * @return BusinessExpenseTypeDto
     */
    @Mapping(source = "_id", target="id")
    @Mapping(target = "subtypes", ignore=true)
    BusinessExpenseTypeDto modelToDto(BusinessExpenseType businessExpenseType);
}
