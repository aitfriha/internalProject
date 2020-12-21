package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.EconomicStaffMonthMapper;
import org.techniu.isbackend.dto.model.EconomicStaffMonthDto;
import org.techniu.isbackend.entity.EconomicStaffMonth;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.EconomicStaffMonthRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class EconomicStaffMonthServiceImpl implements EconomicStaffMonthService {
    private EconomicStaffMonthRepository economicStaffMonthRepository;
    private final EconomicStaffMonthMapper economicStaffMonthMapper = Mappers.getMapper(EconomicStaffMonthMapper.class);


    public EconomicStaffMonthServiceImpl(EconomicStaffMonthRepository economicStaffMonthRepository) {
        this.economicStaffMonthRepository = economicStaffMonthRepository;
    }

    @Override
    public void saveEconomicStaffMonth(EconomicStaffMonthDto economicStaffMonthDto) {
        economicStaffMonthRepository.save(economicStaffMonthMapper.dtoToModel(economicStaffMonthDto));
    }

    @Override
    public List<EconomicStaffMonthDto> getAllEconomicStaffMonth() {
        // Get all actions
        List<EconomicStaffMonth> economicStaffMonth = economicStaffMonthRepository.findAll();
        // Create a list of all actions dto
        ArrayList<EconomicStaffMonthDto> economicStaffMonthDtos = new ArrayList<>();

        for (EconomicStaffMonth economicStaffMonth1 : economicStaffMonth) {
            EconomicStaffMonthDto economicStaffMonthDto = economicStaffMonthMapper.modelToDto(economicStaffMonth1);
            economicStaffMonthDtos.add(economicStaffMonthDto);
        }
        return economicStaffMonthDtos;
    }

    @Override
    public EconomicStaffMonth getById(String id) {
        return economicStaffMonthRepository.findAllBy_id(id);
    }

    @Override
    public List<EconomicStaffMonthDto> updateEconomicStaffMonth(EconomicStaffMonthDto economicStaffMonthDto, String id) {
        // save country if note existe
        EconomicStaffMonth economicStaffMonth = getById(id);
        Optional<EconomicStaffMonth> cs = Optional.ofNullable(economicStaffMonthRepository.findAllBy_id(id));

        if (!cs.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        economicStaffMonth.setMonthPayment(economicStaffMonthDto.getMonthPayment());
        economicStaffMonth.setChangeFactor(economicStaffMonthDto.getChangeFactor());
        economicStaffMonth.setGrosSalaryMonth(economicStaffMonthDto.getGrosSalaryMonth());
        economicStaffMonth.setGrosSalaryEuroMonth(economicStaffMonthDto.getGrosSalaryEuroMonth());
        economicStaffMonth.setNetSalaryEuroMonth(economicStaffMonthDto.getNetSalaryEuroMonth());
        economicStaffMonth.setNetSalaryMonth(economicStaffMonthDto.getNetSalaryMonth());
        economicStaffMonth.setContributionSalaryMonth(economicStaffMonthDto.getContributionSalaryMonth());
        economicStaffMonth.setContributionSalaryEuroMonth(economicStaffMonthDto.getContributionSalaryEuroMonth());
        economicStaffMonth.setCompanyCostMonth(economicStaffMonthDto.getCompanyCostMonth());
        economicStaffMonth.setCompanyCostEuroMonth(economicStaffMonthDto.getCompanyCostEuroMonth());

        economicStaffMonth.setEconomicStaff(economicStaffMonthDto.getEconomicStaff());
        economicStaffMonth.setCurrency(economicStaffMonthDto.getCurrency());

        // System.out.println("new :" + economicStaffMonth);
        economicStaffMonthRepository.save(economicStaffMonth);
        return getAllEconomicStaffMonth();
    }

    @Override
    public List<EconomicStaffMonthDto> remove(String id) {
        Optional<EconomicStaffMonth> action = Optional.ofNullable(economicStaffMonthRepository.findAllBy_id(id));
        // If EconomicStaffMonth doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        economicStaffMonthRepository.deleteById(id);
        return getAllEconomicStaffMonth();
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.EconomicStaffMonth, exceptionType, args);
    }
}
