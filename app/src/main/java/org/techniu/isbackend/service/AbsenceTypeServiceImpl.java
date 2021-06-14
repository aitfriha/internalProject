package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.AbsenceTypeMapper;
import org.techniu.isbackend.dto.model.AbsenceTypeDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.entity.AbsenceType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.AbsenceRequestRepository;
import org.techniu.isbackend.repository.AbsenceTypeRepository;
import org.techniu.isbackend.repository.StaffRepository;
import org.techniu.isbackend.repository.StateCountryRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;

@Service
@Transactional
public class AbsenceTypeServiceImpl implements AbsenceTypeService {

    private AbsenceTypeRepository absenceTypeRepository;
    private StateCountryRepository stateCountryRepository;
    private StaffRepository staffRepository;
    private AbsenceRequestRepository absenceRequestRepository;
    private LogService logService;
    private final AbsenceTypeMapper absenceTypeMapper = Mappers.getMapper(AbsenceTypeMapper.class);

    AbsenceTypeServiceImpl(AbsenceTypeRepository absenceTypeRepository,
                           StateCountryRepository stateCountryRepository,
                           StaffRepository staffRepository,
                           AbsenceRequestRepository absenceRequestRepository, LogService logService) {
        this.absenceTypeRepository = absenceTypeRepository;
        this.stateCountryRepository = stateCountryRepository;
        this.staffRepository = staffRepository;
        this.absenceRequestRepository = absenceRequestRepository;
        this.logService = logService;
    }

    @Override
    public void save(AbsenceTypeDto absenceTypeDto) {
        AbsenceType absenceType = absenceTypeMapper.dtoToModel(absenceTypeDto);
        StateCountry stateCountry = stateCountryRepository.findById(absenceTypeDto.getStateId()).get();

        if (absenceTypeDto.getCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
/*
        Optional<AbsenceType> absenceType1 = Optional.ofNullable(absenceTypeRepository.findByNameAndState(absenceTypeDto.getName(), stateCountry));
        if (absenceType1.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        Optional<AbsenceType> absenceType2 = Optional.ofNullable(absenceTypeRepository.findByCodeAndState(absenceTypeDto.getCode(), stateCountry));
        if (absenceType2.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }*/
        Optional<AbsenceType> absenceType1 = Optional.ofNullable(absenceTypeRepository.findByCode(absenceTypeDto.getCode()));
        if (absenceType1.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        if (absenceTypeDto.getDocument() != null) {
            absenceType.setDocument(absenceTypeDto.getDocument());
            absenceType.setDocExtension(absenceTypeDto.getDocExtension());
        }
        absenceType.setState(stateCountry);
        Staff absenceResponsible = staffRepository.findById(absenceTypeDto.getAbsenceResponsibleId()).get();
        absenceType.setAbsenceResponsible(absenceResponsible);
        Staff inCopyResponsible = staffRepository.findById(absenceTypeDto.getInCopyResponsibleId()).get();
        absenceType.setInCopyResponsible(inCopyResponsible);

        absenceTypeRepository.save(absenceType);
        logService.addLog(LogType.CREATE, ClassType.ABSENCETYPE,"create absence type "+absenceTypeDto.getName());
    }

    @Override
    public void update(AbsenceTypeDto absenceTypeDto) {
        //System.out.println(absenceTypeDto.getAbsenceTypeId());
        AbsenceType absenceType = absenceTypeRepository.findById(absenceTypeDto.getAbsenceTypeId()).get();
        if (absenceTypeDto.getCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        /*
        Optional<AbsenceType> absenceType1 = Optional.ofNullable(absenceTypeRepository.findByNameAndState(absenceTypeDto.getName(), absenceType.getState()));
        if (absenceType1.isPresent()) {
            if (!absenceType1.get().get_id().equals(absenceTypeDto.getAbsenceTypeId())) {
                throw exception(DUPLICATE_ENTITY);
            }
        }
        Optional<AbsenceType> absenceType2 = Optional.ofNullable(absenceTypeRepository.findByCodeAndState(absenceTypeDto.getCode(), absenceType.getState()));
        if (absenceType2.isPresent()) {
            if (!absenceType2.get().get_id().equals(absenceTypeDto.getAbsenceTypeId())) {
                throw exception(DUPLICATE_ENTITY);
            }
        }*/
        Optional<AbsenceType> absenceType1 = Optional.ofNullable(absenceTypeRepository.findBy_id(absenceTypeDto.getAbsenceTypeId()));
        if (!absenceType1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        Optional<AbsenceType> absenceType3 = Optional.ofNullable(absenceTypeRepository.findByCode(absenceTypeDto.getCode()));
        if (absenceType3.isPresent() && !(absenceType1.get().getCode().equals(absenceTypeDto.getCode())) ) {
            throw exception(DUPLICATE_ENTITY);
        }

        if (absenceTypeDto.getDocument() != null) {
            absenceType.setDocument(absenceTypeDto.getDocument());
            absenceType.setDocExtension(absenceTypeDto.getDocExtension());
        }
        absenceType.setCode(absenceTypeDto.getCode());
        absenceType.setName(absenceTypeDto.getName());
        absenceType.setDescription(absenceTypeDto.getDescription());
        Staff absenceResponsible = staffRepository.findById(absenceTypeDto.getAbsenceResponsibleId()).get();
        absenceType.setAbsenceResponsible(absenceResponsible);
        Staff inCopyResponsible = staffRepository.findById(absenceTypeDto.getInCopyResponsibleId()).get();
        absenceType.setInCopyResponsible(inCopyResponsible);
        absenceTypeRepository.save(absenceType);
        logService.addLog(LogType.UPDATE, ClassType.ABSENCETYPE,"update absence type "+absenceType.getName());
    }

    @Override
    public void remove(String oldId, String newId) {
        Optional<AbsenceType> action = Optional.ofNullable(absenceTypeRepository.findById(oldId).get());
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        if(!newId.equals("")){
            Optional<AbsenceType> newAbsenceType = Optional.ofNullable(absenceTypeRepository.findById(newId).get());
            AbsenceType absenceType = absenceTypeRepository.findById(oldId).get();
            List<AbsenceRequest> list = absenceRequestRepository.findAllByAbsenceType(absenceType);
            System.out.println(list.size());
            if(list.size()>0) {
                list.forEach(absenceType1 -> {
                    absenceType1.setAbsenceType(newAbsenceType.get());
                    absenceRequestRepository.save(absenceType1);
                });
            }
        }
        absenceTypeRepository.deleteById(oldId);
        logService.addLog(LogType.DELETE, ClassType.ABSENCETYPE,"delete absence type "+action.get().getName());
    }

    @Override
    public List<AbsenceTypeDto> getAll() {

        List<AbsenceType> absenceTypes = absenceTypeRepository.findAll();
        // Create a list of all actions dto
        List<AbsenceTypeDto> absenceTypeDtos = new ArrayList<>();

        for (AbsenceType absenceType : absenceTypes) {

            absenceTypeDtos.add(absenceTypeToAbsenceTypeDto(absenceType));
        }
        return absenceTypeDtos;
    }

    @Override
    public List<AbsenceTypeDto> getAllByState(String stateCountryId) {
        StateCountry stateCountry = stateCountryRepository.findById(stateCountryId).get();

        List<AbsenceType> absenceTypes = absenceTypeRepository.getAllByState(stateCountry);
        // Create a list of all actions dto
        List<AbsenceTypeDto> absenceTypeDtos = new ArrayList<>();

        for (AbsenceType absenceType : absenceTypes) {
            absenceTypeDtos.add(absenceTypeToAbsenceTypeDto(absenceType));
        }
        return absenceTypeDtos;
    }

    public AbsenceTypeDto absenceTypeToAbsenceTypeDto(AbsenceType absenceType) {
        AbsenceTypeDto absenceTypeDto = absenceTypeMapper.modelToDto(absenceType);
        absenceTypeDto.setStateId(absenceType.getState().get_id());
        absenceTypeDto.setStateName(absenceType.getState().getStateName());
        absenceTypeDto.setCountryName(absenceType.getState().getCountry().getCountryName());
        absenceTypeDto.setAbsenceResponsibleId(absenceType.getAbsenceResponsible().getStaffId());
        absenceTypeDto.setAbsenceResponsibleName(absenceType.getAbsenceResponsible().getFirstName() +
                " " + absenceType.getAbsenceResponsible().getFatherFamilyName() +
                " " + absenceType.getAbsenceResponsible().getMotherFamilyName());
        absenceTypeDto.setAbsenceResponsibleEmail(absenceType.getAbsenceResponsible().getCompanyEmail());
        absenceTypeDto.setInCopyResponsibleId(absenceType.getInCopyResponsible().getStaffId());
        absenceTypeDto.setInCopyResponsibleName(absenceType.getInCopyResponsible().getFirstName() +
                " " + absenceType.getInCopyResponsible().getFatherFamilyName() +
                " " + absenceType.getInCopyResponsible().getMotherFamilyName());
        absenceTypeDto.setInCopyResponsibleEmail(absenceType.getInCopyResponsible().getCompanyEmail());
        return absenceTypeDto;
    }


    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.AbsenceType, exceptionType, args);
    }
}
