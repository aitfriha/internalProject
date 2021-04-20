package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.CommercialActionDto;
import org.techniu.isbackend.entity.CommercialAction;

import java.util.List;

public interface CommercialActionService {

    void saveCommercialAction(CommercialActionDto commercialActionDto);

    List<CommercialActionDto> getAllCommercialAction();

    CommercialAction getById(String id);

    List<CommercialActionDto> updateCommercialAction(CommercialActionDto commercialActionDto, String id);

    List<CommercialActionDto> remove(String id);
}
