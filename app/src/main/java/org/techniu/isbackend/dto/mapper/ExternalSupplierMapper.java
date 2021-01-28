package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.ExternalSupplierAddrequest;
import org.techniu.isbackend.controller.request.ExternalSupplierUpdaterequest;
import org.techniu.isbackend.dto.model.ExternalSupplierDto;
import org.techniu.isbackend.entity.ExternalSupplier;

@Mapper(componentModel = "spring")
public interface ExternalSupplierMapper {
    /**
     * Map dto to model
     *
     * @param externalSupplierDto externalSupplierDto
     * @return ExternalSupplier
     */
    @Mapping(source = "externalSupplierId", target="_id")
    ExternalSupplier dtoToModel(ExternalSupplierDto externalSupplierDto);

    /**
     * Map ExternalSupplier to ExternalSupplierDo
     *
     * @param externalSupplierAddrequest externalSupplierAddrequest
     * @return ExternalSupplierDto
     */
    ExternalSupplierDto addRequestToDto(ExternalSupplierAddrequest externalSupplierAddrequest);

    /**
     * Map ExternalSupplier to ExternalSupplierDo
     *
     * @param externalSupplierUpdaterequest externalSupplierUpdaterequest
     * @return ExternalSupplierDto
     */
    ExternalSupplierDto updateRequestToDto(ExternalSupplierUpdaterequest externalSupplierUpdaterequest);

    /**
     * Map externalSupplier to externalSupplierDo
     *
     * @param externalSupplier externalSupplier
     * @return ExternalSupplierDto
     */
    @Mapping(source = "_id", target="externalSupplierId")
    ExternalSupplierDto modelToDto(ExternalSupplier externalSupplier);
}
