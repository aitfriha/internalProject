package com.wproducts.administration.repository;

import com.wproducts.administration.model.Action;
import com.wproducts.administration.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRoleName(String s);
    Role findBy_id(String s);
}
