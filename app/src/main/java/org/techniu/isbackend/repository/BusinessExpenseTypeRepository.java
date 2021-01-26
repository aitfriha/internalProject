package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.BusinessExpenseType;

import java.util.List;

@Repository
public interface BusinessExpenseTypeRepository extends MongoRepository<BusinessExpenseType, String> {
    List<BusinessExpenseType> findAll();
    BusinessExpenseType findBusinessExpenseTypeByMasterValue(String masterValue);
    BusinessExpenseType findBusinessExpenseTypeByName(String name);
    BusinessExpenseType findBusinessExpenseTypeByCode(String code);
}
