package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.ContractTypeMapper;
import org.techniu.isbackend.dto.model.ContractTypeDto;
import org.techniu.isbackend.entity.ContractType;
import org.techniu.isbackend.entity.StaffContract;
import org.techniu.isbackend.entity.StaffContractHistory;
import org.techniu.isbackend.entity.StateCountry;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.ContractTypeRepository;
import org.techniu.isbackend.repository.StaffContractHistoryRepository;
import org.techniu.isbackend.repository.StaffContractRepository;
import org.techniu.isbackend.repository.StateCountryRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;

@Service
@Transactional
public class ContractTypeServiceImpl implements ContractTypeService {

    private ContractTypeRepository contractTypeRepository;
    private StateCountryRepository stateCountryRepository;
    private StaffContractRepository staffContractRepository;
    private StaffContractHistoryRepository staffContractHistoryRepository;
    private final ContractTypeMapper contractTypeMapper = Mappers.getMapper(ContractTypeMapper.class);

    ContractTypeServiceImpl(
            ContractTypeRepository contractTypeRepository,
            StateCountryRepository stateCountryRepository,
            StaffContractRepository staffContractRepository,
            StaffContractHistoryRepository staffContractHistoryRepository) {
        this.contractTypeRepository = contractTypeRepository;
        this.stateCountryRepository = stateCountryRepository;
        this.staffContractRepository = staffContractRepository;
        this.staffContractHistoryRepository = staffContractHistoryRepository;
    }

    @Override
    public void save(ContractTypeDto contractTypeDto) {
        StateCountry stateCountry = stateCountryRepository.findById(contractTypeDto.getStateId()).get();
        ContractType contractType = contractTypeMapper.dtoToModel(contractTypeDto);

        if (contractTypeDto.getCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }

        Optional<ContractType>  contractType1= Optional.ofNullable(contractTypeRepository.findByNameAndState(contractTypeDto.getName(), stateCountry));
        if (contractType1.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        Optional<ContractType>  contractType2= Optional.ofNullable(contractTypeRepository.findByCodeAndState(contractTypeDto.getCode(), stateCountry));
        if (contractType2.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }

        contractType.setState(stateCountry);
        contractTypeRepository.save(contractType);
    }

    @Override
    public void update(ContractTypeDto contractTypeDto) {
        ContractType contractType = contractTypeRepository.findById(contractTypeDto.getContractTypeId()).get();

        if (contractTypeDto.getCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }

        Optional<ContractType>  contractType1= Optional.ofNullable(contractTypeRepository.findByNameAndState(contractTypeDto.getName(), contractType.getState()));

        if (contractType1.isPresent()) {
            if(!contractType1.get().get_id().equals(contractTypeDto.getContractTypeId())) {
                throw exception(DUPLICATE_ENTITY);
            }
        }
        Optional<ContractType>  contractType2= Optional.ofNullable(contractTypeRepository.findByCodeAndState(contractTypeDto.getCode(), contractType.getState()));
        if (contractType2.isPresent()) {
            if(!contractType2.get().get_id().equals(contractTypeDto.getContractTypeId())) {
                throw exception(DUPLICATE_ENTITY);
            }
        }

        contractType.setCode(contractTypeDto.getCode());
        contractType.setName(contractTypeDto.getName());
        contractType.setDescription(contractTypeDto.getDescription());

        contractTypeRepository.save(contractType);
    }

    @Override
    public void remove(String oldId, String newId) {

        Optional<ContractType> action = Optional.ofNullable(contractTypeRepository.findBy_id(oldId));
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        if(!newId.equals("")){
            List<StaffContract> staffContracts = staffContractRepository.findAllByContractType(contractTypeRepository.findById(oldId).get());
            ContractType newContractType = contractTypeRepository.findById(newId).get();
            staffContracts.forEach( contract -> {
                contract.setContractType(newContractType);
                StaffContractHistory staffContractHistory = new StaffContractHistory();
                staffContractHistory.setStaffContract(contract);
                staffContractHistory.setStaffContractHistory(staffContractRepository.save(contract));
                staffContractHistory.setUpdatedAt(new Date().toInstant().toString().substring(0,10));
                staffContractHistoryRepository.save(staffContractHistory);
            });
        }
        List<StaffContractHistory> staffContractHistories = staffContractHistoryRepository.findAll();
        staffContractHistories.forEach(staffContractHistory -> {
            if(staffContractHistory.getStaffContractHistory().getContractType().get_id().equals(oldId)) {
                staffContractHistoryRepository.delete(staffContractHistory);
            }
        });

        contractTypeRepository.deleteById(oldId);
    }

    @Override
    public List<ContractTypeDto> getAll() {

        List<ContractType> contractTypes = contractTypeRepository.findAll();
        // Create a list of all actions dto
        List<ContractTypeDto> contractTypeDtos = new ArrayList<>();

        for (ContractType contractType : contractTypes) {
            ContractTypeDto contractTypeDto=contractTypeMapper.modelToDto(contractType);
            contractTypeDto.setStateId(contractType.getState().get_id());
            contractTypeDto.setStateName(contractType.getState().getStateName());
            contractTypeDto.setCountryName(contractType.getState().getCountry().getCountryName());
            contractTypeDtos.add(contractTypeDto);
        }
        return contractTypeDtos;
    }

    @Override
    public List<ContractTypeDto> getAllByState(String stateCountryId) {
        StateCountry stateCountry = stateCountryRepository.findById(stateCountryId).get();

        List<ContractType> contractTypes = contractTypeRepository.getAllByState(stateCountry);
        // Create a list of all actions dto
        List<ContractTypeDto> contractTypeDtos = new ArrayList<>();

        for (ContractType contractType : contractTypes) {
            ContractTypeDto contractTypeDto=contractTypeMapper.modelToDto(contractType);
            contractTypeDto.setStateId(contractType.getState().get_id());
            contractTypeDto.setStateName(contractType.getState().getStateName());
            contractTypeDto.setCountryName(contractType.getState().getCountry().getCountryName());
            contractTypeDtos.add(contractTypeDto);
        }
        return contractTypeDtos;
    }


    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.ContractType, exceptionType, args);
    }
}
