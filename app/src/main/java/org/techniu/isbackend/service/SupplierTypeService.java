package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.SupplierTypeDto;
import org.techniu.isbackend.entity.SupplierType;

import java.util.List;

public interface SupplierTypeService {
    void saveSupplierType(SupplierTypeDto supplierTypeDto);

    List<SupplierTypeDto> getAllSupplierType();

    SupplierType getById(String id);

    List<SupplierTypeDto> updateSupplierType(SupplierTypeDto supplierTypeDto, String id);

    List<SupplierTypeDto> remove(String id);
}
