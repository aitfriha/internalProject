package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.CommercialOperation;
import org.techniu.isbackend.entity.Staff;
import org.techniu.isbackend.entity.StaffAssignment;

import java.util.List;

@Repository
public interface StaffAssignmentRepository extends MongoRepository<StaffAssignment, String> {
    StaffAssignment findStaffAssignmentBy_id(String id);
    StaffAssignment findStaffAssignmentByStaffAndOperation(Staff staff, CommercialOperation operation);
    List<StaffAssignment> findAllByOperation(CommercialOperation operation);
    List<StaffAssignment> findAllByOperationAndActive(CommercialOperation operation, boolean active);
}
