package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.*;

import java.util.List;

public interface StaffContractRepository extends MongoRepository<StaffContract, String> {

    StaffContract findByPersonalNumber(String personalNumber);

    StaffContract findBy_id(String id);

    List<StaffContract> findAllByContractType(ContractType contractType);

    List<StaffContract> findAllByLegalCategoryType(LegalCategoryType legalCategoryType);

    List<StaffContract> findAllByContractModel(ContractModel contractModel);

    List<StaffContract> findAllByCompany(FinancialCompany company);

}
