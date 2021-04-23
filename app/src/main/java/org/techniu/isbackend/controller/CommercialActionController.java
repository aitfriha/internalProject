package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.CommercialActionAddrequest;
import org.techniu.isbackend.controller.request.CommercialActionUpdaterequest;
import org.techniu.isbackend.dto.mapper.CommercialActionMapper;
import org.techniu.isbackend.dto.model.CommercialActionTypeDto;
import org.techniu.isbackend.entity.CommercialAction;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.CommercialActionService;

import javax.validation.Valid;

import java.util.List;

import static org.techniu.isbackend.exception.EntityType.CommercialAction;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/commercialAction")
@CrossOrigin("*")
public class CommercialActionController {
    private final CommercialActionService commercialActionService;
    private final MapValidationErrorService mapValidationErrorService;
    private final CommercialActionMapper commercialActionMapper = Mappers.getMapper(CommercialActionMapper.class);
    public CommercialActionController(CommercialActionService commercialActionService, MapValidationErrorService mapValidationErrorService) {
        this.commercialActionService = commercialActionService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid CommercialActionAddrequest commercialActionAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save CommercialAction
        commercialActionService.saveCommercialAction(commercialActionMapper.addRequestToDto(commercialActionAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(CommercialAction, ADDED)), HttpStatus.OK);
    }
    /**
     * Handles the incoming POST API "/commercialAction/update"
     *
     * @param commercialActionUpdaterequest Action update
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid CommercialActionUpdaterequest commercialActionUpdaterequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        String Id = commercialActionUpdaterequest.getCommercialActionId();
        commercialActionService.updateCommercialAction(commercialActionMapper.updateRequestToDto(commercialActionUpdaterequest), Id);
        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(CommercialAction, UPDATED)), HttpStatus.OK);
    }

    @PostMapping("/delete/{Id}")
    public List<org.techniu.isbackend.entity.CommercialAction> deleteCommercialAction(@PathVariable String Id) {
        System.out.println(Id);
        return commercialActionService.remove(Id);
    }

    /**
     * display all commercialAction GET API "/api/commercialAction"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity allCommercialAction() {
        return new ResponseEntity<Response>(Response.ok().setPayload(commercialActionService.getAllCommercialAction()), HttpStatus.OK);
    }

    /**
     * display all commercialAction GET API "/api/commercialAction"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all2")
    public ResponseEntity allCommercialAction2() {
        return new ResponseEntity<Response>(Response.ok().setPayload(commercialActionService.getAllCommercialAction2()), HttpStatus.OK);
    }

}
