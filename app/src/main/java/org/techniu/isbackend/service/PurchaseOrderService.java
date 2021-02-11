package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.PurchaseOrderDto;
import org.techniu.isbackend.entity.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {

    void savePurchaseOrder(PurchaseOrderDto purchaseOrderDto);

    List<PurchaseOrderDto> getAllPurchaseOrder();

    PurchaseOrder getById(String id);

    List<PurchaseOrderDto> updatePurchaseOrder(PurchaseOrderDto purchaseOrderDto, String id);

    List<PurchaseOrderDto> remove(String id);
}
