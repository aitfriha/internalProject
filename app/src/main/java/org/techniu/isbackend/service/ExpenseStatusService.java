package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.ExpenseStatusDto;

import java.util.List;

public interface ExpenseStatusService {

    /**
     * Get All Expense Status
     *
     * @return List<ExpenseStatusDto>
     */
    List<ExpenseStatusDto> getAllExpenseStatus();

    /**
     * Save expense status
     *
     * @param  expenseStatusDto
     *
     */
    void saveExpenseStatus(ExpenseStatusDto expenseStatusDto);

    /**
     * Update expense status
     *
     * @param  expenseStatusDto
     *
     */
    void updateExpenseStatus(ExpenseStatusDto expenseStatusDto);

    /**
     * Delete expense status
     *
     * @param expenseStatusId
     */
    void deleteExpenseStatus(String expenseStatusId);

}
