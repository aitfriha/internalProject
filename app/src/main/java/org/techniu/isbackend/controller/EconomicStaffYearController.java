package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.EconomicStaffYearAddrequest;
import org.techniu.isbackend.controller.request.EconomicStaffYearUpdaterequest;
import org.techniu.isbackend.dto.mapper.EconomicStaffYearMapper;
import org.techniu.isbackend.dto.model.EconomicStaffYearDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.CurrencyRepository;
import org.techniu.isbackend.repository.EconomicStaffRepository;
import org.techniu.isbackend.repository.FinancialCompanyRepository;
import org.techniu.isbackend.repository.StaffRepository;
import org.techniu.isbackend.service.EconomicStaffYearService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.EconomicStaffYear;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/economicStaffYear")
@CrossOrigin("*")
public class EconomicStaffYearController {

    private EconomicStaffYearService economicStaffYearService;
    private final MapValidationErrorService mapValidationErrorService;
    private final EconomicStaffYearMapper economicStaffYearMapper = Mappers.getMapper(EconomicStaffYearMapper.class);
    private EconomicStaffRepository economicStaffRepository;
    private CurrencyRepository currencyRepository;


    public EconomicStaffYearController(EconomicStaffYearService economicStaffYearService, FinancialCompanyRepository financialCompanyRepository,
                                       MapValidationErrorService mapValidationErrorService, EconomicStaffRepository economicStaffRepository, CurrencyRepository currencyRepository) {
        this.economicStaffYearService = economicStaffYearService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.economicStaffRepository = economicStaffRepository;
        this.currencyRepository = currencyRepository;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid EconomicStaffYearAddrequest economicStaffYearAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save  EconomicStaffYear
        System.out.println(economicStaffYearAddrequest);

        EconomicStaff economicStaff = economicStaffRepository.findAllBy_id(economicStaffYearAddrequest.getEconomicStaff().get_id());
        Currency currency = currencyRepository.findAllBy_id(economicStaffYearAddrequest.getCurrency().get_id());

        economicStaffYearAddrequest.setEconomicStaff(economicStaff);
        economicStaffYearAddrequest.setCurrency(currency);

        System.out.println(economicStaffYearAddrequest);

        economicStaffYearService.saveEconomicStaffYear(economicStaffYearMapper.addRequestToDto(economicStaffYearAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EconomicStaffYear, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<EconomicStaffYearDto> getAllContractSt() {
        return economicStaffYearService.getAllEconomicStaffYear();

    }

    @PostMapping("/row/{Id}")
    public EconomicStaffYear getEconomicStaffYearById(@PathVariable String Id) {
        return economicStaffYearService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<EconomicStaffYearDto> deleteEconomicStaffYearById(@PathVariable String Id) {
        System.out.println("test delete :" +Id);
        return economicStaffYearService.remove(Id);

    }

    @PostMapping("/update")
    public List<EconomicStaffYearDto> update(@RequestBody @Valid EconomicStaffYearUpdaterequest economicStaffYearUpdaterequest) {
        // Save Contract Status
        String Id = economicStaffYearUpdaterequest.getEconomicStaffYearId();
        economicStaffYearService.updateEconomicStaffYear(economicStaffYearMapper.updateRequestToDto(economicStaffYearUpdaterequest), Id);
        return economicStaffYearService.getAllEconomicStaffYear();
    }

}
