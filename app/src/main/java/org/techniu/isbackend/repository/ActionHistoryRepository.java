package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.ActionHistory;

public interface ActionHistoryRepository extends MongoRepository<ActionHistory, String> {

    ActionHistory findAllBy_id(String id);
}
