package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.ExternalSupplierAddrequest;
import org.techniu.isbackend.controller.request.ExternalSupplierUpdaterequest;
import org.techniu.isbackend.dto.mapper.ExternalSupplierMapper;
import org.techniu.isbackend.dto.model.ExternalSupplierDto;
import org.techniu.isbackend.entity.Address;
import org.techniu.isbackend.entity.City;
import org.techniu.isbackend.entity.ExternalSupplier;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.CityRepository;
import org.techniu.isbackend.service.AddressService;
import org.techniu.isbackend.service.ExternalSupplierService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.ExternalSupplier;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/externalSupplier")
@CrossOrigin("*")
public class ExternalSupplierController {

    private ExternalSupplierService externalSupplierService;
    public CityRepository cityRepository;
    private AddressService addressService;

    private final MapValidationErrorService mapValidationErrorService;
    private final ExternalSupplierMapper externalSupplierMapper = Mappers.getMapper(ExternalSupplierMapper.class);


    public ExternalSupplierController(ExternalSupplierService externalSupplierService, CityRepository cityRepository,
                                      AddressService addressService, MapValidationErrorService mapValidationErrorService) {
        this.externalSupplierService = externalSupplierService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.cityRepository = cityRepository;
        this.addressService = addressService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid ExternalSupplierAddrequest externalSupplierAddrequest, BindingResult bindingResult) {
        City city = cityRepository.findCityBy_id(externalSupplierAddrequest.getAddress().getCity().get_id());

        Address address = new Address();
        address.setFullAddress(externalSupplierAddrequest.getAddress().getFullAddress());
        address.setPostCode(externalSupplierAddrequest.getAddress().getPostCode());
        address.setCity(city);

        Address currentAddress = addressService.saveAddress(address);
        externalSupplierAddrequest.setAddress(currentAddress);
        System.out.println(" test :"  + externalSupplierAddrequest);

        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        externalSupplierService.saveExternalSupplier(externalSupplierMapper.addRequestToDto(externalSupplierAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(ExternalSupplier, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<ExternalSupplierDto> getAllExternalSupplier() {
        return externalSupplierService.getAllExternalSupplier();

    }

    @PostMapping("/row/{Id}")
    public ExternalSupplier getExternalSupplierById(@PathVariable String Id) {
        return externalSupplierService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<ExternalSupplierDto> deleteExternalSupplierById(@PathVariable String Id) {
        return externalSupplierService.remove(Id);
    }

    @PostMapping("/update")
    public List<ExternalSupplierDto> update(@RequestBody @Valid ExternalSupplierUpdaterequest externalSupplierUpdaterequest) {
        // Save Contract Status
        String Id = externalSupplierUpdaterequest.getExternalSupplierId();
        externalSupplierService.updateExternalSupplier(externalSupplierMapper.updateRequestToDto(externalSupplierUpdaterequest), Id);
        return externalSupplierService.getAllExternalSupplier();
    }

}
