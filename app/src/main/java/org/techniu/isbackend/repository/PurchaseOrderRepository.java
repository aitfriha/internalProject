package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.PurchaseOrder;

public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrder, String> {

    PurchaseOrder findAllBy_id(String id);

}
