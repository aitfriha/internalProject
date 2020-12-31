package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.LocalBankHolidayDto;

import java.util.List;

public interface LocalBankHolidayService {
    void save(LocalBankHolidayDto localBankHolidayDto);
    void update(LocalBankHolidayDto localBankHolidayDto);
    void remove(String id);
    List<LocalBankHolidayDto> getAll();
    List<LocalBankHolidayDto> getAllByCompany(String companyId);
}
