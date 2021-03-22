package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.SupplierPaymentAddrequest;
import org.techniu.isbackend.controller.request.SupplierPaymentUpdaterequest;
import org.techniu.isbackend.dto.model.SupplierPaymentDto;
import org.techniu.isbackend.entity.SupplierPayment;

@Mapper(componentModel = "spring")
public interface SupplierPaymentMapper {
    /**
     * Map dto to model
     *
     * @param supplierPaymentDto supplierPaymentDto
     * @return SupplierPayment
     */
    @Mapping(source = "supplierPaymentId", target="_id")
    SupplierPayment dtoToModel(SupplierPaymentDto supplierPaymentDto);

    /**
     * Map SupplierPayment to SupplierPaymentDo
     *
     * @param supplierPaymentAddrequest supplierPaymentAddrequest
     * @return SupplierPaymentDto
     */
    SupplierPaymentDto addRequestToDto(SupplierPaymentAddrequest supplierPaymentAddrequest);

    /**
     * Map SupplierPayment to SupplierPaymentDo
     *
     * @param supplierPaymentUpdaterequest supplierPaymentUpdaterequest
     * @return SupplierPaymentDto
     */
    SupplierPaymentDto updateRequestToDto(SupplierPaymentUpdaterequest supplierPaymentUpdaterequest);

    /**
     * Map supplierPayment to supplierPaymentDo
     *
     * @param supplierPayment supplierPayment
     * @return SupplierPaymentDto
     */
    @Mapping(source = "_id", target="supplierPaymentId")
    SupplierPaymentDto modelToDto(SupplierPayment supplierPayment);
}
