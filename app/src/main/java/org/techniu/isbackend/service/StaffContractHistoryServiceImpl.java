package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.StaffContractHistoryMapper;
import org.techniu.isbackend.dto.mapper.StaffContractMapper;
import org.techniu.isbackend.dto.model.StaffContractDto;
import org.techniu.isbackend.dto.model.StaffContractHistoryDto;
import org.techniu.isbackend.dto.model.StaffDto;
import org.techniu.isbackend.entity.Staff;
import org.techniu.isbackend.entity.StaffContract;
import org.techniu.isbackend.entity.StaffContractHistory;
import org.techniu.isbackend.entity.StaffEconomicContractInformation;
import org.techniu.isbackend.repository.StaffContractHistoryRepository;
import org.techniu.isbackend.repository.StaffContractRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StaffContractHistoryServiceImpl implements StaffContractHistoryService {

    private StaffContractHistoryRepository staffContractHistoryRepository;
    private StaffContractRepository staffContractRepository;

    private final StaffContractHistoryMapper staffContractHistoryMapper = Mappers.getMapper(StaffContractHistoryMapper.class);

    StaffContractHistoryServiceImpl(StaffContractHistoryRepository staffContractHistoryRepository, StaffContractRepository staffContractRepository) {
        this.staffContractHistoryRepository = staffContractHistoryRepository;
        this.staffContractRepository = staffContractRepository;
    }

    @Override
    public void deleteStaffContractHistory(String staffContractHistoryId) {
        StaffContractHistory staffContractHistory = staffContractHistoryRepository.findById(staffContractHistoryId).get();
        staffContractHistoryRepository.delete(staffContractHistory);
    }


    @Override
    public List<StaffContractHistoryDto> getAllStaffContractHistory() {
        List<StaffContractHistory> staffContractHistories = staffContractHistoryRepository.findAll();
        // Create a list of all staff dto
        ArrayList<StaffContractHistoryDto> staffContractHistoryDtos = new ArrayList<>();
        for (StaffContractHistory staffContractHistory : staffContractHistories) {

            staffContractHistoryDtos.add(staffContractHistoryToDto(staffContractHistory));
        }
        return staffContractHistoryDtos;
    }

    @Override
    public List<StaffContractHistoryDto> getStaffContractHistoryByStaffContract(String staffContractId) {
        StaffContract staffContract = staffContractRepository.findById(staffContractId).get();
        List<StaffContractHistory> staffContractHistories = staffContractHistoryRepository.findAllByStaffContract(staffContract);
        // Create a list of all staff dto
        ArrayList<StaffContractHistoryDto> staffContractHistoryDtos = new ArrayList<>();
        for (StaffContractHistory staffContractHistory : staffContractHistories) {
            staffContractHistoryDtos.add(staffContractHistoryToDto(staffContractHistory));
        }
        return staffContractHistoryDtos;
    }


    public StaffContractHistoryDto staffContractHistoryToDto(StaffContractHistory staffContractHistory) {
        StaffContractHistoryDto staffContractHistoryDto = staffContractHistoryMapper.modelToDto(staffContractHistory);
        staffContractHistoryDto.setStaffContractHistoryId(staffContractHistory.get_id());
        staffContractHistoryDto.setCompanyId(staffContractHistory.getStaffContractHistory().getCompany().get_id());
        staffContractHistoryDto.setCompanyName(staffContractHistory.getStaffContractHistory().getCompany().getName());
        staffContractHistoryDto.setAssociateOffice(staffContractHistory.getStaffContractHistory().getAssociateOffice());
        staffContractHistoryDto.setHiringCountry(staffContractHistory.getStaffContractHistory().getHiringCountry());
        staffContractHistoryDto.setTownContract(staffContractHistory.getStaffContractHistory().getTownContract());
        staffContractHistoryDto.setPersonalNumber(staffContractHistory.getStaffContractHistory().getPersonalNumber());
        staffContractHistoryDto.setHighDate(staffContractHistory.getStaffContractHistory().getHighDate());
        staffContractHistoryDto.setLowDate(staffContractHistory.getStaffContractHistory().getLowDate());
        staffContractHistoryDto.setRegistrationDate(staffContractHistory.getStaffContractHistory().getRegistrationDate());
        staffContractHistoryDto.setPreContractDate(staffContractHistory.getStaffContractHistory().getPreContractDate());
        staffContractHistoryDto.setContractTypeId(staffContractHistory.getStaffContractHistory().getContractType().get_id());
        staffContractHistoryDto.setContractTypeName(staffContractHistory.getStaffContractHistory().getContractType().getName());
        staffContractHistoryDto.setContractTypeCountryId(staffContractHistory.getStaffContractHistory().getContractType().getState().getCountry().getCountryId());
        staffContractHistoryDto.setContractTypeCountry(staffContractHistory.getStaffContractHistory().getContractType().getState().getCountry().getCountryName());
        staffContractHistoryDto.setContractTypeStateId(staffContractHistory.getStaffContractHistory().getContractType().getState().get_id());
        staffContractHistoryDto.setContractTypeState(staffContractHistory.getStaffContractHistory().getContractType().getState().getStateName());
        staffContractHistoryDto.setLegalCategoryTypeId(staffContractHistory.getStaffContractHistory().getLegalCategoryType().get_id());
        staffContractHistoryDto.setLegalCategoryTypeName(staffContractHistory.getStaffContractHistory().getLegalCategoryType().getName());
        staffContractHistoryDto.setInternalRulesDoc(staffContractHistory.getStaffContractHistory().getInternalRulesDoc());
        staffContractHistoryDto.setContractDoc(staffContractHistory.getStaffContractHistory().getContractDoc());
        staffContractHistoryDto.setPreContractDoc(staffContractHistory.getStaffContractHistory().getPreContractDoc());
        return staffContractHistoryDto;
    }
}
