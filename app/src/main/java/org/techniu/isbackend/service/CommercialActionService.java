package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.CommercialActionDto;
import org.techniu.isbackend.entity.CommercialAction;

import java.util.List;

public interface CommercialActionService {

    void saveCommercialAction(CommercialActionDto commercialActionDto);

    List<CommercialActionDto> getAllCommercialAction();

    List<CommercialAction> getAllCommercialAction2();

    CommercialAction getById(String id);

    List<CommercialAction> updateCommercialAction(CommercialActionDto commercialActionDto, String id);

    List<CommercialAction> remove(String id);
}
