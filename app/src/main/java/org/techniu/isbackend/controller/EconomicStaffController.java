package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.EconomicStaffAddrequest;
import org.techniu.isbackend.controller.request.EconomicStaffUpdaterequest;
import org.techniu.isbackend.dto.mapper.EconomicStaffMapper;
import org.techniu.isbackend.dto.model.EconomicStaffDto;
import org.techniu.isbackend.entity.EconomicStaff;
import org.techniu.isbackend.entity.FinancialCompany;
import org.techniu.isbackend.entity.Staff;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.FinancialCompanyRepository;
import org.techniu.isbackend.repository.StaffRepository;
import org.techniu.isbackend.repository.StateCountryRepository;
import org.techniu.isbackend.service.EconomicStaffService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.EconomicStaff;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/economicStaff")
@CrossOrigin("*")
public class EconomicStaffController {

    private EconomicStaffService economicStaffService;
    private final MapValidationErrorService mapValidationErrorService;
    private final EconomicStaffMapper economicStaffMapper = Mappers.getMapper(EconomicStaffMapper.class);
    private StaffRepository staffRepository;
    private FinancialCompanyRepository financialCompanyRepository;


    public EconomicStaffController(EconomicStaffService economicStaffService, FinancialCompanyRepository financialCompanyRepository,
        MapValidationErrorService mapValidationErrorService, StaffRepository staffRepository) {
        this.economicStaffService = economicStaffService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.staffRepository = staffRepository;
        this.financialCompanyRepository = financialCompanyRepository;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid EconomicStaffAddrequest economicStaffAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save  EconomicStaff
        System.out.println(economicStaffAddrequest);

        Staff staff = staffRepository.findAllByStaffId(economicStaffAddrequest.getStaff().getStaffId());
        FinancialCompany financialCompany = financialCompanyRepository.findAllBy_id(economicStaffAddrequest.getFinancialCompany().get_id());

        economicStaffAddrequest.setStaff(staff);
        economicStaffAddrequest.setFinancialCompany(financialCompany);

        System.out.println(economicStaffAddrequest);

        economicStaffService.saveEconomicStaff(economicStaffMapper.addRequestToDto(economicStaffAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EconomicStaff, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<EconomicStaffDto> getAllContractSt() {
        return economicStaffService.getAllEconomicStaff();

    }

    @PostMapping("/row/{Id}")
    public EconomicStaff getEconomicStaffById(@PathVariable String Id) {
        return economicStaffService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<EconomicStaffDto> deleteEconomicStaffById(@PathVariable String Id) {
        System.out.println("test delete :" +Id);
        return economicStaffService.remove(Id);

    }

    @PostMapping("/update")
    public List<EconomicStaffDto> update(@RequestBody @Valid EconomicStaffUpdaterequest economicStaffUpdaterequest) {
        // Save Contract Status
        String Id = economicStaffUpdaterequest.getEconomicStaffId();
        economicStaffService.updateEconomicStaff(economicStaffMapper.updateRequestToDto(economicStaffUpdaterequest), Id);
        return economicStaffService.getAllEconomicStaff();
    }

}
