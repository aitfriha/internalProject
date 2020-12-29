package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.EconomicStaffExtraDto;
import org.techniu.isbackend.entity.EconomicStaffExtra;

import java.util.List;

public interface EconomicStaffExtraService {

    void saveEconomicStaffExtra(EconomicStaffExtraDto economicStaffDto);

    List<EconomicStaffExtraDto> getAllEconomicStaffExtra();

    EconomicStaffExtra getById(String id);

    List<EconomicStaffExtraDto> updateEconomicStaffExtra(EconomicStaffExtraDto economicStaffDto, String id);

    List<EconomicStaffExtraDto> remove(String id);
}
