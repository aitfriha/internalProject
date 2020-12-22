package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.FinancialCompanyAddrequest;
import org.techniu.isbackend.controller.request.FinancialCompanyUpdaterequest;
import org.techniu.isbackend.dto.mapper.FinancialCompanyMapper;
import org.techniu.isbackend.dto.model.FinancialCompanyDto;
import org.techniu.isbackend.entity.Address;
import org.techniu.isbackend.entity.City;
import org.techniu.isbackend.entity.FinancialCompany;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.CityRepository;
import org.techniu.isbackend.service.AddressService;
import org.techniu.isbackend.service.FinancialCompanyService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.FinancialCompany;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/financialCompany")
@CrossOrigin("*")
public class FinancialCompanyController {

    private FinancialCompanyService financialCompanyService;
    public CityRepository cityRepository;
    private AddressService addressService;

    private final MapValidationErrorService mapValidationErrorService;
    private final FinancialCompanyMapper financialCompanyMapper = Mappers.getMapper(FinancialCompanyMapper.class);


    public FinancialCompanyController(FinancialCompanyService financialCompanyService, CityRepository cityRepository,
                                      AddressService addressService, MapValidationErrorService mapValidationErrorService) {
        this.financialCompanyService = financialCompanyService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.cityRepository = cityRepository;
        this.addressService = addressService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid FinancialCompanyAddrequest financialCompanyAddrequest, BindingResult bindingResult) {
        City city = cityRepository.findCityBy_id(financialCompanyAddrequest.getAddress().getCity().get_id());

        Address address = new Address();
        address.setFullAddress(financialCompanyAddrequest.getAddress().getFullAddress());
        address.setPostCode(financialCompanyAddrequest.getAddress().getPostCode());
        address.setCity(city);

        Address currentAddress = addressService.saveAddress(address);
        financialCompanyAddrequest.setAddress(currentAddress);

        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        financialCompanyService.saveFinancialCompany(financialCompanyMapper.addRequestToDto(financialCompanyAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(FinancialCompany, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<FinancialCompanyDto> getAllFinancialCompany() {
        return financialCompanyService.getAllFinancialCompany();

    }

    @PostMapping("/row/{Id}")
    public FinancialCompany getFinancialCompanyById(@PathVariable String Id) {
        return financialCompanyService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<FinancialCompanyDto> deleteFinancialCompanyById(@PathVariable String Id) {
        return financialCompanyService.remove(Id);

    }

    @PostMapping("/update")
    public List<FinancialCompanyDto> update(@RequestBody @Valid FinancialCompanyUpdaterequest financialCompanyUpdaterequest) {
        // Save Contract Status
        String Id = financialCompanyUpdaterequest.getFinancialCompanyId();
        financialCompanyService.updateFinancialCompany(financialCompanyMapper.updateRequestToDto(financialCompanyUpdaterequest), Id);
        return financialCompanyService.getAllFinancialCompany();
    }

}
