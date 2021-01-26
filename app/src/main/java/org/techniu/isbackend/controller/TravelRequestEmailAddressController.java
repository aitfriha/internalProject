package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.TravelRequestEmailAddressAddRequest;
import org.techniu.isbackend.controller.request.TravelRequestEmailAddressUpdateRequest;
import org.techniu.isbackend.dto.mapper.TravelRequestEmailAddressMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.TravelRequestEmailAddressService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.EmailAddress;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/travelRequestEmailAddress")
@CrossOrigin("*")
public class TravelRequestEmailAddressController {

    private final TravelRequestEmailAddressService travelRequestEmailAddressService;
    private final MapValidationErrorService mapValidationErrorService;
    private final TravelRequestEmailAddressMapper travelRequestEmailAddressMapper = Mappers.getMapper(TravelRequestEmailAddressMapper.class);

    public TravelRequestEmailAddressController(TravelRequestEmailAddressService travelRequestEmailAddressService, MapValidationErrorService mapValidationErrorService) {
        this.travelRequestEmailAddressService = travelRequestEmailAddressService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllEmailAddresses() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                travelRequestEmailAddressService.getAllEmailAddresses()), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity addEmailAddress(@RequestBody @Valid TravelRequestEmailAddressAddRequest travelRequestEmailAddressAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        travelRequestEmailAddressService.saveEmailAddress(travelRequestEmailAddressMapper.addRequestToDto(travelRequestEmailAddressAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EmailAddress, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity updateEmailAddress(@RequestBody @Valid TravelRequestEmailAddressUpdateRequest travelRequestEmailAddressUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        travelRequestEmailAddressService.updateEmailAddress(travelRequestEmailAddressMapper.updateRequestToDto(travelRequestEmailAddressUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EmailAddress, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteEmailAddress(@PathVariable String id) {
        travelRequestEmailAddressService.deleteEmailAddress(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EmailAddress, DELETED)), HttpStatus.OK);
    }
}
