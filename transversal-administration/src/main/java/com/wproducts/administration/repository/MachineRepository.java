package com.wproducts.administration.repository;


import com.wproducts.administration.model.Machine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends MongoRepository<Machine, String> {
    Machine findByMachineSerialNumber(String s);
    Machine findMachineBy_id(String s);
}
