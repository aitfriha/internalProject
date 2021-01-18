package com.wproducts.administration.service;

import com.wproducts.administration.dto.model.DepartmentDto;

import java.util.List;

public interface DepartmentService {

    /**
     * Register a new Department
     *
     * @param departmentDto - departmentDto
     */
    void save(DepartmentDto departmentDto);

    /**
     * Update Department
     *
     * @param departmentDto - departmentDto
     */
    void updateDepartment(DepartmentDto departmentDto);

    /**
     * delete Department
     *
     * @param id - id
     */
    void removeDepartment(String id);

    /**
     * all DepartmentsDto
     *
     * @return List DepartmentsDto
     */
    List<DepartmentDto> getAllDepartments();

    /**
     * one DepartmentDto
     *
     * @param id - id
     * @return DepartmentDto
     */
    DepartmentDto getOneDepartment(String id);
}
