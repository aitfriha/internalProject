package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.ExpenseEmailAddressAddRequest;
import org.techniu.isbackend.controller.request.ExpenseEmailAddressUpdateRequest;
import org.techniu.isbackend.dto.mapper.ExpenseEmailAddressMapper;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.ExpenseEmailAddressService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/expenseEmailAddress")
@CrossOrigin("*")
public class ExpenseEmailAddressController {

    private final ExpenseEmailAddressService expenseEmailAddressService;
    private final MapValidationErrorService mapValidationErrorService;

    private final ExpenseEmailAddressMapper expenseEmailAddressMapper = Mappers.getMapper(ExpenseEmailAddressMapper.class);

    public ExpenseEmailAddressController(ExpenseEmailAddressService expenseEmailAddressService, MapValidationErrorService mapValidationErrorService) {
        this.expenseEmailAddressService = expenseEmailAddressService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllEmailAddresses() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                expenseEmailAddressService.getAllEmailAddresses()), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity addEmailAddress(@RequestBody @Valid ExpenseEmailAddressAddRequest expenseEmailAddressAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        expenseEmailAddressService.saveEmailAddress(expenseEmailAddressMapper.addRequestToDto(expenseEmailAddressAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.VoucherType, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity updateEmailAddresses(@RequestBody @Valid ExpenseEmailAddressUpdateRequest expenseEmailAddressUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        expenseEmailAddressService.updateEmailAddress(expenseEmailAddressMapper.updateRequestToDto(expenseEmailAddressUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.VoucherType, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteEmailAddresses(@PathVariable String id) {
        expenseEmailAddressService.deleteEmailAddress(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.VoucherType, DELETED)), HttpStatus.OK);
    }


}
