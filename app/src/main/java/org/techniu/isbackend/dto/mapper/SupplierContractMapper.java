package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.SupplierContractAddrequest;
import org.techniu.isbackend.controller.request.SupplierContractUpdaterequest;
import org.techniu.isbackend.dto.model.SupplierContractDto;
import org.techniu.isbackend.entity.SupplierContract;

@Mapper(componentModel = "spring")
public interface SupplierContractMapper {
    /**
     * Map dto to model
     *
     * @param supplierContractDto supplierContractDto
     * @return SupplierContract
     */
    @Mapping(source = "supplierContractId", target="_id")
    SupplierContract dtoToModel(SupplierContractDto supplierContractDto);

    /**
     * Map SupplierContract to SupplierContractDo
     *
     * @param supplierContractAddrequest supplierContractAddrequest
     * @return SupplierContractDto
     */
    SupplierContractDto addRequestToDto(SupplierContractAddrequest supplierContractAddrequest);

    /**
     * Map SupplierContract to SupplierContractDo
     *
     * @param supplierContractUpdaterequest supplierContractUpdaterequest
     * @return SupplierContractDto
     */
    SupplierContractDto updateRequestToDto(SupplierContractUpdaterequest supplierContractUpdaterequest);

    /**
     * Map supplierContract to supplierContractDo
     *
     * @param supplierContract supplierContract
     * @return SupplierContractDto
     */
    @Mapping(source = "_id", target="supplierContractId")
    SupplierContractDto modelToDto(SupplierContract supplierContract);
}
