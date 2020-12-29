package org.techniu.isbackend.service;

import org.springframework.http.ResponseEntity;
import org.techniu.isbackend.dto.model.AssignmentDto;
import org.techniu.isbackend.entity.Address;
import org.techniu.isbackend.entity.Assignment;

import java.util.List;

public interface AssignmentService {
    Assignment saveAssignment(Assignment assignment);
    Assignment updateAssignment(String assignmentId, Assignment assignment);
    ResponseEntity<?> deleteAssignment(String assignmentId);
    List<Assignment> getAllAssignment();
    List<Assignment> getAssignmentByClient(String clientId);
    List<Assignment> getAssignmentByPeople(String staffId);

    /**
     * save
     *
     * @param assignmentDto - assignmentDto
     * @param staffId - staffId
     * @param clients - clients
     */
    void save(AssignmentDto assignmentDto, String staffId, List<String> clients);
    /**
     * delete Assignment
     *
     * @param id - id
     */
    void remove(String id);
}
