package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.AssignmentType;

import java.util.List;

@Repository
public interface AssignmentTypeRepository extends MongoRepository<AssignmentType, String> {
    List<AssignmentType> findAll();
    AssignmentType findAssignmentTypeBy_id(String id);
    AssignmentType findAssignmentTypeByCode(String code);
    AssignmentType findAssignmentTypeByName(String name);
}
