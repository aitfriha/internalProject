package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.EconomicStaffExtraAddrequest;
import org.techniu.isbackend.controller.request.EconomicStaffExtraUpdaterequest;
import org.techniu.isbackend.dto.mapper.EconomicStaffExtraMapper;
import org.techniu.isbackend.dto.model.EconomicStaffExtraDto;
import org.techniu.isbackend.entity.Currency;
import org.techniu.isbackend.entity.EconomicStaff;
import org.techniu.isbackend.entity.EconomicStaffExtra;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.CurrencyRepository;
import org.techniu.isbackend.repository.EconomicStaffRepository;
import org.techniu.isbackend.repository.FinancialCompanyRepository;
import org.techniu.isbackend.service.EconomicStaffExtraService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.EconomicStaffExtra;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/economicStaffExtra")
@CrossOrigin("*")
public class EconomicStaffExtraController {

    private EconomicStaffExtraService economicStaffExtraService;
    private final MapValidationErrorService mapValidationErrorService;
    private final EconomicStaffExtraMapper economicStaffExtraMapper = Mappers.getMapper(EconomicStaffExtraMapper.class);
    private EconomicStaffRepository economicStaffRepository;
    private CurrencyRepository currencyRepository;


    public EconomicStaffExtraController(EconomicStaffExtraService economicStaffExtraService, FinancialCompanyRepository financialCompanyRepository,
                                        MapValidationErrorService mapValidationErrorService, EconomicStaffRepository economicStaffRepository, CurrencyRepository currencyRepository) {
        this.economicStaffExtraService = economicStaffExtraService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.economicStaffRepository = economicStaffRepository;
        this.currencyRepository = currencyRepository;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid EconomicStaffExtraAddrequest economicStaffExtraAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save  EconomicStaffExtra
        System.out.println(economicStaffExtraAddrequest);

        EconomicStaff economicStaff = economicStaffRepository.findAllBy_id(economicStaffExtraAddrequest.getEconomicStaff().get_id());
        Currency currency = currencyRepository.findAllBy_id(economicStaffExtraAddrequest.getCurrency().get_id());

        economicStaffExtraAddrequest.setEconomicStaff(economicStaff);
        economicStaffExtraAddrequest.setCurrency(currency);

        System.out.println(economicStaffExtraAddrequest);

        economicStaffExtraService.saveEconomicStaffExtra(economicStaffExtraMapper.addRequestToDto(economicStaffExtraAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EconomicStaffExtra, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<EconomicStaffExtraDto> getAllContractSt() {
        return economicStaffExtraService.getAllEconomicStaffExtra();

    }

    @PostMapping("/row/{Id}")
    public EconomicStaffExtra getEconomicStaffExtraById(@PathVariable String Id) {
        return economicStaffExtraService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<EconomicStaffExtraDto> deleteEconomicStaffExtraById(@PathVariable String Id) {
        System.out.println("test delete :" +Id);
        return economicStaffExtraService.remove(Id);

    }

    @PostMapping("/update")
    public List<EconomicStaffExtraDto> update(@RequestBody @Valid EconomicStaffExtraUpdaterequest economicStaffExtraUpdaterequest) {
        // Save Contract Status
        String Id = economicStaffExtraUpdaterequest.getEconomicStaffExtraId();
        economicStaffExtraService.updateEconomicStaffExtra(economicStaffExtraMapper.updateRequestToDto(economicStaffExtraUpdaterequest), Id);
        return economicStaffExtraService.getAllEconomicStaffExtra();
    }

}
