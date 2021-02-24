package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.PurchaseOrderMapper;
import org.techniu.isbackend.dto.model.PurchaseOrderDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private PurchaseOrderRepository purchaseOrderRepository;
    private FinancialCompanyRepository financialCompanyRepository;
    private ExternalSupplierRepository externalSupplierRepository;
    private IvaRepository ivaRepository;
    private CurrencyRepository currencyRepository;
    private ClientRepository clientRepository;
    private FinancialContractRepository financialContractRepository;
    private final PurchaseOrderMapper purchaseOrderMapper = Mappers.getMapper(PurchaseOrderMapper.class);

    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository, IvaRepository ivaRepository, ExternalSupplierRepository externalSupplierRepository,
           FinancialCompanyRepository financialCompanyRepository, CurrencyRepository currencyRepository, ClientRepository clientRepository, FinancialContractRepository financialContractRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.financialCompanyRepository = financialCompanyRepository;
        this.currencyRepository = currencyRepository;
        this.ivaRepository = ivaRepository;
        this.externalSupplierRepository = externalSupplierRepository;
        this.clientRepository = clientRepository;
        this.financialContractRepository = financialContractRepository;
    }

    @Override
    public void savePurchaseOrder(PurchaseOrderDto purchaseOrderDto) {
        purchaseOrderRepository.save(purchaseOrderMapper.dtoToModel(purchaseOrderDto));
    }

    @Override
    public List<PurchaseOrderDto> getAllPurchaseOrder() {
        // Get all actions
        List<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findAll();
        // Create a list of all actions dto
        ArrayList<PurchaseOrderDto> purchaseOrderDtos = new ArrayList<>();

        for (PurchaseOrder purchaseOrder1 : purchaseOrder) {
            PurchaseOrderDto purchaseOrderDto = purchaseOrderMapper.modelToDto(purchaseOrder1);
            purchaseOrderDtos.add(purchaseOrderDto);
        }
        return purchaseOrderDtos;
    }

    @Override
    public PurchaseOrder getById(String id) {
        return purchaseOrderRepository.findAllBy_id(id);
    }

    @Override
    public List<PurchaseOrderDto> updatePurchaseOrder(PurchaseOrderDto purchaseOrderDto, String id) {

        PurchaseOrder purchaseOrder = getById(id);
        Optional<PurchaseOrder> purchaseOrder1 = Optional.ofNullable(purchaseOrderRepository.findAllBy_id(id));

        if (!purchaseOrder1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        FinancialCompany EmitFinancialCompany = financialCompanyRepository.findAllBy_id(purchaseOrderDto.getCompanyEmit().get_id());
        Currency currency = currencyRepository.findAllBy_id(purchaseOrderDto.getCurrency().get_id());
        Iva iva = ivaRepository.findAllBy_id(purchaseOrderDto.getIva().get_id());
        Client client = clientRepository.findBy_id(purchaseOrderDto.getClient().get_id());


        if (purchaseOrderDto.getReceptionSupplierType().equals("external")) {
            ExternalSupplier ReceptionExternalSupplier = externalSupplierRepository.findAllBy_id(purchaseOrderDto.getExternalSupplierReception().get_id());
            purchaseOrder.setExternalSupplierReception(ReceptionExternalSupplier);
            purchaseOrder.setSupplierResponsible(purchaseOrderDto.getSupplierResponsible());
        } else if (purchaseOrderDto.getReceptionSupplierType().equals("internal")) {
            FinancialCompany ReceptionFinancialCompany = financialCompanyRepository.findAllBy_id(purchaseOrderDto.getInternalSupplierReception().get_id());
            purchaseOrder.setInternalSupplierReception(ReceptionFinancialCompany);
            purchaseOrder.setInternLogo(purchaseOrderDto.getInternLogo());
        }

        if (purchaseOrderDto.getTypeClient().equals("contract") ) {
            FinancialContract financialContract = financialContractRepository.findAllBy_id(purchaseOrderDto.getFinancialContract().get_id());
            purchaseOrder.setFinancialContract(financialContract);
        } else if (purchaseOrderDto.getTypeClient().equals("po") ) {
            purchaseOrder.setFinancialContract(null);
        }

        purchaseOrder.setCompanyEmit(EmitFinancialCompany);
        purchaseOrder.setCurrency(currency);
        purchaseOrder.setIva(iva);
        purchaseOrder.setPurchaseNumber(purchaseOrderDto.getPurchaseNumber());
        purchaseOrder.setClient(client);

        purchaseOrder.setFactor(purchaseOrderDto.getFactor());
        purchaseOrder.setTypeClient(purchaseOrderDto.getTypeClient());
        purchaseOrder.setCompanyNIF(purchaseOrderDto.getCompanyNIF());
        purchaseOrder.setCompanyAddress(purchaseOrderDto.getCompanyAddress());
        purchaseOrder.setCompanyLogo(purchaseOrderDto.getCompanyLogo());
        purchaseOrder.setReceptionSupplierType(purchaseOrderDto.getReceptionSupplierType());
        purchaseOrder.setSupplierAddress(purchaseOrderDto.getSupplierAddress());
        purchaseOrder.setSupplierNIF(purchaseOrderDto.getSupplierNIF());
        purchaseOrder.setPaymentMethod(purchaseOrderDto.getPaymentMethod());

        purchaseOrder.setTermsListe(purchaseOrderDto.getTermsListe());
        purchaseOrder.setTermTitle(purchaseOrderDto.getTermTitle());
        purchaseOrder.setTermDescription(purchaseOrderDto.getTermDescription());

        purchaseOrder.setNbrConcepts(purchaseOrderDto.getNbrConcepts());
        purchaseOrder.setItemNames(purchaseOrderDto.getItemNames());
        purchaseOrder.setDescription(purchaseOrderDto.getDescription());
        purchaseOrder.setUnityValue(purchaseOrderDto.getUnityValue());
        purchaseOrder.setUnityNumber(purchaseOrderDto.getUnityNumber());
        purchaseOrder.setUnity(purchaseOrderDto.getUnity());
        purchaseOrder.setValor(purchaseOrderDto.getValor());
        purchaseOrder.setBillingDate(purchaseOrderDto.getBillingDate());
        purchaseOrder.setPaymentDate(purchaseOrderDto.getPaymentDate());
        purchaseOrder.setGivingDate(purchaseOrderDto.getGivingDate());

        purchaseOrder.setTotalLocal(purchaseOrderDto.getTotalLocal());
        purchaseOrder.setTotalEuro(purchaseOrderDto.getTotalEuro());
        purchaseOrder.setValueIVALocal(purchaseOrderDto.getValueIVALocal());
        purchaseOrder.setValueIVAEuro(purchaseOrderDto.getValueIVAEuro());

        purchaseOrder.setTotalAmountEuro(purchaseOrderDto.getTotalAmountEuro());
        purchaseOrder.setTotalAmountLocal(purchaseOrderDto.getTotalAmountLocal());
        purchaseOrder.setIvaRetentions(purchaseOrderDto.getIvaRetentions());
        purchaseOrder.setTotalAmountRetentions(purchaseOrderDto.getTotalAmountRetentions());
        purchaseOrder.setTotalIvaRetention(purchaseOrderDto.getTotalIvaRetention());

        purchaseOrderRepository.save(purchaseOrder);
        return getAllPurchaseOrder();
    }

    @Override
    public List<PurchaseOrderDto> remove(String id) {
        Optional<PurchaseOrder> action = Optional.ofNullable(purchaseOrderRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        purchaseOrderRepository.deleteById(id);
        return getAllPurchaseOrder();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.PurchaseOrder, exceptionType, args);
    }
}
