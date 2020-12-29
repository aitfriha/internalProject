package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.ContactByOperationAddRequest;
import org.techniu.isbackend.controller.request.ContactByOperationUpdateRequest;
import org.techniu.isbackend.dto.mapper.ContactByOperationMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.ContactByOperationService;

import javax.validation.Valid;

import java.util.List;

import static org.techniu.isbackend.exception.EntityType.ContactByOperation;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/contactByOperation")
@CrossOrigin("*")
public class ContactByOperationController {
    private final ContactByOperationService contactByOperationService;
    private final MapValidationErrorService mapValidationErrorService;
    private final ContactByOperationMapper contactByOperationMapper = Mappers.getMapper(ContactByOperationMapper.class);
    public ContactByOperationController(ContactByOperationService contactByOperationService, MapValidationErrorService mapValidationErrorService) {
        this.contactByOperationService = contactByOperationService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid List<ContactByOperationAddRequest> contactByOperationAddRequests, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save ContactByOperation
        for (ContactByOperationAddRequest contactByOperationAddRequest:contactByOperationAddRequests)
        {
            contactByOperationService.save(contactByOperationMapper.addRequestToDto(contactByOperationAddRequest));
        }
        //System.out.println(contactByOperationMapper.addRequestToDto(contactByOperationAddRequest));
        //contactByOperationService.save(contactByOperationMapper.addRequestToDto(contactByOperationAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(ContactByOperation, ADDED)), HttpStatus.OK);
    }
    /**
     * Handles the incoming POST API "/contactByOperation/update"
     *
     * @param contactByOperationUpdateRequest Action update
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid ContactByOperationUpdateRequest contactByOperationUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        contactByOperationService.update(contactByOperationMapper.updateRequestToDto(contactByOperationUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(ContactByOperation, UPDATED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming DELETE API "/contactByOperation/delete"
     *
     * @param statusId action delete request
     */
    @RequestMapping(value = "/delete/{statusId}/{contactTypeName}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String statusId,@PathVariable String contactTypeName) {
        contactByOperationService.remove(statusId,contactTypeName);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(ContactByOperation, DELETED)), HttpStatus.OK);
    }

    /**
     * display all contactByOperation GET API "/api/contactByOperation"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity allContactByOperation() {
        return new ResponseEntity<Response>(Response.ok().setPayload(contactByOperationService.getAll()), HttpStatus.OK);
    }

    /**
     * display all contactByOperation GET API "/api/contactByOperation/id"
     * @param id action delete request
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity contactByOperationById(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(contactByOperationService.getContactByOperationById(id)), HttpStatus.OK);
    }
}
