package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.FinancialContractMapper;
import org.techniu.isbackend.dto.model.FinancialContractDto;
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
public class FinancialContractServiceImpl implements FinancialContractService {
    private FinancialContractRepository financialContractRepository;
    private CityRepository cityRepository;
    private AddressRepository addressRepository;
    private AddressService addressService;
    private ClientRepository clientRepository;
    private LogService logService;
    private FinancialCompanyRepository financialCompanyRepository;
    private CommercialOperationRepository commercialOperationRepository;
    private ContractStatusRepository contractStatusRepository;
    private FunctionalStructureLevelRepository functionalStructureLevelRepository;
    private CurrencyRepository currencyRepository;
    private final FinancialContractMapper financialContractMapper = Mappers.getMapper(FinancialContractMapper.class);

    public FinancialContractServiceImpl(FinancialContractRepository financialContractRepository, AddressService addressService, CommercialOperationRepository commercialOperationRepository,
                                        AddressRepository addressRepository, CityRepository cityRepository, ClientRepository clientRepository, LogService logService, FinancialCompanyRepository financialCompanyRepository,
                                        ContractStatusRepository contractStatusRepository, FunctionalStructureLevelRepository functionalStructureLevelRepository, CurrencyRepository currencyRepository) {
        this.financialContractRepository = financialContractRepository;
        this.cityRepository = cityRepository ;
        this.addressRepository = addressRepository;
        this.addressService = addressService;
        this.commercialOperationRepository = commercialOperationRepository;
        this.logService = logService;
        this.financialCompanyRepository = financialCompanyRepository;
        this.currencyRepository = currencyRepository;
        this.contractStatusRepository = contractStatusRepository;
        this.functionalStructureLevelRepository = functionalStructureLevelRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void saveFinancialContract(FinancialContractDto financialContractDto) {
        financialContractRepository.save(financialContractMapper.dtoToModel(financialContractDto));
        logService.addLog(LogType.CREATE, ClassType.FinancialContract,"create financial contract status "+financialContractDto.getContractTitle());
    }

    @Override
    public List<FinancialContractDto> getAllFinancialContract() {
        // Get all actions
        List<FinancialContract> financialContract = financialContractRepository.findAll();
        // Create a list of all actions dto
        ArrayList<FinancialContractDto> financialContractDtos = new ArrayList<>();

        for (FinancialContract financialContract1 : financialContract) {
            FinancialContractDto financialContractDto = financialContractMapper.modelToDto(financialContract1);
            financialContractDtos.add(financialContractDto);
        }
        return financialContractDtos;
    }

    @Override
    public FinancialContract getById(String id) {
        return financialContractRepository.findAllBy_id(id);
    }

    @Override
    public List<FinancialContractDto> updateFinancialContract(FinancialContractDto financialContractDto, String id) {

        FinancialContract financialContract = getById(id);
        Optional<FinancialContract> financialContract1 = Optional.ofNullable(financialContractRepository.findAllBy_id(id));

        if (!financialContract1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        City city = cityRepository.findCityBy_id(financialContractDto.getAddress().getCity().get_id());
        Address address = addressRepository.findAddressByAddressId(financialContractDto.getAddress().getAddressId());
        address.setCity(city);
        addressService.updateAddress(financialContractDto.getAddress().getAddressId(), address);

        Client client = clientRepository.findBy_id(financialContractDto.getClient().get_id());
        FinancialCompany financialCompany = financialCompanyRepository.findAllBy_id(financialContractDto.getFinancialCompany().get_id());
        CommercialOperation commercialOperation = commercialOperationRepository.findBy_id(financialContractDto.getCommercialOperation().get_id());
        ContractStatus contractStatus = contractStatusRepository.findAllBy_id(financialContractDto.getContractStatus().get_id());
        FunctionalStructureLevel functionalStructureLevel = functionalStructureLevelRepository.findBy_id(financialContractDto.getFunctionalStructureLevel().get_id());
        Currency currency = currencyRepository.findAllBy_id(financialContractDto.getCurrency().get_id());

        financialContract.setAddress(address);
        financialContract.setClient(client);
        financialContract.setContractStatus(contractStatus);
        financialContract.setFunctionalStructureLevel(functionalStructureLevel);
        financialContract.setCurrency(currency);
        financialContract.setCommercialOperation(commercialOperation);
        financialContract.setFinancialCompany(financialCompany);

        financialContract.setContractTitle(financialContractDto.getContractTitle());
        financialContract.setTaxeIdentityNumber(financialContractDto.getTaxeIdentityNumber());
        financialContract.setSignedDate(financialContractDto.getSignedDate());
        financialContract.setStartDate(financialContractDto.getStartDate());
        financialContract.setEndDate(financialContractDto.getEndDate());
        financialContract.setFinalReelDate(financialContractDto.getFinalReelDate());
        financialContract.setFirstDayInsured(financialContractDto.getFirstDayInsured());
        financialContract.setLastDayInsured(financialContractDto.getLastDayInsured());
        financialContract.setContractTradeVolume(financialContractDto.getContractTradeVolume());
        financialContract.setContractTradeVolumeEuro(financialContractDto.getContractTradeVolumeEuro());
        financialContract.setConceptTotalAmount(financialContractDto.getConceptTotalAmount());
        financialContract.setConceptTotalAmountEuro(financialContractDto.getConceptTotalAmountEuro());
        financialContract.setAmountInsured(financialContractDto.getAmountInsured());
        financialContract.setAmountInsuredEuro(financialContractDto.getAmountInsuredEuro());
        financialContract.setPenaltyMaxType(financialContractDto.getPenaltyMaxType());
        financialContract.setPenaltyMaxValue(financialContractDto.getPenaltyMaxValue());
        financialContract.setPaymentsBDDays(financialContractDto.getPaymentsBDDays());

        financialContract.setContractDocumentation(financialContractDto.getContractDocumentation());
        financialContract.setContractDocDescreption(financialContractDto.getContractDocDescreption());
        financialContract.setInsureDocumentation(financialContractDto.getInsureDocumentation());
        financialContract.setPurchaseOrderDocumentation(financialContractDto.getPurchaseOrderDocumentation());
        financialContract.setProposalDocumentation(financialContractDto.getProposalDocumentation());
        financialContract.setProposalDocumentationDuo(financialContractDto.getProposalDocumentationDuo());

        financialContract.setConceptType(financialContractDto.getConceptType());
        financialContract.setConceptValue(financialContractDto.getConceptValue());
        financialContract.setConceptValueEuro(financialContractDto.getConceptValueEuro());
        financialContract.setConceptValueLocal(financialContractDto.getConceptValueLocal());

        financialContract.setPenaltiesListe(financialContractDto.getPenaltiesListe());
        financialContract.setPurchaseOrders(financialContractDto.getPurchaseOrders());
        financialContract.setInsureDocumentations(financialContractDto.getInsureDocumentations());
        financialContract.setContractDocumentations(financialContractDto.getContractDocumentations());
        financialContract.setNbrConcepts(financialContractDto.getNbrConcepts());

        financialContract.setPurchaseOrderNumber(financialContractDto.getPurchaseOrderNumber());

        financialContract.setPenaltyQuantity(financialContractDto.getPenaltyQuantity());
        financialContract.setPenaltyCost(financialContractDto.getPenaltyCost());
        financialContract.setPenaltyValue(financialContractDto.getPenaltyValue());
        financialContract.setPenaltyPer(financialContractDto.getPenaltyPer());

        financialContractRepository.save(financialContract);
        logService.addLog(LogType.UPDATE, ClassType.FinancialContract,"update financial contract status "+financialContract.getContractTitle());
        return getAllFinancialContract();
    }

    @Override
    public List<FinancialContractDto> remove(String id) {
        Optional<FinancialContract> action = Optional.ofNullable(financialContractRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        logService.addLog(LogType.DELETE, ClassType.FinancialContract,"delete financial contract status "+action.get().getContractTitle());
        financialContractRepository.deleteById(id);
        return getAllFinancialContract();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.FinancialContract, exceptionType, args);
    }
}
