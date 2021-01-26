package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.StaffExpenseSubtypeDto;
import org.techniu.isbackend.dto.model.StaffExpenseTypeDto;

import java.util.HashMap;
import java.util.List;

public interface StaffExpensesTypesService {

    /**
     * Get All Staff Expenses Types
     *
     * @return List<StaffExpenseTypeDto>
     */
    List<StaffExpenseTypeDto> getAllTypes();

    /**
     * Save staff expense Type
     *
     * @param  staffExpenseTypeDto
     *
     */
    void saveType(StaffExpenseTypeDto staffExpenseTypeDto);

    /**
     * Update staff expense Type
     *
     * @param  staffExpenseTypeDto
     *
     */
    void updateType(StaffExpenseTypeDto staffExpenseTypeDto);

    /**
     * Delete staffExpenseType
     *
     * @param staffExpenseTypeId
     */
    void deleteType(String staffExpenseTypeId);

    /**
     * Save staff expense Subtype
     *
     * @param  staffExpenseSubtypeDto
     *
     */
    void saveSubtype(StaffExpenseSubtypeDto staffExpenseSubtypeDto);

    /**
     * Update staff expense Subtype
     *
     * @param  staffExpenseSubtypeDto
     *
     */
    void updateSubtype(StaffExpenseSubtypeDto staffExpenseSubtypeDto);

    /**
     * Delete staffExpenseSubtype
     *
     * @param data
     */
    void deleteSubtype(HashMap data);

    /**
     * get staffExpenseSubtype
     *
     * @param masterValue
     * @param subtypeId
     */
    StaffExpenseSubtypeDto getSubtypeBy(String masterValue, String subtypeId);

}
