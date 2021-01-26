package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.PersonTypeAddRequest;
import org.techniu.isbackend.controller.request.PersonTypeUpdateRequest;
import org.techniu.isbackend.dto.mapper.PersonTypeMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.PersonTypeService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.PersonType;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/personType")
@CrossOrigin("*")
public class PersonTypeController {

    private final PersonTypeService personTypeService;
    private final MapValidationErrorService mapValidationErrorService;

    private final PersonTypeMapper personTypeMapper = Mappers.getMapper(PersonTypeMapper.class);

    public PersonTypeController(PersonTypeService personTypeService, MapValidationErrorService mapValidationErrorService) {
        this.personTypeService = personTypeService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllPersonTypes() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                personTypeService.getAllPersonTypes()), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity addPersonType(@RequestBody @Valid PersonTypeAddRequest personTypeAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        personTypeService.savePersonType(personTypeMapper.addRequestToDto(personTypeAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(PersonType, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity updatePersonType(@RequestBody @Valid PersonTypeUpdateRequest personTypeUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        personTypeService.updatePersonType(personTypeMapper.updateRequestToDto(personTypeUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(PersonType, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deletePersonType(@PathVariable String id) {
        personTypeService.deletePersonType(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(PersonType, DELETED)), HttpStatus.OK);
    }


}
