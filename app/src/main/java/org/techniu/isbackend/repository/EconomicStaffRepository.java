package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.*;

@Repository
public interface EconomicStaffRepository extends MongoRepository<EconomicStaff, String> {
    EconomicStaff findAllBy_id(String id);
}
