package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.ExpenseStatus;

@Repository
public interface ExpenseStatusRepository extends MongoRepository<ExpenseStatus, String> {
    ExpenseStatus findExpenseStatusByCode(String code);
    ExpenseStatus findExpenseStatusBy_id(String id);
    ExpenseStatus findExpenseStatusByName(String name);
    ExpenseStatus findExpenseStatusByMasterValue(String masterValue);
}
