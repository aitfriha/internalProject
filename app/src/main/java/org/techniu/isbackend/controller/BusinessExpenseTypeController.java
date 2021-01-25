package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.BusinessExpensesSubtypeAddRequest;
import org.techniu.isbackend.controller.request.BusinessExpensesSubtypeUpdateRequest;
import org.techniu.isbackend.controller.request.BusinessExpensesTypeAddRequest;
import org.techniu.isbackend.controller.request.BusinessExpensesTypeUpdateRequest;
import org.techniu.isbackend.dto.mapper.BusinessExpenseSubtypeMapper;
import org.techniu.isbackend.dto.mapper.BusinessExpenseTypeMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.BusinessExpensesTypesService;

import javax.validation.Valid;
import java.util.HashMap;

import static org.techniu.isbackend.exception.EntityType.BusinessExpenseSubtype;
import static org.techniu.isbackend.exception.EntityType.BusinessExpenseType;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/businessExpenseTypes")
@CrossOrigin("*")
public class BusinessExpenseTypeController {

    private final BusinessExpensesTypesService businessExpensesTypesService;
    private final MapValidationErrorService mapValidationErrorService;
    private final BusinessExpenseTypeMapper businessExpenseTypeMapper = Mappers.getMapper(BusinessExpenseTypeMapper.class);
    private final BusinessExpenseSubtypeMapper businessExpenseSubtypeMapper = Mappers.getMapper(BusinessExpenseSubtypeMapper.class);


    public BusinessExpenseTypeController(BusinessExpensesTypesService businessExpensesTypesService, MapValidationErrorService mapValidationErrorService) {
        this.businessExpensesTypesService = businessExpensesTypesService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllTypes() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                businessExpensesTypesService.getAllTypes()), HttpStatus.OK);
    }

    @PostMapping(value = "/addType")
    public ResponseEntity addBusinessExpenseType(@RequestBody @Valid BusinessExpensesTypeAddRequest businessExpensesTypeAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        businessExpensesTypesService.saveType(businessExpenseTypeMapper.addRequestToDto(businessExpensesTypeAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(BusinessExpenseType, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/updateType")
    public ResponseEntity updateType(@RequestBody @Valid BusinessExpensesTypeUpdateRequest businessExpensesTypeUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        businessExpensesTypesService.updateType(businessExpenseTypeMapper.updateRequestToDto(businessExpensesTypeUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(BusinessExpenseType, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteType/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteType(@PathVariable String id) {
        businessExpensesTypesService.deleteType(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(BusinessExpenseType, DELETED)), HttpStatus.OK);
    }


    @PostMapping(value = "/addSubtype")
    public ResponseEntity addSubtype(@RequestBody @Valid BusinessExpensesSubtypeAddRequest businessExpensesSubtypeAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        businessExpensesTypesService.saveSubtype(businessExpenseSubtypeMapper.addRequestToDto(businessExpensesSubtypeAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(BusinessExpenseSubtype, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/updateSubtype")
    public ResponseEntity updateBusinessExpenseSubtype(@RequestBody @Valid BusinessExpensesSubtypeUpdateRequest businessExpensesSubtypeUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        businessExpensesTypesService.updateSubtype(businessExpenseSubtypeMapper.updateRequestToDto(businessExpensesSubtypeUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(BusinessExpenseSubtype, UPDATED)), HttpStatus.OK);
    }

    @PostMapping(value = "/deleteSubtype")
    public ResponseEntity deleteBusinessExpenseSubtype(@RequestBody HashMap data) {
        businessExpensesTypesService.deleteSubtype(data);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(BusinessExpenseSubtype, DELETED)), HttpStatus.OK);
    }
}
