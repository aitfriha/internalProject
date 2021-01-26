package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.dto.model.StaffAssignmentDto;
import org.techniu.isbackend.entity.StaffAssignment;

@Mapper(componentModel = "spring")
public interface StaffAssignmentMapper {
    /**
     * Map dto to model
     *
     * @param staffAssignmentDto
     * @return StaffAssignment
     */
    @Mapping(source = "id", target="_id")
    @Mapping(target = "staff", ignore=true)
    @Mapping(target = "operation", ignore=true)
    StaffAssignment dtoToModel(StaffAssignmentDto staffAssignmentDto);

    /**
     * Map StaffAssignment to StaffAssignmentDto
     *
     * @param staffAssignment
     * @return StaffAssignmentDto
     */
    @Mapping(source = "_id", target="id")
    StaffAssignmentDto modelToDto(StaffAssignment staffAssignment);
}
