package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.SupplierPayment;

public interface SupplierPaymentRepository extends MongoRepository<SupplierPayment, String> {

    SupplierPayment findAllBy_id(String id);
}
