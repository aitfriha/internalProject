package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.AssignmentTypeDto;

import java.util.List;

public interface AssignmentTypeService {

    /**
     * Get all assignment Types
     *
     * @return List<AssignmentType>
     */
    List<AssignmentTypeDto> getAllAssignmentTypes();

    /**
     * Save assignment Type
     *
     * @param  assignmentTypeDto
     *
     */
    void saveAssignmentType(AssignmentTypeDto assignmentTypeDto);

    /**
     * Update assignment Type
     *
     * @param  assignmentTypeDto
     *
     */
    void updateAssignmentType(AssignmentTypeDto assignmentTypeDto);

    /**
     * Delete assignmentType
     *
     * @param assignmentTypeId
     */
    void deleteAssignmentType(String assignmentTypeId);

}
