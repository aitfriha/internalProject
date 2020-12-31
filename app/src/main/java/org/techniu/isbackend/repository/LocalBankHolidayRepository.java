package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.FinancialCompany;
import org.techniu.isbackend.entity.LocalBankHoliday;

import java.util.List;

public interface LocalBankHolidayRepository extends MongoRepository<LocalBankHoliday, String> {

    List<LocalBankHoliday> getAllByCompany(FinancialCompany financialCompany);
    LocalBankHoliday findBy_id(String id);
    LocalBankHoliday findByNameAndCompany(String name, FinancialCompany company);
}
