package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.SupplierContract;

public interface SupplierContractRepository extends MongoRepository<SupplierContract, String> {

    SupplierContract findAllBy_id(String id);
}
