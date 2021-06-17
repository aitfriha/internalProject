package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.StaffEconomicContractInformationMapper;
import org.techniu.isbackend.dto.model.StaffEconomicContractInformationDto;
import org.techniu.isbackend.entity.Currency;
import org.techniu.isbackend.entity.StaffEconomicContractInformation;
import org.techniu.isbackend.entity.StaffEconomicContractInformationHistory;
import org.techniu.isbackend.entity.StaffEconomicContractInformation;
import org.techniu.isbackend.repository.CurrencyRepository;
import org.techniu.isbackend.repository.StaffEconomicContractInformationHistoryRepository;
import org.techniu.isbackend.repository.StaffEconomicContractInformationRepository;
import org.techniu.isbackend.repository.StaffRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class StaffEconomicContractInformationServiceImpl implements StaffEconomicContractInformationService {

    private StaffEconomicContractInformationRepository staffEconomicContractInformationRepository;
    private StaffEconomicContractInformationHistoryRepository staffEconomicContractInformationHistoryRepository;
    private CurrencyRepository currencyRepository;
    private StaffRepository staffRepository;

    private final StaffEconomicContractInformationMapper staffEconomicContractInformationMapper = Mappers.getMapper(StaffEconomicContractInformationMapper.class);


    StaffEconomicContractInformationServiceImpl(
            StaffEconomicContractInformationRepository staffEconomicContractInformationRepository,
            StaffRepository staffRepository,
            StaffEconomicContractInformationHistoryRepository staffEconomicContractInformationHistoryRepository,
            CurrencyRepository currencyRepository) {
        this.staffEconomicContractInformationRepository = staffEconomicContractInformationRepository;
        this.staffRepository = staffRepository;
        this.staffEconomicContractInformationHistoryRepository = staffEconomicContractInformationHistoryRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void update(StaffEconomicContractInformationDto staffEconomicContractInformationDto) {
        StaffEconomicContractInformation staffEconomicContractInformation = staffEconomicContractInformationMapper.dtoToModel(staffEconomicContractInformationDto);
        Currency currency = currencyRepository.findById(staffEconomicContractInformationDto.getCurrencyId()).get();
        staffEconomicContractInformation.setCurrency(currency);
        StaffEconomicContractInformation staffEconomicContractInformation1 = staffEconomicContractInformationRepository.findById(staffEconomicContractInformationDto.getStaffEconomicContractInformationId()).get();
        StaffEconomicContractInformationHistory staffEconomicContractInformationHistory = new StaffEconomicContractInformationHistory();
        staffEconomicContractInformationHistory.setStaffEconomicContractInformation(staffEconomicContractInformation1);
        staffEconomicContractInformationHistory.setStaffEconomicContractInformationHistory(staffEconomicContractInformationRepository.save(staffEconomicContractInformation));
        staffEconomicContractInformationHistory.setUpdatedAt(staffEconomicContractInformationDto.getUpdatedAt());
        staffEconomicContractInformationHistoryRepository.save(staffEconomicContractInformationHistory);
    }
    
}
