package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.*;

import java.util.List;

public interface AbsenceRequestRepository extends MongoRepository<AbsenceRequest, String> {

    List<AbsenceRequest> findAllByAbsenceType(AbsenceType absenceType);
    List<AbsenceRequest> findAllByStaffAndStateEquals(Staff staff, String state); //"APPROVED"
    List<AbsenceRequest> findAllByStaff(Staff staff);


}
