package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.AssignmentType;
import org.techniu.isbackend.entity.Staff;
import org.techniu.isbackend.entity.WeeklyWork;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface WeeklyWorkRepository extends MongoRepository<WeeklyWork, String> {
    List<WeeklyWork> findWeeklyWorkByStaffAndYearAndWeek(Staff staff, int year, int week);
    WeeklyWork findWeeklyWorkBy_id(String id);
    ArrayList<WeeklyWork> findAllByStaff(Staff staff);
    ArrayList<WeeklyWork> findAllByAssignmentType(AssignmentType assignmentType);
}
