package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.EconomicStaffMapper;
import org.techniu.isbackend.dto.model.EconomicStaffDto;
import org.techniu.isbackend.entity.EconomicStaff;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.EconomicStaffRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class EconomicStaffServiceImpl implements EconomicStaffService {
    private EconomicStaffRepository economicStaffRepository;
    private final EconomicStaffMapper economicStaffMapper = Mappers.getMapper(EconomicStaffMapper.class);


    public EconomicStaffServiceImpl(EconomicStaffRepository economicStaffRepository) {
        this.economicStaffRepository = economicStaffRepository;
    }

    @Override
    public void saveEconomicStaff(EconomicStaffDto economicStaffDto) {
        economicStaffRepository.save(economicStaffMapper.dtoToModel(economicStaffDto));
    }

    @Override
    public List<EconomicStaffDto> getAllEconomicStaff() {
        // Get all actions
        List<EconomicStaff> economicStaff = economicStaffRepository.findAll();
        // Create a list of all actions dto
        ArrayList<EconomicStaffDto> economicStaffDtos = new ArrayList<>();

        for (EconomicStaff economicStaff1 : economicStaff) {
            EconomicStaffDto economicStaffDto = economicStaffMapper.modelToDto(economicStaff1);
            economicStaffDtos.add(economicStaffDto);
        }
        return economicStaffDtos;
    }

    @Override
    public EconomicStaff getById(String id) {
        return economicStaffRepository.findAllBy_id(id);
    }

    @Override
    public List<EconomicStaffDto> updateEconomicStaff(EconomicStaffDto economicStaffDto, String id) {
        // save country if note existe
        EconomicStaff economicStaff = getById(id);
        Optional<EconomicStaff> cs = Optional.ofNullable(economicStaffRepository.findAllBy_id(id));

        if (!cs.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        economicStaff.setCompanyCost(economicStaffDto.getCompanyCost());
        economicStaff.setCompanyCostEuro(economicStaffDto.getCompanyCostEuro());
        economicStaff.setContributionSalary(economicStaffDto.getContributionSalary());
        economicStaff.setContributionSalaryEuro(economicStaffDto.getContributionSalaryEuro());
        economicStaff.setGrosSalary(economicStaffDto.getGrosSalary());
        economicStaff.setGrosSalaryEuro(economicStaffDto.getGrosSalaryEuro());
        economicStaff.setNetSalary(economicStaffDto.getNetSalary());
        economicStaff.setNetSalaryEuro(economicStaffDto.getNetSalaryEuro());
        
        economicStaff.setFinancialCompany(economicStaffDto.getFinancialCompany());
        economicStaff.setStaff(economicStaffDto.getStaff());
        economicStaff.setCurrency(economicStaffDto.getCurrency());
        
        economicStaff.setChangeFactor(economicStaffDto.getChangeFactor());
        economicStaff.setEmployeeNumber(economicStaffDto.getEmployeeNumber());
        economicStaff.setName(economicStaffDto.getName());
        economicStaff.setFatherName(economicStaffDto.getFatherName());
        economicStaff.setMotherName(economicStaffDto.getMotherName());
        economicStaff.setHighDate(economicStaffDto.getHighDate());
        economicStaff.setLowDate(economicStaffDto.getLowDate());

        // System.out.println("new :" + economicStaff);
        economicStaffRepository.save(economicStaff);
        return getAllEconomicStaff();
    }

    @Override
    public List<EconomicStaffDto> remove(String id) {
        Optional<EconomicStaff> action = Optional.ofNullable(economicStaffRepository.findAllBy_id(id));
        // If EconomicStaff doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        economicStaffRepository.deleteById(id);
        return getAllEconomicStaff();
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.EconomicStaff, exceptionType, args);
    }
}
