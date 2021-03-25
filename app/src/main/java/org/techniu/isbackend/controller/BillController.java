package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.BillAddrequest;
import org.techniu.isbackend.controller.request.BillUpdaterequest;
import org.techniu.isbackend.dto.mapper.BillMapper;
import org.techniu.isbackend.dto.model.BillDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.*;
import org.techniu.isbackend.service.BillService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.Bill;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/bill")
@CrossOrigin("*")
public class BillController {

    private BillService billService;
    private BillRepository billRepository;
    private ClientRepository clientRepository;
    private FinancialCompanyRepository financialCompanyRepository;
    private CommercialOperationRepository commercialOperationRepository;
    private FinancialContractRepository financialContractRepository;
    private IvaRepository ivaRepository;
    private CurrencyRepository currencyRepository;

    private final MapValidationErrorService mapValidationErrorService;
    private final BillMapper billMapper = Mappers.getMapper(BillMapper.class);


    public BillController(BillService billService,
                          MapValidationErrorService mapValidationErrorService, ClientRepository clientRepository, IvaRepository ivaRepository,
                          FinancialCompanyRepository financialCompanyRepository, FinancialContractRepository financialContractRepository,
                          CurrencyRepository currencyRepository, CommercialOperationRepository commercialOperationRepository) {
        this.billService = billService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.clientRepository = clientRepository;
        this.financialCompanyRepository = financialCompanyRepository;
        this.currencyRepository = currencyRepository;
        this.commercialOperationRepository = commercialOperationRepository;
        this.financialContractRepository = financialContractRepository;
        this.ivaRepository = ivaRepository;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid BillAddrequest billAddrequest, BindingResult bindingResult) {
        System.out.println(billAddrequest);

        Client client = clientRepository.findBy_id(billAddrequest.getClient().get_id());
        Client clientSigned = clientRepository.findBy_id(billAddrequest.getClientSigned().get_id());
        FinancialCompany financialCompany = financialCompanyRepository.findAllBy_id(billAddrequest.getFinancialCompany().get_id());
        CommercialOperation commercialOperation = commercialOperationRepository.findBy_id(billAddrequest.getCommercialOperation().get_id());
        FinancialContract financialContract = financialContractRepository.findAllBy_id(billAddrequest.getFinancialContract().get_id());
        Iva iva = ivaRepository.findAllBy_id(billAddrequest.getIva().get_id());
        Currency currency = currencyRepository.findAllBy_id(billAddrequest.getCurrency().get_id());

        billAddrequest.setClient(client);
        billAddrequest.setClientSigned(clientSigned);
        billAddrequest.setFinancialCompany(financialCompany);
        billAddrequest.setCommercialOperation(commercialOperation);
        billAddrequest.setFinancialContract(financialContract);
        billAddrequest.setIva(iva);
        billAddrequest.setCurrency(currency);
        System.out.println(billAddrequest);

        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        billService.saveBill(billMapper.addRequestToDto(billAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Bill, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<BillDto> getAllBill() {
        return billService.getAllBill();
    }

    @PostMapping("/row/{Id}")
    public Bill getBillById(@PathVariable String Id) {
        return billService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<BillDto> deleteBillById(@PathVariable String Id) {
        return billService.remove(Id);

    }

    @PostMapping("/update")
    public List<BillDto> update(@RequestBody @Valid BillUpdaterequest billUpdaterequest) {
        // Save Contract Status
        String Id = billUpdaterequest.getBillId();

        System.out.println(Id);
        System.out.println(billUpdaterequest);

        billService.updateBill(billMapper.updateRequestToDto(billUpdaterequest), Id);
        return billService.getAllBill();
    }

}
