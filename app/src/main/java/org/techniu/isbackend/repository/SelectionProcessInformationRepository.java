package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.SelectionProcessInformation;
import org.techniu.isbackend.entity.StateCountry;

import java.util.List;

public interface SelectionProcessInformationRepository extends MongoRepository<SelectionProcessInformation, String> {

}
