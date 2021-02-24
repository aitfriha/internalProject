package org.techniu.isbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.DocumentManagerConfigService;

import java.util.HashMap;

import static org.techniu.isbackend.exception.EntityType.DocumentManagerConfig;
import static org.techniu.isbackend.exception.ExceptionType.UPDATED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/documentManagerConfig")
@CrossOrigin("*")
public class DocumentManagerConfigController {

    private final DocumentManagerConfigService documentManagerConfigService;
    private final MapValidationErrorService mapValidationErrorService;
    //private final DocumentManagerConfig documentManagerConfig;


    public DocumentManagerConfigController(DocumentManagerConfigService documentManagerConfigService, MapValidationErrorService mapValidationErrorService) {
        this.documentManagerConfigService = documentManagerConfigService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getDocumentManagerConfig() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                documentManagerConfigService.getConfiguration()), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity updateDocumentManagerConfig(@RequestBody HashMap data) {
        documentManagerConfigService.updateConfiguration(data);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(DocumentManagerConfig, UPDATED)), HttpStatus.OK);
    }

}
