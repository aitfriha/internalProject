package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.StaffEconomicContractInformationHistoryMapper;
import org.techniu.isbackend.dto.model.StaffEconomicContractInformationHistoryDto;
import org.techniu.isbackend.dto.model.StaffEconomicContractInformationHistoryDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.repository.StaffEconomicContractInformationHistoryRepository;
import org.techniu.isbackend.repository.StaffEconomicContractInformationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StaffEconomicContractInformationHistoryServiceImpl implements StaffEconomicContractInformationHistoryService {

    private StaffEconomicContractInformationHistoryRepository staffEconomicContractInformationHistoryRepository;
    private StaffEconomicContractInformationRepository staffEconomicContractInformationRepository;

    private final StaffEconomicContractInformationHistoryMapper staffEconomicContractInformationHistoryMapper = Mappers.getMapper(StaffEconomicContractInformationHistoryMapper.class);

    StaffEconomicContractInformationHistoryServiceImpl(StaffEconomicContractInformationHistoryRepository staffEconomicContractInformationHistoryRepository, StaffEconomicContractInformationRepository staffEconomicContractInformationRepository) {
        this.staffEconomicContractInformationHistoryRepository = staffEconomicContractInformationHistoryRepository;
        this.staffEconomicContractInformationRepository = staffEconomicContractInformationRepository;
    }

    @Override
    public void deleteStaffEconomicContractInformationHistory(String staffEconomicContractInformationHistoryId) {
        StaffEconomicContractInformationHistory staffEconomicContractInformationHistory = staffEconomicContractInformationHistoryRepository.findById(staffEconomicContractInformationHistoryId).get();
        staffEconomicContractInformationHistoryRepository.delete(staffEconomicContractInformationHistory);
    }


    @Override
    public List<StaffEconomicContractInformationHistoryDto> getAllStaffEconomicContractInformationHistory() {
        List<StaffEconomicContractInformationHistory> staffEconomicContractInformationHistories = staffEconomicContractInformationHistoryRepository.findAll();
        // Create a list of all staff dto
        ArrayList<StaffEconomicContractInformationHistoryDto> staffEconomicContractInformationHistoryDtos = new ArrayList<>();
        for (StaffEconomicContractInformationHistory staffEconomicContractInformationHistory : staffEconomicContractInformationHistories) {

            staffEconomicContractInformationHistoryDtos.add(staffEconomicContractInformationHistoryMapper.modelToDto(staffEconomicContractInformationHistory));
        }
        return staffEconomicContractInformationHistoryDtos;
    }

    @Override
    public List<StaffEconomicContractInformationHistoryDto> getStaffEconomicContractInformationHistoryByStaffEconomicContractInformation(String staffEconomicContractInformationId) {
        StaffEconomicContractInformation staffEconomicContractInformation = staffEconomicContractInformationRepository.findById(staffEconomicContractInformationId).get();

        List<StaffEconomicContractInformationHistory> staffEconomicContractInformationHistories = staffEconomicContractInformationHistoryRepository.findAllByStaffEconomicContractInformation(staffEconomicContractInformation);
        // Create a list of all staff dto
        ArrayList<StaffEconomicContractInformationHistoryDto> staffEconomicContractInformationHistoryDtos = new ArrayList<>();
        for (StaffEconomicContractInformationHistory staffEconomicContractInformationHistory : staffEconomicContractInformationHistories) {

            staffEconomicContractInformationHistoryDtos.add(staffEconomicContractInformationHistoryToDto(staffEconomicContractInformationHistory));
        }
        return staffEconomicContractInformationHistoryDtos;
    }

    public StaffEconomicContractInformationHistoryDto staffEconomicContractInformationHistoryToDto(StaffEconomicContractInformationHistory staffEconomicContractInformationHistory) {
        StaffEconomicContractInformationHistoryDto staffEconomicContractInformationHistoryDto = staffEconomicContractInformationHistoryMapper.modelToDto(staffEconomicContractInformationHistory);
        System.out.println(staffEconomicContractInformationHistory);
        staffEconomicContractInformationHistoryDto.setStaffEconomicContractInformationHistoryId(staffEconomicContractInformationHistory.get_id());
        staffEconomicContractInformationHistoryDto.setContractSalary(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getContractSalary());
        staffEconomicContractInformationHistoryDto.setContractSalaryDateGoing(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getContractSalaryDateGoing());
        staffEconomicContractInformationHistoryDto.setContractSalaryDateOut(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getContractSalaryDateOut());
        staffEconomicContractInformationHistoryDto.setCompanyContractCost(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCompanyContractCost());
        staffEconomicContractInformationHistoryDto.setCompanyContractCostDateGoing(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCompanyContractCostDateGoing());
        staffEconomicContractInformationHistoryDto.setCompanyContractCostDateOut(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCompanyContractCostDateOut());
        staffEconomicContractInformationHistoryDto.setExpenses(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getExpenses());
        staffEconomicContractInformationHistoryDto.setExpensesDateGoing(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getExpensesDateGoing());
        staffEconomicContractInformationHistoryDto.setExpensesDateOut(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getExpensesDateOut());
        staffEconomicContractInformationHistoryDto.setCompanyExpensesCost(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCompanyExpensesCost());
        staffEconomicContractInformationHistoryDto.setCompanyExpensesCostDateGoing(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCompanyExpensesCostDateGoing());
        staffEconomicContractInformationHistoryDto.setCompanyExpensesCostDateOut(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCompanyExpensesCostDateOut());
        staffEconomicContractInformationHistoryDto.setObjectives(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getObjectives());
        staffEconomicContractInformationHistoryDto.setObjectivesDateGoing(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getObjectivesDateGoing());
        staffEconomicContractInformationHistoryDto.setObjectivesDateOut(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getObjectivesDateOut());
        staffEconomicContractInformationHistoryDto.setCompanyObjectivesCost(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCompanyObjectivesCost());
        staffEconomicContractInformationHistoryDto.setCompanyObjectivesCostDateGoing(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCompanyObjectivesCostDateGoing());
        staffEconomicContractInformationHistoryDto.setCompanyObjectivesCostDateOut(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCompanyObjectivesCostDateOut());
        staffEconomicContractInformationHistoryDto.setTotalCompanyCost(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getTotalCompanyCost());
        staffEconomicContractInformationHistoryDto.setTotalCompanyCostDateGoing(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getTotalCompanyCostDateGoing());
        staffEconomicContractInformationHistoryDto.setTotalCompanyCostDateOut(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getTotalCompanyCostDateOut());
        staffEconomicContractInformationHistoryDto.setCurrencyId(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCurrency().get_id());
        staffEconomicContractInformationHistoryDto.setCurrencyCode(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCurrency().getTypeOfCurrency().getCurrencyCode());
        staffEconomicContractInformationHistoryDto.setCurrencyName(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCurrency().getTypeOfCurrency().getCurrencyName());
        staffEconomicContractInformationHistoryDto.setCurrencyMonth(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCurrency().getMonth());
        staffEconomicContractInformationHistoryDto.setCurrencyYear(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCurrency().getYear());
        staffEconomicContractInformationHistoryDto.setChangeFactor(staffEconomicContractInformationHistory.getStaffEconomicContractInformationHistory().getCurrency().getChangeFactor());
        return staffEconomicContractInformationHistoryDto;
    }
}
