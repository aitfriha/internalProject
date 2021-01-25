package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.PersonType;

@Repository
public interface PersonTypeRepository extends MongoRepository<PersonType, String> {
    PersonType findPersonTypeByCode(String code);
    PersonType findPersonTypeByName(String name);
    PersonType findPersonTypeBy_id(String id);
}
