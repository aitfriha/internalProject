package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.AbsenceRequest;
import org.techniu.isbackend.entity.AbsenceType;
import org.techniu.isbackend.entity.StateCountry;

import java.util.List;

public interface AbsenceRequestRepository extends MongoRepository<AbsenceRequest, String> {

}
