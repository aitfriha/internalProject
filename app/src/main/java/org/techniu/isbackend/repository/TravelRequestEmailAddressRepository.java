package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.TravelRequestEmailAddress;

@Repository
public interface TravelRequestEmailAddressRepository extends MongoRepository<TravelRequestEmailAddress, String> {
    TravelRequestEmailAddress findEmailAddressByEmail(String email);
}
