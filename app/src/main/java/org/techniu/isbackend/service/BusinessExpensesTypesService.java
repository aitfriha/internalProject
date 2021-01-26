package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.BusinessExpenseSubtypeDto;
import org.techniu.isbackend.dto.model.BusinessExpenseTypeDto;

import java.util.HashMap;
import java.util.List;

public interface BusinessExpensesTypesService {

    /**
     * Get All Business Expenses Types
     *
     * @return List<BusinessExpenseTypeDto>
     */
    List<BusinessExpenseTypeDto> getAllTypes();

    /**
     * Save business expense Type
     *
     * @param  businessExpenseTypeDto
     *
     */
    void saveType(BusinessExpenseTypeDto businessExpenseTypeDto);

    /**
     * Update business expense Type
     *
     * @param  businessExpenseTypeDto
     *
     */
    void updateType(BusinessExpenseTypeDto businessExpenseTypeDto);

    /**
     * Delete businessExpenseType
     *
     * @param businessExpenseTypeId
     */
    void deleteType(String businessExpenseTypeId);

    /**
     * Save business expense Subtype
     *
     * @param  businessExpenseSubtypeDto
     *
     */
    void saveSubtype(BusinessExpenseSubtypeDto businessExpenseSubtypeDto);

    /**
     * Update business expense Subtype
     *
     * @param  businessExpenseSubtypeDto
     *
     */
    void updateSubtype(BusinessExpenseSubtypeDto businessExpenseSubtypeDto);

    /**
     * Delete businessExpenseSubtype
     *
     * @param data
     */
    void deleteSubtype(HashMap data);

    /**
     * get businessExpenseSubtype
     *
     * @param masterValue
     * @param subtypeId
     */
    BusinessExpenseSubtypeDto getSubtypeBy(String masterValue, String subtypeId);

}
