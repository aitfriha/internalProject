package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.LegalCategoryTypeMapper;
import org.techniu.isbackend.dto.model.LegalCategoryTypeDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.LegalCategoryTypeRepository;
import org.techniu.isbackend.repository.FinancialCompanyRepository;
import org.techniu.isbackend.repository.StaffContractHistoryRepository;
import org.techniu.isbackend.repository.StaffContractRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;

@Service
@Transactional
public class LegalCategoryTypeServiceImpl implements LegalCategoryTypeService {

    private LegalCategoryTypeRepository legalCategoryTypeRepository;
    private FinancialCompanyRepository financialCompanyRepository;
    private StaffContractRepository staffContractRepository;
    private StaffContractHistoryRepository staffContractHistoryRepository;
    private LogService logService;
    private final LegalCategoryTypeMapper legalCategoryTypeMapper = Mappers.getMapper(LegalCategoryTypeMapper.class);

    LegalCategoryTypeServiceImpl(
            LegalCategoryTypeRepository legalCategoryTypeRepository,
            FinancialCompanyRepository financialCompanyRepository,
            StaffContractRepository staffContractRepository,
            StaffContractHistoryRepository staffContractHistoryRepository, LogService logService) {
        this.legalCategoryTypeRepository = legalCategoryTypeRepository;
        this.financialCompanyRepository = financialCompanyRepository;
        this.staffContractRepository = staffContractRepository;
        this.staffContractHistoryRepository = staffContractHistoryRepository;
        this.logService = logService;
    }

    @Override
    public void save(LegalCategoryTypeDto legalCategoryTypeDto) {
        FinancialCompany financialCompany = financialCompanyRepository.findById(legalCategoryTypeDto.getFinancialCompanyId()).get();
        LegalCategoryType legalCategoryType = legalCategoryTypeMapper.dtoToModel(legalCategoryTypeDto);

        Optional<LegalCategoryType>  legalCategoryType1= Optional.ofNullable(legalCategoryTypeRepository.findByNameAndCompany(legalCategoryTypeDto.getName(), financialCompany));
        if (legalCategoryType1.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }

        legalCategoryType.setCompany(financialCompany);
        legalCategoryTypeRepository.save(legalCategoryType);
        logService.addLog(LogType.CREATE, ClassType.LEGALCATEGORYTYPE,"create legal category type "+legalCategoryTypeDto.getName());
    }

    @Override
    public void update(LegalCategoryTypeDto legalCategoryTypeDto) {
        LegalCategoryType legalCategoryType = legalCategoryTypeRepository.findById(legalCategoryTypeDto.getLegalCategoryTypeId()).get();

        Optional<LegalCategoryType>  legalCategoryType1= Optional.ofNullable(legalCategoryTypeRepository.findByNameAndCompany(legalCategoryTypeDto.getName(), legalCategoryType.getCompany()));
        if (legalCategoryType1.isPresent()) {
            if(!legalCategoryType1.get().get_id().equals(legalCategoryTypeDto.getLegalCategoryTypeId())) {
                throw exception(DUPLICATE_ENTITY);
            }
        }

        legalCategoryType.setName(legalCategoryTypeDto.getName());
        legalCategoryType.setFunctions(legalCategoryTypeDto.getFunctions());
        legalCategoryTypeRepository.save(legalCategoryType);
        logService.addLog(LogType.UPDATE, ClassType.LEGALCATEGORYTYPE,"update legal category type "+legalCategoryTypeDto.getName());
    }

    @Override
    public void remove(String oldId, String newId) {

        Optional<LegalCategoryType> action = Optional.ofNullable(legalCategoryTypeRepository.findBy_id(oldId));
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        if(!newId.equals("")){
            List<StaffContract> staffContracts = staffContractRepository.findAllByLegalCategoryType(legalCategoryTypeRepository.findById(oldId).get());
            LegalCategoryType newLegalCategoryType = legalCategoryTypeRepository.findById(newId).get();
            staffContracts.forEach( contract -> {
                contract.setLegalCategoryType(newLegalCategoryType);
                StaffContractHistory staffContractHistory = new StaffContractHistory();
                staffContractHistory.setStaffContract(contract);
                staffContractHistory.setStaffContractHistory(staffContractRepository.save(contract));
                staffContractHistory.setUpdatedAt(new Date().toInstant().toString().substring(0,10));
                staffContractHistoryRepository.save(staffContractHistory);
            });
        }
        List<StaffContractHistory> staffContractHistories = staffContractHistoryRepository.findAll();

            staffContractHistories.forEach(staffContractHistory -> {
                if(staffContractHistory.getStaffContractHistory().getLegalCategoryType()!=null) {
                if (staffContractHistory.getStaffContractHistory().getLegalCategoryType().get_id().equals(oldId)) {
                    staffContractHistoryRepository.delete(staffContractHistory);
                }}
            });

        logService.addLog(LogType.DELETE, ClassType.LEGALCATEGORYTYPE,"delete legal category type "+action.get().getName());
        legalCategoryTypeRepository.deleteById(oldId);
    }

    @Override
    public List<LegalCategoryTypeDto> getAll() {

        List<LegalCategoryType> legalCategoryTypes = legalCategoryTypeRepository.findAll();
        // Create a list of all actions dto
        List<LegalCategoryTypeDto> legalCategoryTypeDtos = new ArrayList<>();

        for (LegalCategoryType legalCategoryType : legalCategoryTypes) {
            LegalCategoryTypeDto legalCategoryTypeDto=legalCategoryTypeMapper.modelToDto(legalCategoryType);
            legalCategoryTypeDto.setFinancialCompanyId(legalCategoryType.getCompany().get_id());
            legalCategoryTypeDto.setCompanyName(legalCategoryType.getCompany().getName());
            legalCategoryTypeDtos.add(legalCategoryTypeDto);
        }
        return legalCategoryTypeDtos;
    }

    @Override
    public List<LegalCategoryTypeDto> getAllByCompany(String companyId) {
        FinancialCompany financialCompany = financialCompanyRepository.findById(companyId).get();

        List<LegalCategoryType> legalCategoryTypes = legalCategoryTypeRepository.getAllByCompany(financialCompany);
        // Create a list of all actions dto
        List<LegalCategoryTypeDto> legalCategoryTypeDtos = new ArrayList<>();

        for (LegalCategoryType legalCategoryType : legalCategoryTypes) {
            LegalCategoryTypeDto legalCategoryTypeDto=legalCategoryTypeMapper.modelToDto(legalCategoryType);
            legalCategoryTypeDto.setFinancialCompanyId(legalCategoryType.getCompany().get_id());
            legalCategoryTypeDto.setCompanyName(legalCategoryType.getCompany().getName());
            legalCategoryTypeDtos.add(legalCategoryTypeDto);
        }
        return legalCategoryTypeDtos;
    }


    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.LegalCategoryType, exceptionType, args);
    }
}
