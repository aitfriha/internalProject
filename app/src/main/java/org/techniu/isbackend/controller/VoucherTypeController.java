package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.VoucherTypeAddRequest;
import org.techniu.isbackend.controller.request.VoucherTypeUpdateRequest;
import org.techniu.isbackend.dto.mapper.VoucherTypeMapper;
import org.techniu.isbackend.entity.VoucherType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.VoucherTypeService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/voucherType")
@CrossOrigin("*")
public class VoucherTypeController {

    private final VoucherTypeService voucherTypeService;
    private final MapValidationErrorService mapValidationErrorService;

    private final VoucherTypeMapper voucherTypeMapper = Mappers.getMapper(VoucherTypeMapper.class);

    public VoucherTypeController(VoucherTypeService voucherTypeService, MapValidationErrorService mapValidationErrorService) {
        this.voucherTypeService = voucherTypeService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllVoucherTypes() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                voucherTypeService.getAllVoucherTypes()), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity addVoucherType(@RequestBody @Valid VoucherTypeAddRequest voucherTypeAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        voucherTypeService.saveVoucherType(voucherTypeMapper.addRequestToDto(voucherTypeAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.VoucherType, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity updateVoucherType(@RequestBody @Valid VoucherTypeUpdateRequest voucherTypeUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        voucherTypeService.updateVoucherType(voucherTypeMapper.updateRequestToDto(voucherTypeUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.VoucherType, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteVoucherType(@PathVariable String id) {
        voucherTypeService.deleteVoucherType(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.VoucherType, DELETED)), HttpStatus.OK);
    }


}
