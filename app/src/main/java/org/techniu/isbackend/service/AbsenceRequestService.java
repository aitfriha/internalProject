package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.AbsenceRequestDto;

import java.util.List;

public interface AbsenceRequestService {
    void save(AbsenceRequestDto absenceRequestDto, String sendToName, String fromName, String sendToEmail);
    void remove(String id);
    List<AbsenceRequestDto> getAll();
}
