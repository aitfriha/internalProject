package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.ServiceType;

public interface ServiceTypeRepository extends MongoRepository<ServiceType, String> {

    ServiceType findByName(String name);
    ServiceType findByNameIgnoreCase(String name);
    ServiceType findBy_id(String id);
}
