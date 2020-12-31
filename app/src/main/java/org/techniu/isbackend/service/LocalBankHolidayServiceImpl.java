package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.LocalBankHolidayMapper;
import org.techniu.isbackend.dto.model.LocalBankHolidayDto;
import org.techniu.isbackend.entity.FinancialCompany;
import org.techniu.isbackend.entity.LocalBankHoliday;
import org.techniu.isbackend.entity.StaffContract;
import org.techniu.isbackend.entity.StaffContractHistory;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.FinancialCompanyRepository;
import org.techniu.isbackend.repository.LocalBankHolidayRepository;
import org.techniu.isbackend.repository.StaffContractHistoryRepository;
import org.techniu.isbackend.repository.StaffContractRepository;

import java.util.ArrayList;
import java.util.Date;
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

    LocalBankHolidayServiceImpl(
            LocalBankHolidayRepository localBankHolidayRepository,
            FinancialCompanyRepository financialCompanyRepository,
            StaffContractRepository staffContractRepository,
            StaffContractHistoryRepository staffContractHistoryRepository) {
        this.localBankHolidayRepository = localBankHolidayRepository;
        this.financialCompanyRepository = financialCompanyRepository;
    }

    @Override
    public void save(LocalBankHolidayDto localBankHolidayDto) {
        FinancialCompany financialCompany = financialCompanyRepository.findById(localBankHolidayDto.getFinancialCompanyId()).get();
        LocalBankHoliday localBankHoliday = localBankHolidayMapper.dtoToModel(localBankHolidayDto);

        Optional<LocalBankHoliday>  localBankHoliday1= Optional.ofNullable(localBankHolidayRepository.findByNameAndCompany(localBankHolidayDto.getName(), financialCompany));
        if (localBankHoliday1.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }

        localBankHoliday.setCompany(financialCompany);
        localBankHolidayRepository.save(localBankHoliday);
    }

    @Override
    public void update(LocalBankHolidayDto localBankHolidayDto) {
        LocalBankHoliday localBankHoliday = localBankHolidayRepository.findById(localBankHolidayDto.getLocalBankHolidayId()).get();

        Optional<LocalBankHoliday>  localBankHoliday1= Optional.ofNullable(localBankHolidayRepository.findByNameAndCompany(localBankHolidayDto.getName(), localBankHoliday.getCompany()));
        if (localBankHoliday1.isPresent()) {
            if(!localBankHoliday1.get().get_id().equals(localBankHolidayDto.getLocalBankHolidayId())) {
                throw exception(DUPLICATE_ENTITY);
            }
        }

        LocalBankHoliday localBankHoliday2 = localBankHolidayMapper.dtoToModel(localBankHolidayDto);
        localBankHoliday2.setCompany(localBankHoliday.getCompany());
        localBankHolidayRepository.save(localBankHoliday2);
    }

    @Override
    public void remove(String id) {

        Optional<LocalBankHoliday> action = Optional.ofNullable(localBankHolidayRepository.findBy_id(id));
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }

        localBankHolidayRepository.deleteById(id);
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
        System.out.println(localBankHolidayDtos);
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
        System.out.println(localBankHolidayDtos);
        return localBankHolidayDtos;
    }


    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.LocalBankHoliday, exceptionType, args);
    }
}
