package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.AbsenceRequestDto;
import org.techniu.isbackend.dto.model.AbsenceTypeDto;
import org.techniu.isbackend.entity.AbsenceRequest;

import java.util.List;

public interface AbsenceRequestService {
    void save(AbsenceRequestDto absenceRequestDto, String fromName);
    void update(AbsenceRequestDto absenceRequestDto);
    void remove(String id);
    List<AbsenceRequestDto> getAll();
    List<AbsenceRequest> getAllByAbsenceType(String absenceTypeId);
}
