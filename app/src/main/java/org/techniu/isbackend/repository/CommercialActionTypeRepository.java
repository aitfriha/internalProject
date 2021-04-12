package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.CommercialActionType;

public interface CommercialActionTypeRepository extends MongoRepository<CommercialActionType, String> {

    CommercialActionType findAllBy_id(String id);
}
