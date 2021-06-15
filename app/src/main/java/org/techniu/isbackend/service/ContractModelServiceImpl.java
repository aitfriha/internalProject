package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.ContractModelMapper;
import org.techniu.isbackend.dto.model.ContractModelDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.ContractModelRepository;
import org.techniu.isbackend.repository.StaffContractHistoryRepository;
import org.techniu.isbackend.repository.StaffContractRepository;
import org.techniu.isbackend.repository.StateCountryRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
@Transactional
public class ContractModelServiceImpl implements ContractModelService {

    private ContractModelRepository contractModelRepository;
    private StateCountryRepository stateCountryRepository;
    private StaffContractRepository staffContractRepository;
    private StaffContractHistoryRepository staffContractHistoryRepository;
    private LogService logService;
    private final ContractModelMapper contractModelMapper = Mappers.getMapper(ContractModelMapper.class);

    ContractModelServiceImpl(
            ContractModelRepository contractModelRepository,
            StateCountryRepository stateCountryRepository,
            StaffContractRepository staffContractRepository,
            StaffContractHistoryRepository staffContractHistoryRepository, LogService logService) {
        this.contractModelRepository = contractModelRepository;
        this.stateCountryRepository = stateCountryRepository;
        this.staffContractRepository = staffContractRepository;
        this.staffContractHistoryRepository = staffContractHistoryRepository;
        this.logService = logService;
    }

    @Override
    public void save(ContractModelDto contractModelDto) {
        ContractModel contractModel = contractModelMapper.dtoToModel(contractModelDto);

        if (contractModelDto.getCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        Optional<ContractModel>  contractModel1= Optional.ofNullable(contractModelRepository.findByName(contractModelDto.getName()));
        if (contractModel1.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        Optional<ContractModel>  contractModel2= Optional.ofNullable(contractModelRepository.findByCode(contractModelDto.getCode()));
        if (contractModel2.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }

        contractModelRepository.save(contractModel);
        logService.addLog(LogType.CREATE, ClassType.CONTRACTMODEL,"create contact model "+contractModelDto.getName());
    }

    @Override
    public void update(ContractModelDto contractModelDto) {
        ContractModel contractModel = contractModelRepository.findById(contractModelDto.getContractModelId()).get();
        if (contractModelDto.getCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }

        Optional<ContractModel>  contractModel1= Optional.ofNullable(contractModelRepository.findByName(contractModelDto.getName()));

        if (contractModel1.isPresent()) {
            if(!contractModel1.get().get_id().equals(contractModelDto.getContractModelId())) {
                throw exception(DUPLICATE_ENTITY);
            }
        }
        Optional<ContractModel>  contractModel2= Optional.ofNullable(contractModelRepository.findByCode(contractModelDto.getCode()));
        if (contractModel2.isPresent()) {
            if(!contractModel2.get().get_id().equals(contractModelDto.getContractModelId())) {
                throw exception(DUPLICATE_ENTITY);
            }
        }

        contractModel.setCode(contractModelDto.getCode());
        contractModel.setName(contractModelDto.getName());
        contractModel.setDescription(contractModelDto.getDescription());

        contractModelRepository.save(contractModel);
        logService.addLog(LogType.UPDATE, ClassType.CONTRACTMODEL,"update contact model "+contractModel.getName());
    }

    @Override
    public void remove(String oldId, String newId) {
        Optional<ContractModel> action = Optional.ofNullable(contractModelRepository.findById(oldId).get());
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        if(!newId.equals("")){
            List<StaffContract> staffContracts = staffContractRepository.findAllByContractModel(contractModelRepository.findById(oldId).get());
            ContractModel newContractModel = contractModelRepository.findById(newId).get();
            staffContracts.forEach( contract -> {
                contract.setContractModel(newContractModel);
                StaffContractHistory staffContractHistory = new StaffContractHistory();
                staffContractHistory.setStaffContract(contract);
                staffContractHistory.setStaffContractHistory(staffContractRepository.save(contract));
                staffContractHistory.setUpdatedAt(new Date().toInstant().toString().substring(0,10));
                staffContractHistoryRepository.save(staffContractHistory);
            });
        }
        List<StaffContractHistory> staffContractHistories = staffContractHistoryRepository.findAll();
        if(staffContractHistories.size()>0) {
            staffContractHistories.forEach(staffContractHistory -> {
                        if(staffContractHistory.getStaffContractHistory().getContractModel()!=null) {
                            if (staffContractHistory.getStaffContractHistory().getContractModel().get_id().equals(oldId)) {
                                staffContractHistoryRepository.delete(staffContractHistory);
                            }
                        }
            });
        }
        contractModelRepository.deleteById(oldId);
        logService.addLog(LogType.DELETE, ClassType.CONTRACTMODEL,"delete contact model "+action.get().getName());
    }

    @Override
    public List<ContractModelDto> getAll() {

        List<ContractModel> contractModels = contractModelRepository.findAll();
        // Create a list of all actions dto
        List<ContractModelDto> contractModelDtos = new ArrayList<>();

        for (ContractModel contractModel : contractModels) {
            ContractModelDto contractModelDto=contractModelMapper.modelToDto(contractModel);
            contractModelDtos.add(contractModelDto);
        }
        return contractModelDtos;
    }


    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.ContractModel, exceptionType, args);
    }
}
