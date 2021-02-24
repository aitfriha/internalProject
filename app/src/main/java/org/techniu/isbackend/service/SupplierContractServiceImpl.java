package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.SupplierContractMapper;
import org.techniu.isbackend.dto.model.SupplierContractDto;
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
public class SupplierContractServiceImpl implements SupplierContractService {
    private SupplierContractRepository supplierContractRepository;
    private FinancialCompanyRepository financialCompanyRepository;
    private ExternalSupplierRepository externalSupplierRepository;
    private ClientRepository clientRepository;
    private FinancialContractRepository financialContractRepository;
    private PurchaseOrderRepository purchaseOrderRepository;
    private CurrencyRepository currencyRepository;
    private static int code = 0;
    private final SupplierContractMapper supplierContractMapper = Mappers.getMapper(SupplierContractMapper.class);

    public SupplierContractServiceImpl(SupplierContractRepository supplierContractRepository, CurrencyRepository currencyRepository,
                                       FinancialCompanyRepository financialCompanyRepository, ClientRepository clientRepository,
                                       ExternalSupplierRepository externalSupplierRepository, FinancialContractRepository financialContractRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.supplierContractRepository = supplierContractRepository;
        this.financialCompanyRepository = financialCompanyRepository;
        this.externalSupplierRepository = externalSupplierRepository;
        this.clientRepository = clientRepository;
        this.financialContractRepository = financialContractRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void saveSupplierContract(SupplierContractDto supplierContractDto) {
        code++;
        System.out.println("Implement part :" + supplierContractDto);

        if (supplierContractDto.getType().equals("internal") ) {
            FinancialCompany financialCompany = financialCompanyRepository.findAllBy_id(supplierContractDto.getFinancialCompany().get_id());
            supplierContractDto.setFinancialCompany(financialCompany);
            supplierContractDto.setExternalSupplier(null);
        } else if (supplierContractDto.getType().equals("external") ) {
            ExternalSupplier externalSupplier = externalSupplierRepository.findAllBy_id(supplierContractDto.getExternalSupplier().get_id());
            supplierContractDto.setExternalSupplier(externalSupplier);
            supplierContractDto.setFinancialCompany(null);
        }
        if (supplierContractDto.getTypeClient().equals("contract") ) {
            FinancialContract financialContract = financialContractRepository.findAllBy_id(supplierContractDto.getFinancialContract().get_id());
            supplierContractDto.setFinancialContract(financialContract);
            supplierContractDto.setPurchaseOrder(null);
        } else if (supplierContractDto.getTypeClient().equals("po") ) {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findAllBy_id(supplierContractDto.getPurchaseOrder().get_id());
            supplierContractDto.setPurchaseOrder(purchaseOrder);
            supplierContractDto.setFinancialContract(null);
        }
        System.out.println("CODE :" + supplierContractDto.getCodeSupplier().concat(String.valueOf(code)));
        supplierContractDto.setCodeContract(supplierContractDto.getCodeSupplier().concat(String.valueOf(code)));
        Currency currency = currencyRepository.findAllBy_id(supplierContractDto.getCurrency().get_id());
        supplierContractDto.setCurrency(currency);
        Client client = clientRepository.findBy_id(supplierContractDto.getClient().get_id());
        supplierContractDto.setClient(client);
        supplierContractRepository.save(supplierContractMapper.dtoToModel(supplierContractDto));
    }

    @Override
    public List<SupplierContractDto> getAllSupplierContract() {
        // Get all actions
        List<SupplierContract> supplierContract = supplierContractRepository.findAll();
        // Create a list of all actions dto
        ArrayList<SupplierContractDto> supplierContractDtos = new ArrayList<>();

        for (SupplierContract supplierContract1 : supplierContract) {
            SupplierContractDto supplierContractDto = supplierContractMapper.modelToDto(supplierContract1);
            supplierContractDtos.add(supplierContractDto);
        }
        return supplierContractDtos;
    }

    @Override
    public SupplierContract getById(String id) {
        return supplierContractRepository.findAllBy_id(id);
    }

    @Override
    public List<SupplierContractDto> updateSupplierContract(SupplierContractDto supplierContractDto, String id) {
        // save country if note existe
        SupplierContract supplierContract = getById(id);
        Optional<SupplierContract> supplierContract1 = Optional.ofNullable(supplierContractRepository.findAllBy_id(id));

        if (!supplierContract1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        System.out.println(supplierContract);

        if (supplierContractDto.getType().equals("internal") ) {
            FinancialCompany financialCompany = financialCompanyRepository.findAllBy_id(supplierContractDto.getFinancialCompany().get_id());
            supplierContract.setFinancialCompany(financialCompany);
            supplierContract.setExternalSupplier(null);
        } else if (supplierContractDto.getType().equals("external") ) {
            ExternalSupplier externalSupplier = externalSupplierRepository.findAllBy_id(supplierContractDto.getExternalSupplier().get_id());
            supplierContract.setExternalSupplier(externalSupplier);
            supplierContract.setFinancialCompany(null);
        }
        if (supplierContractDto.getTypeClient().equals("contract") ) {
            FinancialContract financialContract = financialContractRepository.findAllBy_id(supplierContractDto.getFinancialContract().get_id());
            supplierContract.setFinancialContract(financialContract);
            supplierContract.setPurchaseOrder(null);
        } else if (supplierContractDto.getTypeClient().equals("po") ) {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findAllBy_id(supplierContractDto.getPurchaseOrder().get_id());
            supplierContract.setPurchaseOrder(purchaseOrder);
            supplierContract.setFinancialContract(null);
        }

        Currency currency = currencyRepository.findAllBy_id(supplierContractDto.getCurrency().get_id());
        supplierContract.setCurrency(currency);
        Client client = clientRepository.findBy_id(supplierContractDto.getClient().get_id());
        supplierContract.setClient(client);

        supplierContract.setName(supplierContractDto.getName());
        supplierContract.setCodeSupplier(supplierContractDto.getCodeSupplier());
        supplierContract.setDocument(supplierContractDto.getDocument());
        supplierContract.setType(supplierContractDto.getType());
        supplierContract.setTypeClient(supplierContractDto.getTypeClient());
        supplierContract.setContractTradeVolume(supplierContractDto.getContractTradeVolume());
        supplierContract.setChangeFactor(supplierContractDto.getChangeFactor());
        supplierContract.setContractTradeVolumeEuro(supplierContractDto.getContractTradeVolumeEuro());

        System.out.println(supplierContract);

        supplierContractRepository.save(supplierContract);
        return getAllSupplierContract();
    }

    @Override
    public List<SupplierContractDto> remove(String id) {
        Optional<SupplierContract> action = Optional.ofNullable(supplierContractRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        supplierContractRepository.deleteById(id);
        return getAllSupplierContract();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.SupplierContract, exceptionType, args);
    }
}
