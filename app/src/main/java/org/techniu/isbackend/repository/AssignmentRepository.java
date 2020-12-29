package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.Assignment;
import org.techniu.isbackend.entity.Client;
import org.techniu.isbackend.entity.Staff;

import java.util.List;
@Repository
public interface AssignmentRepository extends MongoRepository<Assignment, String> {
    List<Assignment> findByClient(Client client);
    List<Assignment> findByStaff(Staff staff);
    Assignment findByClientAndTypeStaff(Client client,String typeStaff);
   List<Assignment> findByClientAndStaffAndTypeStaff(Client client,Staff staff,String typeStaff);
    List<Assignment> findAll();
    Assignment findBy_id(String id);
}
