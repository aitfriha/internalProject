package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.PurchaseOrderAcceptanceAddrequest;
import org.techniu.isbackend.controller.request.PurchaseOrderAcceptanceUpdaterequest;
import org.techniu.isbackend.dto.mapper.PurchaseOrderAcceptanceMapper;
import org.techniu.isbackend.dto.model.PurchaseOrderAcceptanceDto;
import org.techniu.isbackend.entity.PurchaseOrderAcceptance;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.PurchaseOrderAcceptanceService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.PurchaseOrderAcceptance;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/purchaseOrderAcceptance")
@CrossOrigin("*")
public class PurchaseOrderAcceptanceController {

    private PurchaseOrderAcceptanceService purchaseOrderAcceptanceService;
    private final MapValidationErrorService mapValidationErrorService;
    private final PurchaseOrderAcceptanceMapper purchaseOrderAcceptanceMapper = Mappers.getMapper(PurchaseOrderAcceptanceMapper.class);


    public PurchaseOrderAcceptanceController(PurchaseOrderAcceptanceService purchaseOrderAcceptanceService, MapValidationErrorService mapValidationErrorService) {
        this.purchaseOrderAcceptanceService = purchaseOrderAcceptanceService;
        this.mapValidationErrorService = mapValidationErrorService;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid PurchaseOrderAcceptanceAddrequest purchaseOrderAcceptanceAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save
        System.out.println(purchaseOrderAcceptanceAddrequest);
        purchaseOrderAcceptanceService.savePurchaseOrderAcceptance(purchaseOrderAcceptanceMapper.addRequestToDto(purchaseOrderAcceptanceAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(PurchaseOrderAcceptance, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<PurchaseOrderAcceptanceDto> getAllTypeOfCurrencies() {
        return purchaseOrderAcceptanceService.getAllPurchaseOrderAcceptance();

    }

    @PostMapping("/row/{Id}")
    public PurchaseOrderAcceptance getPurchaseOrderAcceptanceById(@PathVariable String Id) {
        return purchaseOrderAcceptanceService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<PurchaseOrderAcceptanceDto> deletePurchaseOrderAcceptanceById(@PathVariable String Id) {
        System.out.println("test delete :" +Id);
        return purchaseOrderAcceptanceService.remove(Id);

    }

    @PostMapping("/update")
    public List<PurchaseOrderAcceptanceDto> update(@RequestBody @Valid PurchaseOrderAcceptanceUpdaterequest purchaseOrderAcceptanceUpdaterequest) {
        // Save Contract Status
        String Id = purchaseOrderAcceptanceUpdaterequest.getPurchaseOrderAcceptanceId();
        System.out.println(purchaseOrderAcceptanceUpdaterequest + "" + Id);
        purchaseOrderAcceptanceService.updatePurchaseOrderAcceptance(purchaseOrderAcceptanceMapper.updateRequestToDto(purchaseOrderAcceptanceUpdaterequest), Id);
        return purchaseOrderAcceptanceService.getAllPurchaseOrderAcceptance();
    }

}
