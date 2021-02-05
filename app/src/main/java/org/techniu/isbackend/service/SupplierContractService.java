package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.SupplierContractDto;
import org.techniu.isbackend.entity.SupplierContract;

import java.util.List;

public interface SupplierContractService {
    void saveSupplierContract(SupplierContractDto supplierContractDto);

    List<SupplierContractDto> getAllSupplierContract();

    SupplierContract getById(String id);

    List<SupplierContractDto> updateSupplierContract(SupplierContractDto supplierContractDto, String id);

    List<SupplierContractDto> remove(String id);
}
