package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.CommercialOperationStatus;
import org.techniu.isbackend.entity.ContactByOperation;

import java.util.List;

public interface ContactByOperationRepository extends MongoRepository<ContactByOperation, String> {
    ContactByOperation findBy_id(String id);
    ContactByOperation findBy_idAndContactsType(String id,String contactype);
    List<ContactByOperation> findByStatus(CommercialOperationStatus commercialOperationStatus);
}
