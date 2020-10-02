package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.FunctionalStructureLevel;
import org.techniu.isbackend.entity.Staff;

import java.util.List;

public interface StaffRepository extends MongoRepository<Staff, String> {

    List<Staff> findAllByLevelIsNull();

    List<Staff> findAllByLevel(FunctionalStructureLevel level);
}
