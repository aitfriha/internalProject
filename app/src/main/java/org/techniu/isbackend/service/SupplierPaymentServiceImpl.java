package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.SupplierPaymentMapper;
import org.techniu.isbackend.dto.model.SupplierPaymentDto;
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
public class SupplierPaymentServiceImpl implements SupplierPaymentService {
    private SupplierPaymentRepository supplierPaymentRepository;
    private FinancialCompanyRepository financialCompanyRepository;
    private ExternalSupplierRepository externalSupplierRepository;
    private ClientRepository clientRepository;
    private LogService logService;
    private FinancialContractRepository financialContractRepository;
    private PurchaseOrderRepository purchaseOrderRepository;
    private CurrencyRepository currencyRepository;
    private final SupplierPaymentMapper supplierPaymentMapper = Mappers.getMapper(SupplierPaymentMapper.class);

    public SupplierPaymentServiceImpl(SupplierPaymentRepository supplierPaymentRepository, CurrencyRepository currencyRepository, FinancialCompanyRepository financialCompanyRepository, ClientRepository clientRepository,
                                      ExternalSupplierRepository externalSupplierRepository, LogService logService, FinancialContractRepository financialContractRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.supplierPaymentRepository = supplierPaymentRepository;
        this.financialCompanyRepository = financialCompanyRepository;
        this.externalSupplierRepository = externalSupplierRepository;
        this.clientRepository = clientRepository;
        this.logService = logService;
        this.financialContractRepository = financialContractRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void saveSupplierPayment(SupplierPaymentDto supplierPaymentDto) {
        if (supplierPaymentDto.getType().equals("internal") ) {
            FinancialCompany financialCompany = financialCompanyRepository.findAllBy_id(supplierPaymentDto.getFinancialCompany().get_id());
            supplierPaymentDto.setFinancialCompany(financialCompany);
            supplierPaymentDto.setExternalSupplier(null);
        } else if (supplierPaymentDto.getType().equals("external") ) {
            ExternalSupplier externalSupplier = externalSupplierRepository.findAllBy_id(supplierPaymentDto.getExternalSupplier().get_id());
            supplierPaymentDto.setExternalSupplier(externalSupplier);
            supplierPaymentDto.setFinancialCompany(null);
        }
        if (supplierPaymentDto.getTypeClient().equals("contract") ) {
            FinancialContract financialContract = financialContractRepository.findAllBy_id(supplierPaymentDto.getFinancialContract().get_id());
            supplierPaymentDto.setFinancialContract(financialContract);
            supplierPaymentDto.setPurchaseOrder(null);
        } else if (supplierPaymentDto.getTypeClient().equals("po") ) {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findAllBy_id(supplierPaymentDto.getPurchaseOrder().get_id());
            supplierPaymentDto.setPurchaseOrder(purchaseOrder);
            supplierPaymentDto.setFinancialContract(null);
        }

        Currency currency = currencyRepository.findAllBy_id(supplierPaymentDto.getCurrency().get_id());
        supplierPaymentDto.setCurrency(currency);
        Client client = clientRepository.findBy_id(supplierPaymentDto.getClient().get_id());
        supplierPaymentDto.setClient(client);
        supplierPaymentRepository.save(supplierPaymentMapper.dtoToModel(supplierPaymentDto));
        logService.addLog(LogType.CREATE, ClassType.supplierPayment,"create supplier payment code "+supplierPaymentDto.getCodeSupplier());
    }

    @Override
    public List<SupplierPaymentDto> getAllSupplierPayment() {
        // Get all actions
        List<SupplierPayment> supplierPayment = supplierPaymentRepository.findAll();
        // Create a list of all actions dto
        ArrayList<SupplierPaymentDto> supplierPaymentDtos = new ArrayList<>();

        for (SupplierPayment supplierPayment1 : supplierPayment) {
            SupplierPaymentDto supplierPaymentDto = supplierPaymentMapper.modelToDto(supplierPayment1);
            supplierPaymentDtos.add(supplierPaymentDto);
        }
        return supplierPaymentDtos;
    }

    @Override
    public SupplierPayment getById(String id) {
        return supplierPaymentRepository.findAllBy_id(id);
    }

    @Override
    public List<SupplierPaymentDto> updateSupplierPayment(SupplierPaymentDto supplierPaymentDto, String id) {
        // save country if note existe
        SupplierPayment supplierPayment = getById(id);
        Optional<SupplierPayment> supplierPayment1 = Optional.ofNullable(supplierPaymentRepository.findAllBy_id(id));

        if (!supplierPayment1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
        if (supplierPaymentDto.getType().equals("internal") ) {
            FinancialCompany financialCompany = financialCompanyRepository.findAllBy_id(supplierPaymentDto.getFinancialCompany().get_id());
            supplierPayment.setFinancialCompany(financialCompany);
            supplierPayment.setExternalSupplier(null);
        } else if (supplierPaymentDto.getType().equals("external") ) {
            ExternalSupplier externalSupplier = externalSupplierRepository.findAllBy_id(supplierPaymentDto.getExternalSupplier().get_id());
            supplierPayment.setExternalSupplier(externalSupplier);
            supplierPayment.setFinancialCompany(null);
        }
        if (supplierPaymentDto.getTypeClient().equals("contract") ) {
            FinancialContract financialContract = financialContractRepository.findAllBy_id(supplierPaymentDto.getFinancialContract().get_id());
            supplierPayment.setFinancialContract(financialContract);
            supplierPayment.setPurchaseOrder(null);
        } else if (supplierPaymentDto.getTypeClient().equals("po") ) {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findAllBy_id(supplierPaymentDto.getPurchaseOrder().get_id());
            supplierPayment.setPurchaseOrder(purchaseOrder);
            supplierPayment.setFinancialContract(null);
        }

        Client client = clientRepository.findBy_id(supplierPaymentDto.getClient().get_id());
        supplierPayment.setClient(client);
        Currency currency = currencyRepository.findAllBy_id(supplierPaymentDto.getCurrency().get_id());
        supplierPayment.setCurrency(currency);

        supplierPayment.setCodeSupplier(supplierPaymentDto.getCodeSupplier());
        supplierPayment.setSupplierBill(supplierPaymentDto.getSupplierBill());
        supplierPayment.setType(supplierPaymentDto.getType());
        supplierPayment.setTypeClient(supplierPaymentDto.getTypeClient());
        supplierPayment.setPaymentDate(supplierPaymentDto.getPaymentDate());
        supplierPayment.setReelPaymentDate(supplierPaymentDto.getReelPaymentDate());
        supplierPaymentRepository.save(supplierPayment);
        logService.addLog(LogType.UPDATE, ClassType.supplierPayment,"update supplier payment code "+supplierPayment.getCodeSupplier());
        return getAllSupplierPayment();
    }

    @Override
    public List<SupplierPaymentDto> remove(String id) {
        Optional<SupplierPayment> action = Optional.ofNullable(supplierPaymentRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        supplierPaymentRepository.deleteById(id);
        logService.addLog(LogType.DELETE, ClassType.supplierPayment,"delete supplier payment code "+action.get().getCodeSupplier());
        return getAllSupplierPayment();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.SupplierPayment, exceptionType, args);
    }
}
