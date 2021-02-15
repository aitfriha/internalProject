package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.PurchaseOrderAddrequest;
import org.techniu.isbackend.controller.request.PurchaseOrderUpdaterequest;
import org.techniu.isbackend.dto.model.PurchaseOrderDto;
import org.techniu.isbackend.entity.PurchaseOrder;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {
    /**
     * Map dto to model
     *
     * @param purchaseOrderDto purchaseOrderDto
     * @return PurchaseOrder
     */
    @Mapping(source = "purchaseOrderId", target="_id")
    PurchaseOrder dtoToModel(PurchaseOrderDto purchaseOrderDto);

    /**
     * Map PurchaseOrder to PurchaseOrderDo
     *
     * @param purchaseOrderAddrequest purchaseOrderAddrequest
     * @return PurchaseOrderDto
     */
    PurchaseOrderDto addRequestToDto(PurchaseOrderAddrequest purchaseOrderAddrequest);

    /**
     * Map PurchaseOrder to PurchaseOrderDo
     *
     * @param purchaseOrderUpdaterequest purchaseOrderUpdaterequest
     * @return PurchaseOrderDto
     */
    PurchaseOrderDto updateRequestToDto(PurchaseOrderUpdaterequest purchaseOrderUpdaterequest);

    /**
     * Map purchaseOrder to purchaseOrderDo
     *
     * @param purchaseOrder purchaseOrder
     * @return PurchaseOrderDto
     */
    @Mapping(source = "_id", target="purchaseOrderId")
    PurchaseOrderDto modelToDto(PurchaseOrder purchaseOrder);
}
