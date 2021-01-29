package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.PurchaseOrderAcceptanceDto;
import org.techniu.isbackend.entity.PurchaseOrderAcceptance;

import java.util.List;

public interface PurchaseOrderAcceptanceService {
    void savePurchaseOrderAcceptance(PurchaseOrderAcceptanceDto purchaseOrderAcceptanceDto);

    List<PurchaseOrderAcceptanceDto> getAllPurchaseOrderAcceptance();

    PurchaseOrderAcceptance getById(String id);

    List<PurchaseOrderAcceptanceDto> updatePurchaseOrderAcceptance(PurchaseOrderAcceptanceDto purchaseOrderAcceptanceDto, String id);

    List<PurchaseOrderAcceptanceDto> remove(String id);
}
