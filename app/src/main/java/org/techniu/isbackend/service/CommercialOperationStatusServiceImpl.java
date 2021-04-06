package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.CommercialOperationStatusMapper;
import org.techniu.isbackend.dto.model.CommercialOperationStatusDto;
import org.techniu.isbackend.entity.ClassType;
import org.techniu.isbackend.entity.CommercialOperationStatus;
import org.techniu.isbackend.entity.LogType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.CommercialOperationStatusRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
public class CommercialOperationStatusServiceImpl implements CommercialOperationStatusService{
    private CommercialOperationStatusRepository commercialOperationStatusRepository;
    private LogService logService;
    private final CommercialOperationStatusMapper commercialOperationStatusMapper = Mappers.getMapper(CommercialOperationStatusMapper.class);
    CommercialOperationStatusServiceImpl(CommercialOperationStatusRepository commercialOperationStatusRepository, LogService logService) {
        this.commercialOperationStatusRepository = commercialOperationStatusRepository;
        this.logService = logService;
    }
    @Override
    public void save(CommercialOperationStatusDto commercialOperationStatusDto) {
        // save country if note existe
        commercialOperationStatusDto.setName(commercialOperationStatusDto.getName().toLowerCase());

        if (commercialOperationStatusDto.getName().contains(" ")) {
             throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        Optional<CommercialOperationStatus>  commercialOperationStatus= Optional.ofNullable(commercialOperationStatusRepository.findByName(commercialOperationStatusDto.getName()));
        if (commercialOperationStatus.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        Optional<CommercialOperationStatus>  commercialOperationStatus1= Optional.ofNullable(commercialOperationStatusRepository.findByCode(commercialOperationStatusDto.getCode()));
        if (commercialOperationStatus1.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        commercialOperationStatusRepository.save(commercialOperationStatusMapper.dtoToModel(commercialOperationStatusDto));
        logService.addLog(LogType.CREATE, ClassType.STATUS,"create status of commercial operation "+commercialOperationStatusDto.getName());
    }

    @Override
    public void update(CommercialOperationStatusDto commercialOperationStatusDto) {
        // save country if note existe
        commercialOperationStatusDto.setName(commercialOperationStatusDto.getName().toLowerCase());
        if (commercialOperationStatusDto.getName().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
       Optional<CommercialOperationStatus> commercialOperationStatus1 = Optional.ofNullable(commercialOperationStatusRepository.findBy_id(commercialOperationStatusDto.getCommercialOperationStatusId()));
        if (!commercialOperationStatus1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
        Optional<CommercialOperationStatus> commercialOperationStatus2 = Optional.ofNullable(commercialOperationStatusRepository.findByName(commercialOperationStatusDto.getName()));

        if (commercialOperationStatus2.isPresent() && !(commercialOperationStatus1.get().getName().equals(commercialOperationStatusDto.getName())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
        Optional<CommercialOperationStatus> commercialOperationStatus3 = Optional.ofNullable(commercialOperationStatusRepository.findByCode(commercialOperationStatusDto.getCode()));
        if (commercialOperationStatus3.isPresent() && !(commercialOperationStatus1.get().getCode().equals(commercialOperationStatusDto.getCode())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
         commercialOperationStatusRepository.save(commercialOperationStatusMapper.dtoToModel(commercialOperationStatusDto));
        logService.addLog(LogType.UPDATE, ClassType.STATUS,"update status of commercial operation "+commercialOperationStatusDto.getName());
    }

    @Override
    public List<CommercialOperationStatusDto> getAll() {
        // Get all actions
        List<CommercialOperationStatus> commercialOperationStatuss = commercialOperationStatusRepository.findAll();
        // Create a list of all actions dto
        ArrayList<CommercialOperationStatusDto> commercialOperationStatusDtos = new ArrayList<>();

        for (CommercialOperationStatus commercialOperationStatus : commercialOperationStatuss) {
            CommercialOperationStatusDto commercialOperationStatusDto=commercialOperationStatusMapper.modelToDto(commercialOperationStatus);
            commercialOperationStatusDtos.add(commercialOperationStatusDto);
        }
        return commercialOperationStatusDtos;
    }
    /**
     * delete Action
     *
     * @param id - id
     */
    @Override
    public void remove(String id) {
        Optional<CommercialOperationStatus> action = Optional.ofNullable(commercialOperationStatusRepository.findBy_id(id));
        // If CommercialOperationStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        commercialOperationStatusRepository.deleteById(id);
        logService.addLog(LogType.DELETE, ClassType.STATUS,"delete status of commercial operation "+action.get().getName());
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.CommercialOperationStatus, exceptionType, args);
    }
}
