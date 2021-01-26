package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.PersonTypeAddRequest;
import org.techniu.isbackend.controller.request.PersonTypeUpdateRequest;
import org.techniu.isbackend.controller.request.RequestStatusAddRequest;
import org.techniu.isbackend.controller.request.RequestStatusUpdateRequest;
import org.techniu.isbackend.dto.mapper.PersonTypeMapper;
import org.techniu.isbackend.dto.mapper.RequestStatusMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.PersonTypeService;
import org.techniu.isbackend.service.RequestStatusService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.PersonType;
import static org.techniu.isbackend.exception.EntityType.RequestStatus;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/requestStatus")
@CrossOrigin("*")
public class RequestStatusController {

    private final RequestStatusService requestStatusService;
    private final MapValidationErrorService mapValidationErrorService;

    private final RequestStatusMapper requestStatusMapper = Mappers.getMapper(RequestStatusMapper.class);

    public RequestStatusController(RequestStatusService requestStatusService, MapValidationErrorService mapValidationErrorService) {
        this.requestStatusService = requestStatusService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllRequestStatus() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                requestStatusService.getAllRequestStatus()), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity addRequestStatus(@RequestBody @Valid RequestStatusAddRequest requestStatusAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        requestStatusService.saveRequestStatus(requestStatusMapper.addRequestToDto(requestStatusAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(RequestStatus, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity updateRequestStatus(@RequestBody @Valid RequestStatusUpdateRequest requestStatusUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        requestStatusService.updateRequestStatus(requestStatusMapper.updateRequestToDto(requestStatusUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(RequestStatus, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteRequestStatus(@PathVariable String id) {
        requestStatusService.deleteRequestStatus(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(RequestStatus, DELETED)), HttpStatus.OK);
    }


}
