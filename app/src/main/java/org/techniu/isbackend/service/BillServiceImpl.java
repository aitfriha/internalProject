package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.BillMapper;
import org.techniu.isbackend.dto.model.BillDto;
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
public class BillServiceImpl implements BillService {
    private BillRepository billRepository;
    private ClientRepository clientRepository;
    private FinancialCompanyRepository financialCompanyRepository;
    private CommercialOperationRepository commercialOperationRepository;
    private FinancialContractRepository financialContractRepository;
    private IvaRepository ivaRepository;
    private CurrencyRepository currencyRepository;
    private LogService logService;
    private final BillMapper billMapper = Mappers.getMapper(BillMapper.class);

    public BillServiceImpl(BillRepository billRepository, AddressService addressService, CommercialOperationRepository commercialOperationRepository,
                           ClientRepository clientRepository, FinancialCompanyRepository financialCompanyRepository, IvaRepository ivaRepository,
                           FinancialContractRepository financialContractRepository, CurrencyRepository currencyRepository, LogService logService) {
        this.billRepository = billRepository;
        this.commercialOperationRepository = commercialOperationRepository;
        this.financialCompanyRepository = financialCompanyRepository;
        this.currencyRepository = currencyRepository;
        this.financialContractRepository = financialContractRepository;
        this.clientRepository = clientRepository;
        this.ivaRepository = ivaRepository;
        this.logService = logService;
    }

    @Override
    public void saveBill(BillDto billDto) {
        billRepository.save(billMapper.dtoToModel(billDto));
        logService.addLog(LogType.CREATE, ClassType.Bill,"create Bill code "+billDto.getCode());
    }

    @Override
    public List<BillDto> getAllBill() {
        // Get all actions
        List<Bill> bill = billRepository.findAll();
        // Create a list of all actions dto
        ArrayList<BillDto> billDtos = new ArrayList<>();

        for (Bill bill1 : bill) {
            BillDto billDto = billMapper.modelToDto(bill1);
            billDtos.add(billDto);
        }
        return billDtos;
    }

    @Override
    public Bill getById(String id) {
        return billRepository.findAllBy_id(id);
    }

    @Override
    public List<BillDto> updateBill(BillDto billDto, String id) {

        Bill bill = getById(id);
        Optional<Bill> bill1 = Optional.ofNullable(billRepository.findAllBy_id(id));

        if (!bill1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        Client client = clientRepository.findBy_id(billDto.getClient().get_id());
        Client clientSigned = clientRepository.findBy_id(billDto.getClientSigned().get_id());
        FinancialCompany financialCompany = financialCompanyRepository.findAllBy_id(billDto.getFinancialCompany().get_id());
        CommercialOperation commercialOperation = commercialOperationRepository.findBy_id(billDto.getCommercialOperation().get_id());
        FinancialContract financialContract = financialContractRepository.findAllBy_id(billDto.getFinancialContract().get_id());
        Iva iva = ivaRepository.findAllBy_id(billDto.getIva().get_id());
        Currency currency = currencyRepository.findAllBy_id(billDto.getCurrency().get_id());

        bill.setClient(client);
        bill.setClientSigned(clientSigned);
        bill.setCurrency(currency);
        bill.setCommercialOperation(commercialOperation);
        bill.setFinancialCompany(financialCompany);
        bill.setFinancialContract(financialContract);
        bill.setIva(iva);

        bill.setCode(billDto.getCode());
        bill.setPurchaseOrderNumber(billDto.getPurchaseOrderNumber());
        bill.setTotalLocal(billDto.getTotalLocal());
        bill.setTotalEuro(billDto.getTotalEuro());
        bill.setValueIVALocal(billDto.getValueIVALocal());
        bill.setValueIVAEuro(billDto.getValueIVAEuro());
        bill.setTotalAmountLocal(billDto.getTotalAmountLocal());
        bill.setTotalAmountEuro(billDto.getTotalAmountEuro());
        bill.setDesc(billDto.getDesc());
        bill.setDescTotalUSD(billDto.getDescTotalUSD());
        bill.setDelayDate(billDto.getDelayDate());
        bill.setRegisterDate(billDto.getRegisterDate());
        bill.setPaymentDate(billDto.getPaymentDate());
        bill.setReelPaymentDay(billDto.getReelPaymentDay());
        bill.setReelPaymentDays(billDto.getReelPaymentDays());
        bill.setPaymentsBDDay(billDto.getPaymentsBDDay());
        bill.setPaymentDone(billDto.isPaymentDone());
        bill.setNbrConcepts(billDto.getNbrConcepts());

        billRepository.save(bill);
        logService.addLog(LogType.UPDATE, ClassType.Bill,"update Bill code "+bill.getCode());
        return getAllBill();
    }

    @Override
    public List<BillDto> remove(String id) {
        Optional<Bill> action = Optional.ofNullable(billRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        billRepository.deleteById(id);
        logService.addLog(LogType.DELETE, ClassType.Bill,"delete Bill code "+action.get().getCode());
        return getAllBill();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Bill, exceptionType, args);
    }
}
