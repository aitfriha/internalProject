package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.ExternalSupplier;
import org.techniu.isbackend.entity.SupplierType;

public interface ExternalSupplierRepository extends MongoRepository<ExternalSupplier, String> {

    ExternalSupplier findAllBy_id(String id);
}
