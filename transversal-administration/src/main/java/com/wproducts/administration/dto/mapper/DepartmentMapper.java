package com.wproducts.administration.dto.mapper;

import com.wproducts.administration.controller.request.DepartmentAddRequest;
import com.wproducts.administration.controller.request.DepartmentUpdateRequest;
import com.wproducts.administration.dto.model.DepartmentDto;
import com.wproducts.administration.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    /**
     * Map dto to model
     *
     * @param departmentDto departmentDto
     * @return Department
     */
    @Mapping(source = "departmentId", target="_id")
    Department dtoToModel(DepartmentDto departmentDto);

    /**
     * Map model to dto
     *
     * @param department department
     * @return departmentDto
     */
    @Mapping(source = "_id", target="departmentId")
    @Mapping(target = "departmentCreatedAt", ignore=true)
    @Mapping(target = "departmentUpdatedAt", ignore=true)
    DepartmentDto modelToDto(Department department);

    /**
     * Map add request to dto
     *
     * @param departmentAddRequest departmentAddRequest
     * @return departmentDto
     */
    DepartmentDto addRequestToDto(DepartmentAddRequest departmentAddRequest);

    /**
     * Map update request to dto
     *
     * @param DepartmentUpdateRequest DepartmentUpdateRequest
     * @return departmentDto
     */
     DepartmentDto updateRequestToDto(DepartmentUpdateRequest DepartmentUpdateRequest);
}
