package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.CivilityTitle;
import org.techniu.isbackend.entity.Contact;

import java.util.List;

public interface ContactRepository extends MongoRepository<Contact, String> {

    Contact findByFirstName(String firstName);
    Contact findByPersonalEmail(String personalEmail);
    Contact findBy_id(String id);
    List<Contact> findByCivilityTitle(CivilityTitle civilityTitle);
}
