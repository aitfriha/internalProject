package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.SupplierType;

public interface SupplierTypeRepository extends MongoRepository<SupplierType, String> {

    SupplierType findAllBy_id(String id);
}
