package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.Expense;
import org.techniu.isbackend.entity.ExpenseStatus;
import org.techniu.isbackend.entity.Staff;

import java.util.List;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    Expense findExpenseBy_id(String id);
    List<Expense> findAllByStatus(ExpenseStatus status);
    List<Expense> findAllByStaff(Staff staff);
}
