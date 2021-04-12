package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.CommercialActionTypeAddrequest;
import org.techniu.isbackend.controller.request.CommercialActionTypeUpdaterequest;
import org.techniu.isbackend.dto.mapper.CommercialActionTypeMapper;
import org.techniu.isbackend.dto.model.CommercialActionTypeDto;
import org.techniu.isbackend.entity.CommercialActionType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.CommercialActionTypeService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.CommercialActionType;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/commercialActionType")
@CrossOrigin("*")
public class CommercialActionTypeController {

    private CommercialActionTypeService commercialActionTypeService;
    private final MapValidationErrorService mapValidationErrorService;
    private final CommercialActionTypeMapper commercialActionTypeMapper = Mappers.getMapper(CommercialActionTypeMapper.class);


    public CommercialActionTypeController(CommercialActionTypeService commercialActionTypeService, MapValidationErrorService mapValidationErrorService) {
        this.commercialActionTypeService = commercialActionTypeService;
        this.mapValidationErrorService = mapValidationErrorService;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid CommercialActionTypeAddrequest commercialActionTypeAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save Type Of Currency
        System.out.println(commercialActionTypeAddrequest);

        List <CommercialActionTypeDto> actionTypeList = commercialActionTypeService.getAllCommercialActionType();
        for (CommercialActionTypeDto actionTypeDto : actionTypeList) {
            if (actionTypeDto.getTypeName().equals(commercialActionTypeAddrequest.getTypeName())) return null;
        }


        commercialActionTypeService.saveCommercialActionType(commercialActionTypeMapper.addRequestToDto(commercialActionTypeAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(CommercialActionType, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<CommercialActionTypeDto> getAllCommercialActions() {
        return commercialActionTypeService.getAllCommercialActionType();

    }

    @PostMapping("/row/{Id}")
    public CommercialActionType getCommercialActionTypeById(@PathVariable String Id) {
        return commercialActionTypeService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<CommercialActionTypeDto> deleteCommercialActionTypeById(@PathVariable String Id) {
        System.out.println("test delete :" +Id);
        return commercialActionTypeService.remove(Id);

    }

    @PostMapping("/update")
    public List<CommercialActionTypeDto> update(@RequestBody @Valid CommercialActionTypeUpdaterequest commercialActionTypeUpdaterequest) {
        // Save Contract Status
        String Id = commercialActionTypeUpdaterequest.getActionTypeId();
        System.out.println(commercialActionTypeUpdaterequest + "" + Id);
        commercialActionTypeService.updateCommercialActionType(commercialActionTypeMapper.updateRequestToDto(commercialActionTypeUpdaterequest), Id);
        return commercialActionTypeService.getAllCommercialActionType();
    }

}
