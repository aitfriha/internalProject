package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.EconomicStaffDto;
import org.techniu.isbackend.entity.EconomicStaff;

import java.util.List;

public interface EconomicStaffService {

    void saveEconomicStaff(EconomicStaffDto economicStaffDto);

    List<EconomicStaffDto> getAllEconomicStaff();

    EconomicStaff getById(String id);

    List<EconomicStaffDto> updateEconomicStaff(EconomicStaffDto economicStaffDto, String id);

    List<EconomicStaffDto> remove(String id);
}
