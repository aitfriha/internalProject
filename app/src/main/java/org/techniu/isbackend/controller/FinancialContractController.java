package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.FinancialContractAddrequest;
import org.techniu.isbackend.controller.request.FinancialContractUpdaterequest;
import org.techniu.isbackend.dto.mapper.FinancialContractMapper;
import org.techniu.isbackend.dto.model.FinancialContractDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.*;
import org.techniu.isbackend.service.AddressService;
import org.techniu.isbackend.service.FinancialContractService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.FinancialContract;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/contract")
@CrossOrigin("*")
public class FinancialContractController {

    private FinancialContractService financialContractService;
    public CityRepository cityRepository;
    private AddressService addressService;
    private ClientRepository clientRepository;
    private FinancialCompanyRepository financialCompanyRepository;
    private CommercialOperationRepository commercialOperationRepository;
    private ContractStatusRepository contractStatusRepository;
    private FunctionalStructureLevelRepository functionalStructureLevelRepository;
    private CurrencyRepository currencyRepository;

    private final MapValidationErrorService mapValidationErrorService;
    private final FinancialContractMapper financialContractMapper = Mappers.getMapper(FinancialContractMapper.class);


    public FinancialContractController(FinancialContractService financialContractService, CityRepository cityRepository,
                                       AddressService addressService, MapValidationErrorService mapValidationErrorService, ClientRepository clientRepository,
                                       FinancialCompanyRepository financialCompanyRepository,  ContractStatusRepository contractStatusRepository,
                                       FunctionalStructureLevelRepository functionalStructureLevelRepository, CurrencyRepository currencyRepository, CommercialOperationRepository commercialOperationRepository) {
        this.financialContractService = financialContractService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.cityRepository = cityRepository;
        this.addressService = addressService;
        this.clientRepository = clientRepository;
        this.financialCompanyRepository = financialCompanyRepository;
        this.contractStatusRepository = contractStatusRepository;
        this.functionalStructureLevelRepository = functionalStructureLevelRepository;
        this.currencyRepository = currencyRepository;
        this.commercialOperationRepository = commercialOperationRepository;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid FinancialContractAddrequest financialContractAddrequest, BindingResult bindingResult) {
        City city = cityRepository.findCityBy_id(financialContractAddrequest.getAddress().getCity().get_id());
        Address address = new Address();
        address.setCity(city);
        Address currentAddress = addressService.saveAddress(address);

        Client client = clientRepository.findBy_id(financialContractAddrequest.getClient().get_id());
        FinancialCompany financialCompany = financialCompanyRepository.findAllBy_id(financialContractAddrequest.getFinancialCompany().get_id());
        CommercialOperation commercialOperation = commercialOperationRepository.findBy_id(financialContractAddrequest.getCommercialOperation().get_id());
        ContractStatus contractStatus = contractStatusRepository.findAllBy_id(financialContractAddrequest.getContractStatus().get_id());
        FunctionalStructureLevel functionalStructureLevel = functionalStructureLevelRepository.findBy_id(financialContractAddrequest.getFunctionalStructureLevel().get_id());
        Currency currency = currencyRepository.findAllBy_id(financialContractAddrequest.getCurrency().get_id());

        financialContractAddrequest.setAddress(address);
        financialContractAddrequest.setClient(client);
        financialContractAddrequest.setContractStatus(contractStatus);
        financialContractAddrequest.setFunctionalStructureLevel(functionalStructureLevel);
        financialContractAddrequest.setCurrency(currency);
        financialContractAddrequest.setCommercialOperation(commercialOperation);
        System.out.println(financialContractAddrequest);

        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        financialContractService.saveFinancialContract(financialContractMapper.addRequestToDto(financialContractAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(FinancialContract, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<FinancialContractDto> getAllFinancialContract() {
        return financialContractService.getAllFinancialContract();

    }

    @PostMapping("/row/{Id}")
    public FinancialContract getFinancialContractById(@PathVariable String Id) {
        return financialContractService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<FinancialContractDto> deleteFinancialContractById(@PathVariable String Id) {
        return financialContractService.remove(Id);

    }

    @PostMapping("/update")
    public List<FinancialContractDto> update(@RequestBody @Valid FinancialContractUpdaterequest financialContractUpdaterequest) {
        // Save Contract Status
        String Id = financialContractUpdaterequest.getFinancialContractId();

        financialContractService.updateFinancialContract(financialContractMapper.updateRequestToDto(financialContractUpdaterequest), Id);
        return financialContractService.getAllFinancialContract();
    }

}
