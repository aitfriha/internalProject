package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.PurchaseOrderAcceptance;

public interface PurchaseOrderAcceptanceRepository extends MongoRepository<PurchaseOrderAcceptance, String> {

    PurchaseOrderAcceptance findAllBy_id(String id);
}
