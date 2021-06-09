package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.ActionHistoryDto;
import org.techniu.isbackend.entity.ActionHistory;

import java.util.List;

public interface ActionHistoryService {
    void saveActionHistory(ActionHistoryDto actionHistoryDto);

    List<ActionHistoryDto> getAllActionHistory();

    ActionHistory getById(String id);

    List<ActionHistoryDto> updateActionHistory(ActionHistoryDto actionHistoryDto, String id);

    List<ActionHistoryDto> remove(String id);
}
