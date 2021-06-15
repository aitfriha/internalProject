package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.AbsenceType;
import org.techniu.isbackend.entity.AbsenceType;
import org.techniu.isbackend.entity.StateCountry;

import java.util.List;

public interface AbsenceTypeRepository extends MongoRepository<AbsenceType, String> {

    List<AbsenceType> getAllByState(StateCountry stateCountry);
    AbsenceType findBy_id(String id);
    AbsenceType findByNameAndState(String name, StateCountry state);
    AbsenceType findByCodeAndState(String code, StateCountry state);
    AbsenceType findByCode(String code);
}
