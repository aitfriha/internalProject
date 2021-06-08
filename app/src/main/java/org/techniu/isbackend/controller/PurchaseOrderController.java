package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.PurchaseOrderAddrequest;
import org.techniu.isbackend.controller.request.PurchaseOrderUpdaterequest;
import org.techniu.isbackend.dto.mapper.PurchaseOrderMapper;
import org.techniu.isbackend.dto.model.PurchaseOrderDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.*;
import org.techniu.isbackend.service.PurchaseOrderService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.PurchaseOrder;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/purchaseOrder")
@CrossOrigin("*")
public class PurchaseOrderController {

    private PurchaseOrderService purchaseOrderService;
    private FinancialCompanyRepository financialCompanyRepository;
    private ExternalSupplierRepository externalSupplierRepository;
    private IvaRepository ivaRepository;
    private CurrencyRepository currencyRepository;
    private ClientRepository clientRepository;
    private FinancialContractRepository financialContractRepository;
    private static int code = 0;
    private final MapValidationErrorService mapValidationErrorService;
    private final PurchaseOrderMapper purchaseOrderMapper = Mappers.getMapper(PurchaseOrderMapper.class);


    public PurchaseOrderController (PurchaseOrderService purchaseOrderService, ExternalSupplierRepository externalSupplierRepository,
                                    MapValidationErrorService mapValidationErrorService, FinancialCompanyRepository financialCompanyRepository, ContractStatusRepository contractStatusRepository,
                                    IvaRepository ivaRepository, CurrencyRepository currencyRepository, ClientRepository clientRepository, FinancialContractRepository financialContractRepository) {
        this.purchaseOrderService = purchaseOrderService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.financialCompanyRepository = financialCompanyRepository;
        this.externalSupplierRepository = externalSupplierRepository;
        this.currencyRepository = currencyRepository;
        this.ivaRepository = ivaRepository;
        this.clientRepository = clientRepository;
        this.financialContractRepository = financialContractRepository;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid PurchaseOrderAddrequest purchaseOrderAddrequest, BindingResult bindingResult) {

        code++;
        Date d = new Date();

        String year = String.valueOf(d).substring(25,28);

        String Code = year.concat("/").concat(String.valueOf(code));
        System.out.println("CODE :" + Code);

        purchaseOrderAddrequest.setPurchaseNumber(Code);

        FinancialCompany EmitFinancialCompany = financialCompanyRepository.findAllBy_id(purchaseOrderAddrequest.getCompanyEmit().get_id());
        Currency currency = currencyRepository.findAllBy_id(purchaseOrderAddrequest.getCurrency().get_id());
        Iva iva = ivaRepository.findAllBy_id(purchaseOrderAddrequest.getIva().get_id());
        Client client = clientRepository.findBy_id(purchaseOrderAddrequest.getClient().get_id());

        if (purchaseOrderAddrequest.getReceptionSupplierType().equals("external")) {
            ExternalSupplier ReceptionExternalSupplier = externalSupplierRepository.findAllBy_id(purchaseOrderAddrequest.getExternalSupplierReception().get_id());
            purchaseOrderAddrequest.setExternalSupplierReception(ReceptionExternalSupplier);
            purchaseOrderAddrequest.setSupplierResponsible(purchaseOrderAddrequest.getSupplierResponsible());
            purchaseOrderAddrequest.setInternalSupplierReception(null);
        } else if (purchaseOrderAddrequest.getReceptionSupplierType().equals("internal")) {
            FinancialCompany ReceptionFinancialCompany = financialCompanyRepository.findAllBy_id(purchaseOrderAddrequest.getInternalSupplierReception().get_id());
            purchaseOrderAddrequest.setInternalSupplierReception(ReceptionFinancialCompany);
            purchaseOrderAddrequest.setInternLogo(purchaseOrderAddrequest.getInternLogo());
            purchaseOrderAddrequest.setExternalSupplierReception(null);
        }

        if (purchaseOrderAddrequest.getTypeClient().equals("contract") ) {
            FinancialContract financialContract = financialContractRepository.findAllBy_id(purchaseOrderAddrequest.getFinancialContract().get_id());
            purchaseOrderAddrequest.setFinancialContract(financialContract);
        } else if (purchaseOrderAddrequest.getTypeClient().equals("po") ) {
            purchaseOrderAddrequest.setFinancialContract(null);
        }

        purchaseOrderAddrequest.setClient(client);
        purchaseOrderAddrequest.setCompanyEmit(EmitFinancialCompany);
        purchaseOrderAddrequest.setCurrency(currency);
        purchaseOrderAddrequest.setIva(iva);

        System.out.println(purchaseOrderAddrequest);

        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        purchaseOrderService.savePurchaseOrder(purchaseOrderMapper.addRequestToDto(purchaseOrderAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(PurchaseOrder, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<PurchaseOrderDto> getAllPurchaseOrder() {
        return purchaseOrderService.getAllPurchaseOrder();

    }

    @PostMapping("/row/{Id}")
    public PurchaseOrder getPurchaseOrderById(@PathVariable String Id) {
        return purchaseOrderService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<PurchaseOrderDto> deletePurchaseOrderById(@PathVariable String Id) {
        return purchaseOrderService.remove(Id);

    }

    @PostMapping("/update")
    public List<PurchaseOrderDto> update(@RequestBody @Valid PurchaseOrderUpdaterequest purchaseOrderUpdaterequest) {
        // Save Contract Status
        String Id = purchaseOrderUpdaterequest.getPurchaseOrderId();

        purchaseOrderService.updatePurchaseOrder(purchaseOrderMapper.updateRequestToDto(purchaseOrderUpdaterequest), Id);
        return purchaseOrderService.getAllPurchaseOrder();
    }

}
