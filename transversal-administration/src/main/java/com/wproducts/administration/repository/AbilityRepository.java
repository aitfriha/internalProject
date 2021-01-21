package com.wproducts.administration.repository;

import com.wproducts.administration.model.Ability;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AbilityRepository extends MongoRepository<Ability, String> {
    Ability findAbilityBy_id(String s);
}
