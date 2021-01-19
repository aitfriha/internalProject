package com.wproducts.administration.service;

import com.wproducts.administration.dto.mapper.DepartmentMapper;
import com.wproducts.administration.dto.model.DepartmentDto;
import com.wproducts.administration.model.Department;
import com.wproducts.administration.repository.DepartmentRepository;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper = Mappers.getMapper(DepartmentMapper.class);

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * Register a new Department
     *
     * @param departmentDto - departmentDto
     */
    @Override
    public void save(DepartmentDto departmentDto) {

        departmentDto.setDepartmentCode(departmentDto.getDepartmentCode().toLowerCase());

        Optional<Department> department = Optional.ofNullable(departmentRepository.findByDepartmentCode(departmentDto.getDepartmentCode()));
        if (departmentDto.getDepartmentCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        if (department.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }

        Department department1 = departmentMapper.dtoToModel(departmentDto)
                  .setDepartmentCreatedAt(Instant.now());
        departmentRepository.save(department1);
    }

    /**
     * Update Department
     *
     * @param departmentDto - departmentDto
     */
    @Override
    public void updateDepartment(DepartmentDto departmentDto) {

        departmentDto.setDepartmentCode(departmentDto.getDepartmentCode().toLowerCase());

        Optional<Department> department = Optional.ofNullable(departmentRepository.findDepartmentBy_id(departmentDto.getDepartmentId()));
        Optional<Department> departmentCode = Optional.ofNullable(departmentRepository.findByDepartmentCode(departmentDto.getDepartmentCode().toLowerCase()));

        if (!department.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }

        if (departmentCode.isPresent() && !(department.get().getDepartmentCode().equals(departmentDto.getDepartmentCode().toLowerCase())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
        if (departmentDto.getDepartmentCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }

        Department department1 = departmentMapper.dtoToModel(departmentDto)
                .setDepartmentUpdatedAt(Instant.now())
                .setDepartmentCreatedAt(department.get().getDepartmentCreatedAt());
        // Update department in database
        departmentRepository.save(department1);

    }

    /**
     * delete Department
     *
     * @param id - id
     */
    @Override
    public void removeDepartment(String id) {
        Optional<Department> department = Optional.ofNullable(departmentRepository.findDepartmentBy_id(id));
        // If department doesn't exists
        if (!department.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        departmentRepository.deleteById(id);
    }

    /**
     * all DepartmentsDto
     *
     * @return List DepartmentsDto
     */
    @Override
    public List<DepartmentDto> getAllDepartments() {
        // Get all departments
        List<Department> departments = departmentRepository.findAll();

        // Create a list of all departments dto
        ArrayList<DepartmentDto> departmentDtos = new ArrayList<>();

        for (Department department : departments) {
            DepartmentDto departmentDto = departmentMapper.modelToDto(department);
            departmentDto.setDepartmentCode(departmentDto.getDepartmentCode().toUpperCase());

            if(department.getDepartmentUpdatedAt() !=null ) {
                departmentDto.setDepartmentUpdatedAt(department.getDepartmentUpdatedAt().toString());
            }
            if(department.getDepartmentCreatedAt()!=null ) {
                departmentDto.setDepartmentCreatedAt(department.getDepartmentCreatedAt().toString());
            }

            departmentDtos.add(departmentDto);
        }
        return departmentDtos;
    }

    /**
     * one DepartmentDto
     *
     * @param id - id
     * @return DepartmentDto
     */
    @Override
    public DepartmentDto getOneDepartment(String id) {

        Optional<Department> department = Optional.ofNullable(departmentRepository.findDepartmentBy_id(id));

        // If department doesn't exists
        if (!department.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        DepartmentDto departmentDto = departmentMapper.modelToDto(department.get());
        departmentDto.setDepartmentCode(departmentDto.getDepartmentCode().toUpperCase());

        if(department.get().getDepartmentUpdatedAt() !=null ) {
            departmentDto.setDepartmentUpdatedAt(department.get().getDepartmentUpdatedAt().toString());
        }
        if(department.get().getDepartmentCreatedAt()!=null ) {
            departmentDto.setDepartmentCreatedAt(department.get().getDepartmentCreatedAt().toString());
        }

        return departmentDto;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Department, exceptionType, args);
    }
}
