package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.EconomicStaffMonthDto;
import org.techniu.isbackend.entity.EconomicStaffMonth;

import java.util.List;

public interface EconomicStaffMonthService {

    void saveEconomicStaffMonth(EconomicStaffMonthDto economicStaffDto);

    List<EconomicStaffMonthDto> getAllEconomicStaffMonth();

    EconomicStaffMonth getById(String id);

    List<EconomicStaffMonthDto> updateEconomicStaffMonth(EconomicStaffMonthDto economicStaffDto, String id);

    List<EconomicStaffMonthDto> remove(String id);
}
