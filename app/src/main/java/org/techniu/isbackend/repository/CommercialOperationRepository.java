package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.CommercialOperation;
import org.techniu.isbackend.entity.ServiceType;

import java.util.List;
@Repository
public interface CommercialOperationRepository extends MongoRepository<CommercialOperation, String> {

    CommercialOperation findByName(String name);
    CommercialOperation findByNameIgnoreCase(String name);
    CommercialOperation findByCode(String code);
    CommercialOperation findBy_id(String id);
    List<CommercialOperation> findByServiceType(ServiceType serviceType);
}
