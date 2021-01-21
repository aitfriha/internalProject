package com.wproducts.administration.controller.api;

import com.wproducts.administration.controller.request.DepartmentAddRequest;
import com.wproducts.administration.controller.request.DepartmentUpdateRequest;
import com.wproducts.administration.dto.mapper.DepartmentMapper;
import com.wproducts.administration.service.DepartmentService;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.Response;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.Department;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/department")
@CrossOrigin(origins = { "http://localhost:3001" })
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper = Mappers.getMapper(DepartmentMapper.class);
    private final MapValidationErrorService mapValidationErrorService;

    public DepartmentController(DepartmentService departmentService, MapValidationErrorService mapValidationErrorService) {
        this.departmentService = departmentService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    /**
     * Handles the incoming POST API "/department/add"
     *
     * @param departmentAddRequest Department Add request
     * @return DepartmentDto
     */
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid DepartmentAddRequest departmentAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Create DepartmentDto from departmentAddRequest and save
        departmentService.save(departmentMapper.addRequestToDto(departmentAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.Department, ADDED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/department/update"
     *
     * @param departmentUpdateRequest department update request
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid DepartmentUpdateRequest departmentUpdateRequest, BindingResult bindingResult) {
        // Handle validation errors
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);

        // Update department
        departmentService.updateDepartment(departmentMapper.updateRequestToDto(departmentUpdateRequest));

        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(Department, UPDATED)), HttpStatus.OK);
    }
    /**
     * Handles the incoming DELETE API "/department/delete"
     *
     * @param id service delete request
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        departmentService.removeDepartment(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Department, DELETED)), HttpStatus.OK);
    }

    /**
     * display all objects GET API "/department/all"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllDepartments() {
        return new ResponseEntity<Response>(Response.ok().setPayload(departmentService.getAllDepartments()), HttpStatus.OK);
    }

    /**
     * display an object GET API "/department/id"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getOneDepartment(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(departmentService.getOneDepartment(id)), HttpStatus.OK);
    }

}
