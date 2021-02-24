package com.wproducts.administration.repository;

import com.wproducts.administration.model.Action;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ActionRepository extends MongoRepository<Action, String> {
    Action findBy_id(String s);
}
