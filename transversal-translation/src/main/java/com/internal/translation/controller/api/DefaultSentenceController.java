package com.internal.translation.controller.api;

import com.internal.translation.controller.request.DefaultSentenceAddListRequest;
import com.internal.translation.controller.request.DefaultSentenceAddRequest;
import com.internal.translation.controller.request.DefaultSentenceUpdateRequest;
import com.internal.translation.dto.mapper.DefaultSentenceMapper;
import com.internal.translation.service.DefaultSentenceService;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.DefaultSentence;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/translation/defaultSentence")
@CrossOrigin("*")
public class DefaultSentenceController {

    private final DefaultSentenceService defaultSentenceService;
    private final MapValidationErrorService mapValidationErrorService;
    private final DefaultSentenceMapper defaultSentenceMapper = Mappers.getMapper(DefaultSentenceMapper.class);

    public DefaultSentenceController(DefaultSentenceService defaultSentenceService, MapValidationErrorService mapValidationErrorService) {
        this.defaultSentenceService = defaultSentenceService;
        this.mapValidationErrorService = mapValidationErrorService;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid DefaultSentenceAddRequest defaultSentenceAddRequest, BindingResult bindingResult) {

        // Handle validation errors
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);

        // Save defaultSentence
        defaultSentenceService.save(
                defaultSentenceMapper.addRequestToDto(defaultSentenceAddRequest));

        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(DefaultSentence, ADDED)), HttpStatus.OK);
    }

    @PostMapping("/addList")
    public ResponseEntity addList(@RequestBody @Valid DefaultSentenceAddListRequest defaultSentenceAddListRequest, BindingResult bindingResult) {

        // Handle validation errors
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);

        // Save defaultSentences list
        defaultSentenceService.addDefaultSentencesList(
                defaultSentenceMapper.addListRequestToDto(defaultSentenceAddListRequest));

        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(DefaultSentence, ADDED)), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid DefaultSentenceUpdateRequest defaultSentenceUpdateRequest, BindingResult bindingResult) {

        // Handle validation errors
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);

        // Update defaultSentence
        defaultSentenceService.updateDefaultSentence(defaultSentenceMapper.updateRequestToDto(defaultSentenceUpdateRequest));

        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(DefaultSentence, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {

        // Delete defaultSentence
        defaultSentenceService.deleteDefaultSentence(id);

        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(DefaultSentence, DELETED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllDefaultSentences() {

        return new ResponseEntity<Response>(Response.ok().setPayload(
                defaultSentenceService.getAllDefaultSentence()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getDefaultSentence(@PathVariable String id) {

        return new ResponseEntity<Response>(Response.ok().setPayload(
                defaultSentenceService.getDefaultSentence(id)), HttpStatus.OK);
    }

}
