package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.SelectionProcessInformationDto;

import java.util.List;

public interface SelectionProcessInformationService {
    void save(SelectionProcessInformationDto selectionProcessInformationDto, List<String> knowledgeIdList);
    void update(SelectionProcessInformationDto selectionProcessInformationDto, List<String> knowledgeIdList);
    void remove(String id);
    List<SelectionProcessInformationDto> getAll();
}
