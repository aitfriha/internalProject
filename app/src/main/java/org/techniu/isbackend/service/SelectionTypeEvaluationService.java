package org.techniu.isbackend.service;

import org.springframework.http.ResponseEntity;
import org.techniu.isbackend.dto.model.SelectionTypeEvaluationDto;
import org.techniu.isbackend.entity.SelectionTypeEvaluation;
import org.techniu.isbackend.entity.Staff;

import java.util.List;

public interface SelectionTypeEvaluationService {
    void save(List<SelectionTypeEvaluationDto> selectionTypeEvaluationDtos);
    void update(SelectionTypeEvaluationDto selectionTypeEvaluationDto);
    SelectionTypeEvaluation getSelectionTypeByName(String name);
    ResponseEntity<?> remove(String SelectionTypeId);
    List<SelectionTypeEvaluationDto> getAll();
    List<SelectionTypeEvaluationDto> getAllByType(String type);
    List<Staff> setLevelStaffs(List<Object> staffs);
    List<SelectionTypeEvaluation> getFunctionalStructureTree(String SelectionTypeId);

}
