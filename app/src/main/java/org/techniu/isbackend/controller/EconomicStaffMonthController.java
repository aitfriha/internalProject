package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.EconomicStaffMonthAddrequest;
import org.techniu.isbackend.controller.request.EconomicStaffMonthUpdaterequest;
import org.techniu.isbackend.dto.mapper.EconomicStaffMonthMapper;
import org.techniu.isbackend.dto.model.EconomicStaffMonthDto;
import org.techniu.isbackend.entity.Currency;
import org.techniu.isbackend.entity.EconomicStaff;
import org.techniu.isbackend.entity.EconomicStaffMonth;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.CurrencyRepository;
import org.techniu.isbackend.repository.EconomicStaffRepository;
import org.techniu.isbackend.repository.FinancialCompanyRepository;
import org.techniu.isbackend.service.EconomicStaffMonthService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.EconomicStaffMonth;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/economicStaffMonth")
@CrossOrigin("*")
public class EconomicStaffMonthController {

    private EconomicStaffMonthService economicStaffMonthService;
    private final MapValidationErrorService mapValidationErrorService;
    private final EconomicStaffMonthMapper economicStaffMonthMapper = Mappers.getMapper(EconomicStaffMonthMapper.class);
    private EconomicStaffRepository economicStaffRepository;
    private CurrencyRepository currencyRepository;


    public EconomicStaffMonthController(EconomicStaffMonthService economicStaffMonthService, FinancialCompanyRepository financialCompanyRepository,
                                        MapValidationErrorService mapValidationErrorService, EconomicStaffRepository economicStaffRepository, CurrencyRepository currencyRepository) {
        this.economicStaffMonthService = economicStaffMonthService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.economicStaffRepository = economicStaffRepository;
        this.currencyRepository = currencyRepository;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid EconomicStaffMonthAddrequest economicStaffMonthAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save  EconomicStaffMonth
        System.out.println(economicStaffMonthAddrequest);

        EconomicStaff economicStaff = economicStaffRepository.findAllBy_id(economicStaffMonthAddrequest.getEconomicStaff().get_id());
        Currency currency = currencyRepository.findAllBy_id(economicStaffMonthAddrequest.getCurrency().get_id());

        economicStaffMonthAddrequest.setEconomicStaff(economicStaff);
        economicStaffMonthAddrequest.setCurrency(currency);

        System.out.println(economicStaffMonthAddrequest);

        economicStaffMonthService.saveEconomicStaffMonth(economicStaffMonthMapper.addRequestToDto(economicStaffMonthAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EconomicStaffMonth, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<EconomicStaffMonthDto> getAllContractSt() {
        return economicStaffMonthService.getAllEconomicStaffMonth();

    }

    @PostMapping("/row/{Id}")
    public EconomicStaffMonth getEconomicStaffMonthById(@PathVariable String Id) {
        return economicStaffMonthService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<EconomicStaffMonthDto> deleteEconomicStaffMonthById(@PathVariable String Id) {
        System.out.println("test delete :" +Id);
        return economicStaffMonthService.remove(Id);

    }

    @PostMapping("/update")
    public List<EconomicStaffMonthDto> update(@RequestBody @Valid EconomicStaffMonthUpdaterequest economicStaffMonthUpdaterequest) {
        // Save Contract Status
        String Id = economicStaffMonthUpdaterequest.getEconomicStaffMonthId();
        economicStaffMonthService.updateEconomicStaffMonth(economicStaffMonthMapper.updateRequestToDto(economicStaffMonthUpdaterequest), Id);
        return economicStaffMonthService.getAllEconomicStaffMonth();
    }

}
