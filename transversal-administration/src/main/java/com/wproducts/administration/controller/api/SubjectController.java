package com.wproducts.administration.controller.api;

import com.wproducts.administration.controller.request.SubjectAddRequest;
import com.wproducts.administration.controller.request.SubjectUpdateRequest;
import com.wproducts.administration.dto.mapper.SubjectMapper;
import com.wproducts.administration.service.SubjectService;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.Response;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.Subject;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/subject")
@CrossOrigin(origins = { "http://localhost:3001" })
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper = Mappers.getMapper(SubjectMapper.class);
    private final MapValidationErrorService mapValidationErrorService;

    public SubjectController(SubjectService subjectService, MapValidationErrorService mapValidationErrorService) {
        this.subjectService = subjectService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    /**
     * Handles the incoming POST API "/subject/add"
     *
     * @param subjectAddRequest Subject Add request
     * @return SubjectDto
     */
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid SubjectAddRequest subjectAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Create SubjectDto from subjectAddRequest and save
        subjectService.save(subjectAddRequest);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.Subject, ADDED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/subject/update"
     *
     * @param subjectUpdateRequest subject update request
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid SubjectUpdateRequest subjectUpdateRequest, BindingResult bindingResult) {
        // Handle validation errors
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);

        // Update subject
        subjectService.updateSubject(subjectMapper.updateRequestToDto(subjectUpdateRequest));

        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(Subject, UPDATED)), HttpStatus.OK);
    }
    /**
     * Handles the incoming DELETE API "/subject/delete"
     *
     * @param id service delete request
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        subjectService.removeSubject(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Subject, DELETED)), HttpStatus.OK);
    }

    /**
     * display all objects GET API "/subject/all"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllSubjects() {
        return new ResponseEntity<Response>(Response.ok().setPayload(subjectService.getAllSubjects()), HttpStatus.OK);
    }

    /**
     * display an object GET API "/subject/id"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getOneSubject(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(subjectService.getOneSubject(id)), HttpStatus.OK);
    }

}
