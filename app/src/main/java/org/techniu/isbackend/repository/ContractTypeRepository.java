package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.CommercialOperationStatus;
import org.techniu.isbackend.entity.ContractType;
import org.techniu.isbackend.entity.StateCountry;

import java.util.List;

public interface ContractTypeRepository extends MongoRepository<ContractType, String> {

    List<ContractType> getAllByState(StateCountry stateCountry);
    ContractType findBy_id(String id);
    ContractType findByNameAndState(String name, StateCountry state);
    ContractType findByCodeAndState(String code, StateCountry state);
}
