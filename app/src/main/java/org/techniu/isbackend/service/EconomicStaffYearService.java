package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.EconomicStaffYearDto;
import org.techniu.isbackend.entity.EconomicStaffYear;

import java.util.List;

public interface EconomicStaffYearService {

    void saveEconomicStaffYear(EconomicStaffYearDto economicStaffDto);

    List<EconomicStaffYearDto> getAllEconomicStaffYear();

    EconomicStaffYear getById(String id);

    List<EconomicStaffYearDto> updateEconomicStaffYear(EconomicStaffYearDto economicStaffDto, String id);

    List<EconomicStaffYearDto> remove(String id);
}
