package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.EconomicStaffYearMapper;
import org.techniu.isbackend.dto.model.EconomicStaffYearDto;
import org.techniu.isbackend.entity.EconomicStaffYear;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.EconomicStaffYearRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class EconomicStaffYearServiceImpl implements EconomicStaffYearService {
    private EconomicStaffYearRepository economicStaffYearRepository;
    private final EconomicStaffYearMapper economicStaffYearMapper = Mappers.getMapper(EconomicStaffYearMapper.class);


    public EconomicStaffYearServiceImpl(EconomicStaffYearRepository economicStaffYearRepository) {
        this.economicStaffYearRepository = economicStaffYearRepository;
    }

    @Override
    public void saveEconomicStaffYear(EconomicStaffYearDto economicStaffYearDto) {
        economicStaffYearRepository.save(economicStaffYearMapper.dtoToModel(economicStaffYearDto));
    }

    @Override
    public List<EconomicStaffYearDto> getAllEconomicStaffYear() {
        // Get all actions
        List<EconomicStaffYear> economicStaffYear = economicStaffYearRepository.findAll();
        // Create a list of all actions dto
        ArrayList<EconomicStaffYearDto> economicStaffYearDtos = new ArrayList<>();

        for (EconomicStaffYear economicStaffYear1 : economicStaffYear) {
            EconomicStaffYearDto economicStaffYearDto = economicStaffYearMapper.modelToDto(economicStaffYear1);
            economicStaffYearDtos.add(economicStaffYearDto);
        }
        return economicStaffYearDtos;
    }

    @Override
    public EconomicStaffYear getById(String id) {
        return economicStaffYearRepository.findAllBy_id(id);
    }

    @Override
    public List<EconomicStaffYearDto> updateEconomicStaffYear(EconomicStaffYearDto economicStaffYearDto, String id) {
        // save country if note existe
        EconomicStaffYear economicStaffYear = getById(id);
        Optional<EconomicStaffYear> cs = Optional.ofNullable(economicStaffYearRepository.findAllBy_id(id));

        if (!cs.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        economicStaffYear.setYearPayment(economicStaffYearDto.getYearPayment());
        economicStaffYear.setChangeFactor(economicStaffYearDto.getChangeFactor());
        economicStaffYear.setGrosSalaryYear(economicStaffYearDto.getGrosSalaryYear());
        economicStaffYear.setGrosSalaryEuroYear(economicStaffYearDto.getGrosSalaryEuroYear());
        economicStaffYear.setNetSalaryEuroYear(economicStaffYearDto.getNetSalaryEuroYear());
        economicStaffYear.setNetSalaryYear(economicStaffYearDto.getNetSalaryYear());
        economicStaffYear.setContributionSalaryYear(economicStaffYearDto.getContributionSalaryYear());
        economicStaffYear.setContributionSalaryEuroYear(economicStaffYearDto.getContributionSalaryEuroYear());
        economicStaffYear.setCompanyCostYear(economicStaffYearDto.getCompanyCostYear());
        economicStaffYear.setCompanyCostEuroYear(economicStaffYearDto.getCompanyCostEuroYear());

        economicStaffYear.setEconomicStaff(economicStaffYearDto.getEconomicStaff());
        economicStaffYear.setCurrency(economicStaffYearDto.getCurrency());

        // System.out.println("new :" + economicStaffYear);
        economicStaffYearRepository.save(economicStaffYear);
        return getAllEconomicStaffYear();
    }

    @Override
    public List<EconomicStaffYearDto> remove(String id) {
        Optional<EconomicStaffYear> action = Optional.ofNullable(economicStaffYearRepository.findAllBy_id(id));
        // If EconomicStaffYear doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        economicStaffYearRepository.deleteById(id);
        return getAllEconomicStaffYear();
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.EconomicStaffYear, exceptionType, args);
    }
}
