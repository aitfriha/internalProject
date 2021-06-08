package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.ActionHistoryAddrequest;
import org.techniu.isbackend.controller.request.ActionHistoryUpdaterequest;
import org.techniu.isbackend.dto.mapper.ActionHistoryMapper;
import org.techniu.isbackend.dto.model.ActionHistoryDto;
import org.techniu.isbackend.entity.ActionHistory;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.ActionHistoryService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.ActionHistory;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/actionHistory")
@CrossOrigin("*")
public class ActionHistoryController {

    private ActionHistoryService actionHistoryService;
    private final MapValidationErrorService mapValidationErrorService;
    private final ActionHistoryMapper actionHistoryMapper = Mappers.getMapper(ActionHistoryMapper.class);


    public ActionHistoryController(ActionHistoryService actionHistoryService, MapValidationErrorService mapValidationErrorService) {
        this.actionHistoryService = actionHistoryService;
        this.mapValidationErrorService = mapValidationErrorService;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid ActionHistoryAddrequest actionHistoryAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save action history

        actionHistoryAddrequest.setActionDate(new Date());

        System.out.println(actionHistoryAddrequest);

        actionHistoryService.saveActionHistory(actionHistoryMapper.addRequestToDto(actionHistoryAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(ActionHistory, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<ActionHistoryDto> getAllTypeOfCurrencies() {
        return actionHistoryService.getAllActionHistory();

    }
    /*
    @PostMapping("/row/{Id}")
    public ActionHistory getActionHistoryById(@PathVariable String Id) {
        return actionHistoryService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<ActionHistoryDto> deleteActionHistoryById(@PathVariable String Id) {
        return actionHistoryService.remove(Id);

    }

    @PostMapping("/update")
    public List<ActionHistoryDto> update(@RequestBody @Valid ActionHistoryUpdaterequest actionHistoryUpdaterequest) {
        // Save Contract Status
        String Id = actionHistoryUpdaterequest.getActionHistoryId();
        actionHistoryService.updateActionHistory(actionHistoryMapper.updateRequestToDto(actionHistoryUpdaterequest), Id);
        return actionHistoryService.getAllActionHistory();
    }

     */

}
