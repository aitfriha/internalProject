package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.SupplierTypeAddrequest;
import org.techniu.isbackend.controller.request.SupplierTypeUpdaterequest;
import org.techniu.isbackend.dto.mapper.SupplierTypeMapper;
import org.techniu.isbackend.dto.model.SupplierTypeDto;
import org.techniu.isbackend.entity.SupplierType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.SupplierTypeService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.SupplierType;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/supplierType")
@CrossOrigin("*")
public class SupplierTypeController {

    private SupplierTypeService supplierTypeService;
    private final MapValidationErrorService mapValidationErrorService;
    private final SupplierTypeMapper supplierTypeMapper = Mappers.getMapper(SupplierTypeMapper.class);


    public SupplierTypeController(SupplierTypeService supplierTypeService, MapValidationErrorService mapValidationErrorService) {
        this.supplierTypeService = supplierTypeService;
        this.mapValidationErrorService = mapValidationErrorService;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid SupplierTypeAddrequest supplierTypeAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save Type Of Currency
        System.out.println(supplierTypeAddrequest);
        supplierTypeService.saveSupplierType(supplierTypeMapper.addRequestToDto(supplierTypeAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(SupplierType, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<SupplierTypeDto> getAllTypeOfCurrencies() {
        return supplierTypeService.getAllSupplierType();

    }

    @PostMapping("/row/{Id}")
    public SupplierType getSupplierTypeById(@PathVariable String Id) {
        return supplierTypeService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<SupplierTypeDto> deleteSupplierTypeById(@PathVariable String Id) {
        return supplierTypeService.remove(Id);

    }

    @PostMapping("/update")
    public List<SupplierTypeDto> update(@RequestBody @Valid SupplierTypeUpdaterequest supplierTypeUpdaterequest) {
        // Save Contract Status
        String Id = supplierTypeUpdaterequest.getSupplierTypeId();
        supplierTypeService.updateSupplierType(supplierTypeMapper.updateRequestToDto(supplierTypeUpdaterequest), Id);
        return supplierTypeService.getAllSupplierType();
    }

}
