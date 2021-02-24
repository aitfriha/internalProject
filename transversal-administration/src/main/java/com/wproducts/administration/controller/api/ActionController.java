package com.wproducts.administration.controller.api;

import com.wproducts.administration.controller.request.ActionAddRequest;
import com.wproducts.administration.controller.request.ActionUpdateRequest;
import com.wproducts.administration.dto.mapper.ActionMapper;
import com.wproducts.administration.service.ActionService;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.Response;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.Action;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.ExceptionType.UPDATED;
import static org.techniu.isbackend.exception.ExceptionType.DELETED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/action")
@CrossOrigin(origins = { "http://localhost:3001" })
public class ActionController {

    private final ActionService actionService;
    private final ActionMapper actionMapper = Mappers.getMapper(ActionMapper.class);
    private final MapValidationErrorService mapValidationErrorService;

    public ActionController(ActionService actionService, MapValidationErrorService mapValidationErrorService) {
        this.actionService = actionService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    /**
     * Handles the incoming POST API "/action/add"
     *
     * @param actionAddRequest Action Add request
     * @return ActionDto
     */
    @PostMapping("/add")
    public ResponseEntity signup(@RequestBody @Valid ActionAddRequest actionAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        System.out.println(actionAddRequest);
        actionService.save(actionMapper.addRequestToDto(actionAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Action, ADDED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/action/update"
     *
     * @param actionUpdateRequest Action update
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid ActionUpdateRequest actionUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        actionService.updateAction(actionMapper.updateRequestToDto(actionUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(Action, UPDATED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming DELETE API "/action/delete"
     *
     * @param id action delete request
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        actionService.removeAction(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Action, DELETED)), HttpStatus.OK);
    }

    /**
     * display all objects GET API "/service/all"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllActions() {
        return new ResponseEntity<Response>(Response.ok().setPayload(actionService.getAllActions()), HttpStatus.OK);
    }

    /**
     * display an object GET API "/action/id"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getOneAction(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(actionService.getOneAction(id)), HttpStatus.OK);
    }


}
