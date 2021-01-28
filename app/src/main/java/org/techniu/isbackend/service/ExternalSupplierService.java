package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.ExternalSupplierDto;
import org.techniu.isbackend.entity.ExternalSupplier;

import java.util.List;

public interface ExternalSupplierService {
    void saveExternalSupplier(ExternalSupplierDto externalSupplierDto);

    List<ExternalSupplierDto> getAllExternalSupplier();

    ExternalSupplier getById(String id);

    List<ExternalSupplierDto> updateExternalSupplier(ExternalSupplierDto externalSupplierDto, String id);

    List<ExternalSupplierDto> remove(String id);
}
