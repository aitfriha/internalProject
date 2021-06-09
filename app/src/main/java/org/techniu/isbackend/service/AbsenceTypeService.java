package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.AbsenceTypeDto;
import org.techniu.isbackend.entity.AbsenceType;

import java.util.List;

public interface AbsenceTypeService {
    void save(AbsenceTypeDto absenceTypeDto);
    void update(AbsenceTypeDto absenceTypeDto);
    void remove(String oldId, String newId);
    List<AbsenceTypeDto> getAll();
    List<AbsenceTypeDto> getAllByState(String stateCountryId);
}
