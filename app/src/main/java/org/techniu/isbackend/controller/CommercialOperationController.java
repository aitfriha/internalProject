package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.CommercialOperationAddrequest;
import org.techniu.isbackend.controller.request.CommercialOperationUpdaterequest;
import org.techniu.isbackend.dto.mapper.CommercialOperationMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.CommercialOperationService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.CommercialOperation;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/commercialOperation")
@CrossOrigin("*")
public class CommercialOperationController {
    private final CommercialOperationService commercialOperationService;
    private final MapValidationErrorService mapValidationErrorService;
    private final CommercialOperationMapper commercialOperationMapper = Mappers.getMapper(CommercialOperationMapper.class);
    public CommercialOperationController(CommercialOperationService commercialOperationService, MapValidationErrorService mapValidationErrorService) {
        this.commercialOperationService = commercialOperationService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid CommercialOperationAddrequest commercialOperationAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save CommercialOperation
        commercialOperationService.save(commercialOperationMapper.addRequestToDto(commercialOperationAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(CommercialOperation, ADDED)), HttpStatus.OK);
    }
    /**
     * Handles the incoming POST API "/commercialOperation/update"
     *
     * @param commercialOperationUpdaterequest Action update
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid CommercialOperationUpdaterequest commercialOperationUpdaterequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        commercialOperationService.update(commercialOperationMapper.updateRequestToDto(commercialOperationUpdaterequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(CommercialOperation, UPDATED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming DELETE API "/commercialOperation/delete"
     *
     * @param id action delete request
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        commercialOperationService.remove(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(CommercialOperation, DELETED)), HttpStatus.OK);
    }

    /**
     * display all commercialOperation GET API "/api/commercialOperation"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity allCommercialOperation() {
        return new ResponseEntity<Response>(Response.ok().setPayload(commercialOperationService.getAll()), HttpStatus.OK);
    }

    /**
     * display all commercialOperation GET API "/api/commercialOperation"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all2")
    public ResponseEntity allCommercialOperation2() {
        return new ResponseEntity<Response>(Response.ok().setPayload(commercialOperationService.getAll2()), HttpStatus.OK);
    }
}
