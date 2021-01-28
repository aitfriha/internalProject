package com.wproducts.administration.repository;

import com.wproducts.administration.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUserEmail(String s);
    User findByUserEmailAndUserIsActive(String email,boolean active);
    User findBy_id(String s);
    List<User> findByUserFullName(String s);
}
