package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.ContractStatusMapper;
import org.techniu.isbackend.dto.model.ContractStatusDto;
import org.techniu.isbackend.entity.ClassType;
import org.techniu.isbackend.entity.ContractStatus;
import org.techniu.isbackend.entity.LogType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.ContractStatusRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
@Transactional
public class ContractStatusServiceImpl implements ContractStatusService {
    private ContractStatusRepository contractStatusRepository;
    private LogService logService;
    private final ContractStatusMapper contractStatusMapper = Mappers.getMapper(ContractStatusMapper.class);


    public ContractStatusServiceImpl(ContractStatusRepository contractStatusRepository, LogService logService) {
        this.contractStatusRepository = contractStatusRepository;
        this.logService = logService;
    }

    @Override
    public void saveContractStatus(ContractStatusDto contractStatusDto) {
        contractStatusRepository.save(contractStatusMapper.dtoToModel(contractStatusDto));
        logService.addLog(LogType.CREATE, ClassType.CONTRACTSTATUS,"create contract status "+contractStatusDto.getStatusName());
    }

    @Override
    public List<ContractStatus> getAllContractStatus() {
        return contractStatusRepository.findAll();
    }

    @Override
    public List<ContractStatusDto> getAllContractStatus2() {
        // Get all actions
        List<ContractStatus> contractStatus = contractStatusRepository.findAll();
        // Create a list of all actions dto
        ArrayList<ContractStatusDto> contractStatusDtos = new ArrayList<>();

        for (ContractStatus contractStatus1 : contractStatus) {
            ContractStatusDto contractStatusDto = contractStatusMapper.modelToDto(contractStatus1);
            contractStatusDtos.add(contractStatusDto);
        }
        return contractStatusDtos;
    }

    @Override
    public ContractStatus getById(String id) {
        return contractStatusRepository.findAllBy_id(id);
    }

    @Override
    public List<ContractStatusDto> updateContractStatus(ContractStatusDto contractStatusDto, String id) {
        // save country if note existe
        ContractStatus status = getById(id);
        Optional<ContractStatus> cs = Optional.ofNullable(contractStatusRepository.findAllBy_id(id));

        if (!cs.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        status.setStatusCode(contractStatusDto.getStatusCode());
        status.setStatusName(contractStatusDto.getStatusName());
        status.setDescription(contractStatusDto.getDescription());

        // System.out.println("new :" + status);
        contractStatusRepository.save(status);
        logService.addLog(LogType.UPDATE, ClassType.CONTRACTSTATUS,"update contract status "+status.getStatusName());
        return getAllContractStatus2();
    }

    @Override
    public List<ContractStatusDto> remove(String id) {
        Optional<ContractStatus> action = Optional.ofNullable(contractStatusRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        contractStatusRepository.deleteById(id);
        logService.addLog(LogType.UPDATE, ClassType.CONTRACTSTATUS,"update contract status "+action.get().getStatusName());
        return getAllContractStatus2();
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.ContractStatus, exceptionType, args);
    }
}
