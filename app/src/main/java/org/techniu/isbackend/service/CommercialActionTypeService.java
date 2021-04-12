package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.CommercialActionTypeDto;
import org.techniu.isbackend.entity.CommercialActionType;

import java.util.List;

public interface CommercialActionTypeService {
    void saveCommercialActionType(CommercialActionTypeDto typeOfCurrencyDto);

    List<CommercialActionTypeDto> getAllCommercialActionType();

    CommercialActionType getById(String id);

    List<CommercialActionTypeDto> updateCommercialActionType(CommercialActionTypeDto typeOfCurrencyDto, String id);

    List<CommercialActionTypeDto> remove(String id);
}
