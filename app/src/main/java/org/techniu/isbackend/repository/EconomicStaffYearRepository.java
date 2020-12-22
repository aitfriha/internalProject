package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.EconomicStaffYear;

@Repository
public interface EconomicStaffYearRepository extends MongoRepository<EconomicStaffYear, String> {
    EconomicStaffYear findAllBy_id(String id);
}
