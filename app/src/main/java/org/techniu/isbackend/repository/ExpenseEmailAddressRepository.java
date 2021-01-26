package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.ExpenseEmailAddress;

@Repository
public interface ExpenseEmailAddressRepository extends MongoRepository<ExpenseEmailAddress, String> {
    ExpenseEmailAddress findEmailAddressByEmail(String email);
    ExpenseEmailAddress findEmailAddressByActionAndEmail(String action, String email);
    ExpenseEmailAddress findEmailAddressByAction(String action);
}
