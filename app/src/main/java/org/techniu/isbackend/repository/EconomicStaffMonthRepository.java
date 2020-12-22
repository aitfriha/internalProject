package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.EconomicStaffMonth;

@Repository
public interface EconomicStaffMonthRepository extends MongoRepository<EconomicStaffMonth, String> {
    EconomicStaffMonth findAllBy_id(String id);
}
