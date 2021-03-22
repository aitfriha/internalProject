package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.SupplierPaymentDto;
import org.techniu.isbackend.entity.SupplierPayment;

import java.util.List;

public interface SupplierPaymentService {
    void saveSupplierPayment(SupplierPaymentDto supplierPaymentDto);

    List<SupplierPaymentDto> getAllSupplierPayment();

    SupplierPayment getById(String id);

    List<SupplierPaymentDto> updateSupplierPayment(SupplierPaymentDto supplierPaymentDto, String id);

    List<SupplierPaymentDto> remove(String id);
}
