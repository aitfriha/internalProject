package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.StaffExpensesSubtypeAddRequest;
import org.techniu.isbackend.controller.request.StaffExpensesSubtypeUpdateRequest;
import org.techniu.isbackend.controller.request.StaffExpensesTypeAddRequest;
import org.techniu.isbackend.controller.request.StaffExpensesTypeUpdateRequest;
import org.techniu.isbackend.dto.mapper.StaffExpenseSubtypeMapper;
import org.techniu.isbackend.dto.mapper.StaffExpenseTypeMapper;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.StaffExpensesTypesService;

import javax.validation.Valid;
import java.util.HashMap;

import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/staffExpenseTypes")
@CrossOrigin("*")
public class StaffExpensesTypeController {

    private final StaffExpensesTypesService staffExpensesTypesService;
    private final MapValidationErrorService mapValidationErrorService;

    private final StaffExpenseTypeMapper staffExpenseTypeMapper = Mappers.getMapper(StaffExpenseTypeMapper.class);
    private final StaffExpenseSubtypeMapper staffExpenseSubtypeMapper = Mappers.getMapper(StaffExpenseSubtypeMapper.class);

    public StaffExpensesTypeController(StaffExpensesTypesService staffExpensesTypesService, MapValidationErrorService mapValidationErrorService) {
        this.staffExpensesTypesService = staffExpensesTypesService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllStaffExpensesTypes() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                staffExpensesTypesService.getAllTypes()), HttpStatus.OK);
    }

    @PostMapping(value = "/addType")
    public ResponseEntity addType(@RequestBody @Valid StaffExpensesTypeAddRequest staffExpensesTypeAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        staffExpensesTypesService.saveType(staffExpenseTypeMapper.addRequestToDto(staffExpensesTypeAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.StaffExpenseType, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/updateType")
    public ResponseEntity updateType(@RequestBody @Valid StaffExpensesTypeUpdateRequest staffExpensesTypeUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        staffExpensesTypesService.updateType(staffExpenseTypeMapper.updateRequestToDto(staffExpensesTypeUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.StaffExpenseType, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteType/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteType(@PathVariable String id) {
        staffExpensesTypesService.deleteType(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.StaffExpenseType, DELETED)), HttpStatus.OK);
    }


    @PostMapping(value = "/addSubtype")
    public ResponseEntity addSubtype(@RequestBody @Valid StaffExpensesSubtypeAddRequest staffExpensesSubtypeAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        staffExpensesTypesService.saveSubtype(staffExpenseSubtypeMapper.addRequestToDto(staffExpensesSubtypeAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.StaffExpenseSubtype, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/updateSubtype")
    public ResponseEntity updateSubtype(@RequestBody @Valid StaffExpensesSubtypeUpdateRequest staffExpensesSubtypeUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        staffExpensesTypesService.updateSubtype(staffExpenseSubtypeMapper.updateRequestToDto(staffExpensesSubtypeUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.StaffExpenseSubtype, UPDATED)), HttpStatus.OK);
    }

    @PostMapping(value = "/deleteSubtype")
    public ResponseEntity deleteSubtype(@RequestBody HashMap data) {
        staffExpensesTypesService.deleteSubtype(data);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.StaffExpenseSubtype, DELETED)), HttpStatus.OK);
    }

}
