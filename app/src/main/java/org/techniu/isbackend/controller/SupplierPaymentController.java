package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.SupplierPaymentAddrequest;
import org.techniu.isbackend.controller.request.SupplierPaymentUpdaterequest;
import org.techniu.isbackend.dto.mapper.SupplierPaymentMapper;
import org.techniu.isbackend.dto.model.SupplierPaymentDto;
import org.techniu.isbackend.entity.SupplierPayment;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.SupplierPaymentService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.SupplierPayment;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/supplierPayment")
@CrossOrigin("*")
public class SupplierPaymentController {

    private SupplierPaymentService supplierPaymentService;
    private final MapValidationErrorService mapValidationErrorService;
    private final SupplierPaymentMapper supplierPaymentMapper = Mappers.getMapper(SupplierPaymentMapper.class);


    public SupplierPaymentController(SupplierPaymentService supplierPaymentService, MapValidationErrorService mapValidationErrorService) {
        this.supplierPaymentService = supplierPaymentService;
        this.mapValidationErrorService = mapValidationErrorService;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid SupplierPaymentAddrequest supplierPaymentAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save Type Of Currency
        System.out.println(supplierPaymentAddrequest);
        supplierPaymentService.saveSupplierPayment(supplierPaymentMapper.addRequestToDto(supplierPaymentAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(SupplierPayment, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<SupplierPaymentDto> getAllTypeOfCurrencies() {
        return supplierPaymentService.getAllSupplierPayment();

    }

    @PostMapping("/row/{Id}")
    public SupplierPayment getSupplierPaymentById(@PathVariable String Id) {
        return supplierPaymentService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<SupplierPaymentDto> deleteSupplierPaymentById(@PathVariable String Id) {
        System.out.println("test delete :" +Id);
        return supplierPaymentService.remove(Id);

    }

    @PostMapping("/update")
    public List<SupplierPaymentDto> update(@RequestBody @Valid SupplierPaymentUpdaterequest supplierPaymentUpdaterequest) {
        // Save Contract Status
        String Id = supplierPaymentUpdaterequest.getSupplierPaymentId();
        System.out.println(supplierPaymentUpdaterequest + "" + Id);
        supplierPaymentService.updateSupplierPayment(supplierPaymentMapper.updateRequestToDto(supplierPaymentUpdaterequest), Id);
        return supplierPaymentService.getAllSupplierPayment();
    }

}
