package com.internal.translation.controller.api;

import com.internal.translation.controller.request.TranslateSentenceAddListRequest;
import com.internal.translation.controller.request.TranslateSentenceAddRequest;
import com.internal.translation.controller.request.TranslateSentenceUpdateRequest;
import com.internal.translation.dto.model.TranslateSentenceDto;
import com.internal.translation.model.TranslateSentence;
import com.internal.translation.service.TranslateSentenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/translation/translateSentence")
@CrossOrigin("*")
public class TranslateSentenceController {

    private final TranslateSentenceService translateSentenceService;

    private final MapValidationErrorService mapValidationErrorService;

    public TranslateSentenceController(TranslateSentenceService translateSentenceService , MapValidationErrorService mapValidationErrorService) {
        this.translateSentenceService = translateSentenceService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    /**
     * Handles the incoming POST API "/translateSentence/add"
     *
     * @param translateSentenceAddRequest translateSentence Add request
     * @return translateSentenceDto
     */
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid TranslateSentenceAddRequest translateSentenceAddRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);

        // Create translateSentenceDto from translateSentenceAddRequest
        TranslateSentenceDto translateSentenceDto = new TranslateSentenceDto()
                .setCountryLanguageCode(translateSentenceAddRequest.getCountryLanguageCode())
                .setTranslation(translateSentenceAddRequest.getTranslation())
                .setDefaultSentenceCode(translateSentenceAddRequest.getDefaultSentenceCode());

        // Save TranslateSentence
        translateSentenceService.save(translateSentenceDto);

        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.TranslateSentence, ADDED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/translateSentence/update"
     *
     * @param translateSentenceUpdateRequest TranslateSentence update request
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid TranslateSentenceUpdateRequest translateSentenceUpdateRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);

        // Create TranslateSentenceDto from translateSentenceUpdateRequest
        TranslateSentenceDto translateSentenceDto = new TranslateSentenceDto()
                .setTranslateSentenceId(translateSentenceUpdateRequest.getTranslateSentenceId())
                .setDefaultSentenceCode(translateSentenceUpdateRequest.getDefaultSentenceCode())
                .setCountryLanguageCode(translateSentenceUpdateRequest.getCountryLanguageCode())
                .setTranslation(translateSentenceUpdateRequest.getTranslation());

        // Update TranslateSentence
        translateSentenceService.updateTranslateSentence(translateSentenceDto);

        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.TranslateSentence, UPDATED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/translateSentence/delete"
     *
     * @param id translateSentence delete request
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        translateSentenceService.removeParam(id);

        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.TranslateSentence, DELETED)), HttpStatus.OK);
    }


    /**
     * display all objects GET API "/ translateSentence/all"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity allTranslateSentence() {
        return new ResponseEntity<Response>(Response.ok().setPayload(translateSentenceService.getAllTranslateSentence()), HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/translateSentence/addlist"
     *
     * @param translateSentenceAddListRequest translateSentence Add List Request
     * @return defaultSentenceDto
     */
    @PostMapping("/addList")
    public ResponseEntity addList(@RequestBody @Valid TranslateSentenceAddListRequest translateSentenceAddListRequest) {

        TranslateSentenceDto translateSentenceDto = new TranslateSentenceDto().setTranslateSentencesList(translateSentenceAddListRequest.getTranslateSentencesList())
                .setCountryLanguageCode(translateSentenceAddListRequest.getCountryLanguageCode());

        translateSentenceService.addTranslateSentencesList(translateSentenceDto);

        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.TranslateSentence, ADDED)), HttpStatus.OK);
    }

    /**
     * display all objects GET API "/ translateSentence/translateSentenceByCountryLanguage"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/translateSentenceByCountryLanguageCode/{countryLanguage}")
    public ResponseEntity allTranslateSentencesByCountryLanguage(@PathVariable String countryLanguage) {
        return new ResponseEntity<Response>(Response.ok().setPayload(translateSentenceService.getAllTranslateSentenceByCountryLanguage(countryLanguage)), HttpStatus.OK);
    }

    /**
     * display an object GET API "/translateSentence/id"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity oneTranslateSentence(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(translateSentenceService.getOneTranslateSentence(id)), HttpStatus.OK);
    }

    /**
     * display an object GET API "/translateSentence/id"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/distinctTranslateSentenceCountryLanguages")
    public ResponseEntity getDistinctTranslateSentencesCountryLanguages() {
        return new ResponseEntity<Response>(Response.ok().setPayload(translateSentenceService.getDistinctTranslateSentenceCountryLanguages()), HttpStatus.OK);
    }

}
