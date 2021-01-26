package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.AssignmentTypeAddRequest;
import org.techniu.isbackend.controller.request.AssignmentTypeUpdateRequest;
import org.techniu.isbackend.dto.mapper.AssignmentTypeMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.AssignmentTypeService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.AssignmentType;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/assignmentType")
@CrossOrigin("*")
public class AssignmentTypeController {

    private final AssignmentTypeService assignmentTypeService;
    private final MapValidationErrorService mapValidationErrorService;
    private final AssignmentTypeMapper assignmentTypeMapper = Mappers.getMapper(AssignmentTypeMapper.class);

    public AssignmentTypeController(AssignmentTypeService assignmentTypeService, MapValidationErrorService mapValidationErrorService) {
        this.assignmentTypeService = assignmentTypeService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllAssignmentTypes() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                assignmentTypeService.getAllAssignmentTypes()), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity addAssignmentType(@RequestBody @Valid AssignmentTypeAddRequest assignmentTypeAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        assignmentTypeService.saveAssignmentType(assignmentTypeMapper.addRequestToDto(assignmentTypeAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(AssignmentType, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity updateAssignmentType(@RequestBody @Valid AssignmentTypeUpdateRequest assignmentTypeUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        assignmentTypeService.updateAssignmentType(assignmentTypeMapper.updateRequestToDto(assignmentTypeUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(AssignmentType, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteAssignmentType(@PathVariable String id) {
        assignmentTypeService.deleteAssignmentType(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(AssignmentType, DELETED)), HttpStatus.OK);
    }
}
