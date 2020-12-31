package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.FinancialCompany;

public interface FinancialCompanyRepository extends MongoRepository<FinancialCompany, String> {

    FinancialCompany findAllBy_id(String id);

    FinancialCompany findByCode(String code);

}
