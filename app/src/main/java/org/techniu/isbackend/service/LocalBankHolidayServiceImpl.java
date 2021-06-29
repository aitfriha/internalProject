package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.LocalBankHolidayMapper;
import org.techniu.isbackend.dto.model.LocalBankHolidayDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.FinancialCompanyRepository;
import org.techniu.isbackend.repository.LocalBankHolidayRepository;
import org.techniu.isbackend.repository.StaffContractHistoryRepository;
import org.techniu.isbackend.repository.StaffContractRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;
import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class LocalBankHolidayServiceImpl implements LocalBankHolidayService {

    private LocalBankHolidayRepository localBankHolidayRepository;
    private FinancialCompanyRepository financialCompanyRepository;
    private final LocalBankHolidayMapper localBankHolidayMapper = Mappers.getMapper(LocalBankHolidayMapper.class);
    private LogService logService;
    LocalBankHolidayServiceImpl(
            LocalBankHolidayRepository localBankHolidayRepository,
            FinancialCompanyRepository financialCompanyRepository,
            StaffContractRepository staffContractRepository,
            StaffContractHistoryRepository staffContractHistoryRepository, LogService logService) {
        this.localBankHolidayRepository = localBankHolidayRepository;
        this.financialCompanyRepository = financialCompanyRepository;
        this.logService = logService;
    }

    @Override
    public void save(LocalBankHolidayDto localBankHolidayDto) {
        FinancialCompany financialCompany = financialCompanyRepository.findById(localBankHolidayDto.getFinancialCompanyId()).get();
        LocalBankHoliday localBankHoliday = localBankHolidayMapper.dtoToModel(localBankHolidayDto);
        //Optional<LocalBankHoliday>  localBankHoliday1= Optional.ofNullable(localBankHolidayRepository.findByNameAndCompany(localBankHolidayDto.getName(), financialCompany));
        Optional<LocalBankHoliday>  localBankHoliday1= Optional.ofNullable(localBankHolidayRepository.findByCode(localBankHolidayDto.getCode()));
        if (localBankHoliday1.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }

        localBankHoliday.setCompany(financialCompany);
        localBankHolidayRepository.save(localBankHoliday);
        logService.addLog(LogType.CREATE, ClassType.LOCALBANKHOLIDAY,"create local bank holiday "+localBankHolidayDto.getName());
    }

    @Override
    public void update(LocalBankHolidayDto localBankHolidayDto) {
        LocalBankHoliday localBankHoliday = localBankHolidayRepository.findById(localBankHolidayDto.getLocalBankHolidayId()).get();

       // Optional<LocalBankHoliday>  localBankHoliday1= Optional.ofNullable(localBankHolidayRepository.findByCode(localBankHolidayDto.getCode()));
        Optional<LocalBankHoliday> localBankHoliday1 = Optional.ofNullable(localBankHolidayRepository.findBy_id(localBankHolidayDto.getLocalBankHolidayId()));
        if (!localBankHoliday1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
        Optional<LocalBankHoliday> localBankHoliday3 = Optional.ofNullable(localBankHolidayRepository.findByCode(localBankHolidayDto.getCode()));

        if (localBankHoliday3.isPresent() && !(localBankHoliday1.get().getCode().equals(localBankHolidayDto.getCode())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
        /*if (localBankHoliday1.isPresent()) {
            if(!localBankHoliday1.get().get_id().equals(localBankHolidayDto.getLocalBankHolidayId())) {
                throw exception(DUPLICATE_ENTITY);
            }
        }*/
        LocalBankHoliday localBankHoliday2 = localBankHolidayMapper.dtoToModel(localBankHolidayDto);
        localBankHoliday2.setCompany(localBankHoliday.getCompany());
        localBankHolidayRepository.save(localBankHoliday2);
        logService.addLog(LogType.CREATE, ClassType.LOCALBANKHOLIDAY,"update local bank holiday "+localBankHolidayDto.getName());
    }

    @Override
    public void remove(String id) {

        Optional<LocalBankHoliday> action = Optional.ofNullable(localBankHolidayRepository.findBy_id(id));
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }

        localBankHolidayRepository.deleteById(id);
        logService.addLog(LogType.DELETE, ClassType.LOCALBANKHOLIDAY,"delete local bank holiday "+action.get().getName());
    }

    @Override
    public List<LocalBankHolidayDto> getAll() {

        List<LocalBankHoliday> localBankHolidays = localBankHolidayRepository.findAll();
        // Create a list of all actions dto
        List<LocalBankHolidayDto> localBankHolidayDtos = new ArrayList<>();

        for (LocalBankHoliday localBankHoliday : localBankHolidays) {
            LocalBankHolidayDto localBankHolidayDto=localBankHolidayMapper.modelToDto(localBankHoliday);
            localBankHolidayDto.setFinancialCompanyId(localBankHoliday.getCompany().get_id());
            localBankHolidayDto.setCompanyName(localBankHoliday.getCompany().getName());
            localBankHolidayDtos.add(localBankHolidayDto);
        }
        return localBankHolidayDtos;
    }

    @Override
    public List<LocalBankHolidayDto> getAllByCompany(String companyId) {
        FinancialCompany financialCompany = financialCompanyRepository.findById(companyId).get();
        System.out.println("getALL");
        List<LocalBankHoliday> localBankHolidays = localBankHolidayRepository.getAllByCompany(financialCompany);
        // Create a list of all actions dto
        List<LocalBankHolidayDto> localBankHolidayDtos = new ArrayList<>();

        for (LocalBankHoliday localBankHoliday : localBankHolidays) {
            LocalBankHolidayDto localBankHolidayDto=localBankHolidayMapper.modelToDto(localBankHoliday);
            localBankHolidayDto.setFinancialCompanyId(localBankHoliday.getCompany().get_id());
            localBankHolidayDto.setCompanyName(localBankHoliday.getCompany().getName());
            localBankHolidayDtos.add(localBankHolidayDto);
        }
        return localBankHolidayDtos;
    }


    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.LocalBankHoliday, exceptionType, args);
    }
}
