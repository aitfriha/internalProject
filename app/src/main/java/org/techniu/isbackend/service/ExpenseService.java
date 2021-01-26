package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.ExpenseDto;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface ExpenseService {

    /**
     * Get all expenses
     *
     * @param data
     * @return List<ExpenseDto>
     */
    List<ExpenseDto> getAllExpenses(HashMap data);

    /**
     * Save expense with file
     *
     * @param data
     */
    void saveExpenseWithFile(HashMap data);

    /**
     * Save expense
     *
     * @param data
     */
    void saveExpense(HashMap data);

    /**
     * ChangeStatus expense
     *
     * @param data
     */
    void changeStatusExpense(HashMap data);


    /**
     * exportExpenses
     *
     * @param data
     */
    File exportExpenses(HashMap data);

    /**
     * downloadAttachedDocumentOfExpense
     *
     * @param expenseId
     */
    byte[] downloadDocumentOfExpense(String expenseId);

    /**
     * existsExpensesWithStatus
     *
     * @param statusId
     * @return boolean
     */
    boolean existsExpensesWithStatus(String statusId);
}
