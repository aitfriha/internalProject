package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.CommercialAction;

public interface CommercialActionRepository extends MongoRepository<CommercialAction, String> {

    CommercialAction findAllBy_id(String id);
}
