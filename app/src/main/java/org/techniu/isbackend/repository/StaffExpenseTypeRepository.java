package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.StaffExpenseType;

@Repository
public interface StaffExpenseTypeRepository extends MongoRepository<StaffExpenseType, String> {
    StaffExpenseType findStaffExpenseTypeByName(String name);
    StaffExpenseType findStaffExpenseTypeByCode(String code);
    StaffExpenseType findStaffExpenseTypeByMasterValue(String masterValue);
}
