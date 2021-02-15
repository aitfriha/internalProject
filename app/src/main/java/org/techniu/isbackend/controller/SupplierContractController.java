package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.SupplierContractAddrequest;
import org.techniu.isbackend.controller.request.SupplierContractUpdaterequest;
import org.techniu.isbackend.dto.mapper.SupplierContractMapper;
import org.techniu.isbackend.dto.model.SupplierContractDto;
import org.techniu.isbackend.entity.SupplierContract;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.SupplierContractService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.SupplierContract;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/supplierContract")
@CrossOrigin("*")
public class SupplierContractController {

    private SupplierContractService supplierContractService;
    private final MapValidationErrorService mapValidationErrorService;
    private final SupplierContractMapper supplierContractMapper = Mappers.getMapper(SupplierContractMapper.class);


    public SupplierContractController(SupplierContractService supplierContractService, MapValidationErrorService mapValidationErrorService) {
        this.supplierContractService = supplierContractService;
        this.mapValidationErrorService = mapValidationErrorService;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid SupplierContractAddrequest supplierContractAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save Type Of Currency
        System.out.println(supplierContractAddrequest);
        supplierContractService.saveSupplierContract(supplierContractMapper.addRequestToDto(supplierContractAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(SupplierContract, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<SupplierContractDto> getAllTypeOfCurrencies() {
        return supplierContractService.getAllSupplierContract();

    }

    @PostMapping("/row/{Id}")
    public SupplierContract getSupplierContractById(@PathVariable String Id) {
        return supplierContractService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<SupplierContractDto> deleteSupplierContractById(@PathVariable String Id) {
        System.out.println("test delete :" +Id);
        return supplierContractService.remove(Id);

    }

    @PostMapping("/update")
    public List<SupplierContractDto> update(@RequestBody @Valid SupplierContractUpdaterequest supplierContractUpdaterequest) {
        // Save Contract Status
        String Id = supplierContractUpdaterequest.getSupplierContractId();
        System.out.println(supplierContractUpdaterequest + "" + Id);
        supplierContractService.updateSupplierContract(supplierContractMapper.updateRequestToDto(supplierContractUpdaterequest), Id);
        return supplierContractService.getAllSupplierContract();
    }

}
