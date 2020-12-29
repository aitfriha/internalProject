package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.EconomicStaffExtra;

@Repository
public interface EconomicStaffExtraRepository extends MongoRepository<EconomicStaffExtra, String> {
    EconomicStaffExtra findAllBy_id(String id);
}
