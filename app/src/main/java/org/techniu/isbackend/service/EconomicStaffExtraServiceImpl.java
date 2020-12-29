package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.EconomicStaffExtraMapper;
import org.techniu.isbackend.dto.model.EconomicStaffExtraDto;
import org.techniu.isbackend.entity.EconomicStaffExtra;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.EconomicStaffExtraRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class EconomicStaffExtraServiceImpl implements EconomicStaffExtraService {
    private EconomicStaffExtraRepository economicStaffExtraRepository;
    private final EconomicStaffExtraMapper economicStaffExtraMapper = Mappers.getMapper(EconomicStaffExtraMapper.class);


    public EconomicStaffExtraServiceImpl(EconomicStaffExtraRepository economicStaffExtraRepository) {
        this.economicStaffExtraRepository = economicStaffExtraRepository;
    }

    @Override
    public void saveEconomicStaffExtra(EconomicStaffExtraDto economicStaffExtraDto) {
        economicStaffExtraRepository.save(economicStaffExtraMapper.dtoToModel(economicStaffExtraDto));
    }

    @Override
    public List<EconomicStaffExtraDto> getAllEconomicStaffExtra() {
        // Get all actions
        List<EconomicStaffExtra> economicStaffExtra = economicStaffExtraRepository.findAll();
        // Create a list of all actions dto
        ArrayList<EconomicStaffExtraDto> economicStaffExtraDtos = new ArrayList<>();

        for (EconomicStaffExtra economicStaffExtra1 : economicStaffExtra) {
            EconomicStaffExtraDto economicStaffExtraDto = economicStaffExtraMapper.modelToDto(economicStaffExtra1);
            economicStaffExtraDtos.add(economicStaffExtraDto);
        }
        return economicStaffExtraDtos;
    }

    @Override
    public EconomicStaffExtra getById(String id) {
        return economicStaffExtraRepository.findAllBy_id(id);
    }

    @Override
    public List<EconomicStaffExtraDto> updateEconomicStaffExtra(EconomicStaffExtraDto economicStaffExtraDto, String id) {
        // save country if note existe
        EconomicStaffExtra economicStaffExtra = getById(id);
        Optional<EconomicStaffExtra> cs = Optional.ofNullable(economicStaffExtraRepository.findAllBy_id(id));

        if (!cs.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        economicStaffExtra.setExtraordinaryDate(economicStaffExtraDto.getExtraordinaryDate());
        economicStaffExtra.setChangeFactor(economicStaffExtraDto.getChangeFactor());
        economicStaffExtra.setExtraordinaryExpenses(economicStaffExtraDto.getExtraordinaryExpenses());
        economicStaffExtra.setExtraordinaryExpensesEuro(economicStaffExtraDto.getExtraordinaryExpensesEuro());
        economicStaffExtra.setExtraordinaryObjectives(economicStaffExtraDto.getExtraordinaryObjectives());
        economicStaffExtra.setExtraordinaryObjectivesEuro(economicStaffExtraDto.getExtraordinaryObjectives());

        economicStaffExtra.setEconomicStaff(economicStaffExtraDto.getEconomicStaff());
        economicStaffExtra.setCurrency(economicStaffExtraDto.getCurrency());

        // System.out.println("new :" + economicStaffExtra);
        economicStaffExtraRepository.save(economicStaffExtra);
        return getAllEconomicStaffExtra();
    }

    @Override
    public List<EconomicStaffExtraDto> remove(String id) {
        Optional<EconomicStaffExtra> action = Optional.ofNullable(economicStaffExtraRepository.findAllBy_id(id));
        // If EconomicStaffExtra doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        economicStaffExtraRepository.deleteById(id);
        return getAllEconomicStaffExtra();
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.EconomicStaffExtra, exceptionType, args);
    }
}
