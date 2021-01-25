package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.ExpenseStatusAddRequest;
import org.techniu.isbackend.controller.request.ExpenseStatusUpdateRequest;
import org.techniu.isbackend.dto.mapper.ExpenseStatusMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.ExpenseStatusService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.ExpenseStatus;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/expenseStatus")
@CrossOrigin("*")
public class ExpenseStatusController {

    private final ExpenseStatusService expenseStatusService;
    private final MapValidationErrorService mapValidationErrorService;
    private final ExpenseStatusMapper expenseStatusMapper = Mappers.getMapper(ExpenseStatusMapper.class);

    public ExpenseStatusController(ExpenseStatusService expenseStatusService, MapValidationErrorService mapValidationErrorService) {
        this.expenseStatusService = expenseStatusService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllExpenseStatus() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                expenseStatusService.getAllExpenseStatus()), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity addExpenseStatus(@RequestBody @Valid ExpenseStatusAddRequest expenseStatusAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        expenseStatusService.saveExpenseStatus(expenseStatusMapper.addRequestToDto(expenseStatusAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(ExpenseStatus, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity updateExpenseStatus(@RequestBody @Valid ExpenseStatusUpdateRequest expenseStatusUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        expenseStatusService.updateExpenseStatus(expenseStatusMapper.updateRequestToDto(expenseStatusUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(ExpenseStatus, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteExpenseStatus(@PathVariable String id) {
        expenseStatusService.deleteExpenseStatus(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(ExpenseStatus, DELETED)), HttpStatus.OK);
    }


}
