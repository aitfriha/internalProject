package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.SupplierTypeAddrequest;
import org.techniu.isbackend.controller.request.SupplierTypeUpdaterequest;
import org.techniu.isbackend.dto.model.SupplierTypeDto;
import org.techniu.isbackend.entity.SupplierType;

@Mapper(componentModel = "spring")
public interface SupplierTypeMapper {
    /**
     * Map dto to model
     *
     * @param supplierTypeDto supplierTypeDto
     * @return SupplierType
     */
    @Mapping(source = "supplierTypeId", target="_id")
    SupplierType dtoToModel(SupplierTypeDto supplierTypeDto);

    /**
     * Map SupplierType to SupplierTypeDo
     *
     * @param supplierTypeAddrequest supplierTypeAddrequest
     * @return SupplierTypeDto
     */
    SupplierTypeDto addRequestToDto(SupplierTypeAddrequest supplierTypeAddrequest);

    /**
     * Map SupplierType to SupplierTypeDo
     *
     * @param supplierTypeUpdaterequest supplierTypeUpdaterequest
     * @return SupplierTypeDto
     */
    SupplierTypeDto updateRequestToDto(SupplierTypeUpdaterequest supplierTypeUpdaterequest);

    /**
     * Map supplierType to supplierTypeDo
     *
     * @param supplierType supplierType
     * @return SupplierTypeDto
     */
    @Mapping(source = "_id", target="supplierTypeId")
    SupplierTypeDto modelToDto(SupplierType supplierType);
}
