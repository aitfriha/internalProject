package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.StaffContractMapper;
import org.techniu.isbackend.dto.mapper.StaffMapper;
import org.techniu.isbackend.dto.model.StaffContractDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
@Transactional
public class StaffContractServiceImpl implements StaffContractService {

    private StaffContractRepository staffContractRepository;
    private ContractTypeRepository contractTypeRepository;
    private LegalCategoryTypeRepository legalCategoryTypeRepository;
    private ContractModelRepository contractModelRepository;
    private StaffContractHistoryRepository staffContractHistoryRepository;
    private FinancialCompanyRepository financialCompanyRepository;

    private final StaffContractMapper staffContractMapper = Mappers.getMapper(StaffContractMapper.class);

    StaffContractServiceImpl(
            StaffContractRepository staffContractRepository,
            ContractTypeRepository contractTypeRepository ,
            LegalCategoryTypeRepository legalCategoryTypeRepository,
            ContractModelRepository contractModelRepository,
            StaffContractHistoryRepository staffContractHistoryRepository,
            FinancialCompanyRepository financialCompanyRepository) {
        this.staffContractRepository = staffContractRepository;
        this.contractTypeRepository = contractTypeRepository;
        this.legalCategoryTypeRepository = legalCategoryTypeRepository;
        this.contractModelRepository = contractModelRepository;
        this.staffContractHistoryRepository = staffContractHistoryRepository;
        this.financialCompanyRepository = financialCompanyRepository;
    }

    @Override
    public void update(StaffContractDto staffContractDto) {
        if(
                        staffContractDto.getTownContract().equals("")
                        || staffContractDto.getAssociateOffice().equals("")
                        || staffContractDto.getContractTypeId().equals("")
                        || staffContractDto.getLegalCategoryTypeId().equals("")
                        || staffContractDto.getPersonalNumber().equals("")
        ) {
            throw exception(FILL_ALL_NECESSARY_FIELDS);
        }

        Optional<StaffContract> staffContract2= Optional.ofNullable(staffContractRepository.findBy_id(staffContractDto.getStaffContractId()));
        /*if (staffContract2.isPresent()) {
            throw exception(STAFF_PERSONAL_NUMBER_EXIST);
        }*/
        Optional<StaffContract> staffContract3 = Optional.ofNullable(staffContractRepository.findByPersonalNumber(staffContractDto.getPersonalNumber()));
        if (staffContract3.isPresent() && !(staffContract2.get().getPersonalNumber().equals(staffContractDto.getPersonalNumber())) ) {
            throw exception(STAFF_PERSONAL_NUMBER_EXIST);
        }

        StaffContract staffContract = staffContractMapper.dtoToModel(staffContractDto);
        StaffContract staffContract1 = staffContractRepository.findById(staffContractDto.getStaffContractId()).get();
        if(staffContractDto.getContractDoc() == null && staffContract1.getContractDoc() != null)  {
            staffContract.setContractDoc(staffContract1.getContractDoc());
        } else staffContract.setContractDoc(staffContractDto.getContractDoc());
        if(staffContractDto.getInternalRulesDoc() == null  && staffContract1.getInternalRulesDoc() != null)  {
            staffContract.setInternalRulesDoc(staffContract1.getInternalRulesDoc());
        } else staffContract.setInternalRulesDoc(staffContractDto.getInternalRulesDoc());
        if(staffContractDto.getPreContractDoc() == null  && staffContract1.getPreContractDoc() != null)  {
            staffContract.setPreContractDoc(staffContract1.getPreContractDoc());
        } else staffContract.setPreContractDoc(staffContractDto.getPreContractDoc());
        staffContract.setContractType(contractTypeRepository.findById(staffContractDto.getContractTypeId()).get());
        staffContract.setLegalCategoryType(legalCategoryTypeRepository.findById(staffContractDto.getLegalCategoryTypeId()).get());
        staffContract.setCompany(financialCompanyRepository.findById(staffContractDto.getCompanyId()).get());
        staffContract.setContractModel(contractModelRepository.findById(staffContractDto.getContractModelId()).get());
        StaffContractHistory staffContractHistory = new StaffContractHistory();
        staffContractHistory.setStaffContract(staffContract1);
        staffContractHistory.setStaffContractHistory(staffContractRepository.save(staffContract));
        staffContractHistory.setUpdatedAt(staffContractDto.getUpdatedAt());
        staffContractHistoryRepository.save(staffContractHistory);
    }

    @Override
    public List<StaffContract> getAllByContractType(String contractTypeId) {
        ContractType contractType = contractTypeRepository.findById(contractTypeId).get();
        return staffContractRepository.findAllByContractType(contractType);
    }

    @Override
    public List<StaffContract> getAllByLegalCategoryType(String legalCategoryTypeId) {
        LegalCategoryType legalCategoryType = legalCategoryTypeRepository.findById(legalCategoryTypeId).get();
        return staffContractRepository.findAllByLegalCategoryType(legalCategoryType);
    }

    @Override
    public List<StaffContract> getAllByContractModel(String contractModelId) {
        ContractModel contractModel = contractModelRepository.findById(contractModelId).get();
        return staffContractRepository.findAllByContractModel(contractModel);
    }

    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.StaffContract, exceptionType, args);
    }
}
