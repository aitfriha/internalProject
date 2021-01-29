package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.PurchaseOrderAcceptanceAddrequest;
import org.techniu.isbackend.controller.request.PurchaseOrderAcceptanceUpdaterequest;
import org.techniu.isbackend.dto.model.PurchaseOrderAcceptanceDto;
import org.techniu.isbackend.entity.PurchaseOrderAcceptance;

@Mapper(componentModel = "spring")
public interface PurchaseOrderAcceptanceMapper {
    /**
     * Map dto to model
     *
     * @param purchaseOrderAcceptanceDto purchaseOrderAcceptanceDto
     * @return PurchaseOrderAcceptance
     */
    @Mapping(source = "purchaseOrderAcceptanceId", target="_id")
    PurchaseOrderAcceptance dtoToModel(PurchaseOrderAcceptanceDto purchaseOrderAcceptanceDto);

    /**
     * Map PurchaseOrderAcceptance to PurchaseOrderAcceptanceDo
     *
     * @param purchaseOrderAcceptanceAddrequest purchaseOrderAcceptanceAddrequest
     * @return PurchaseOrderAcceptanceDto
     */
    PurchaseOrderAcceptanceDto addRequestToDto(PurchaseOrderAcceptanceAddrequest purchaseOrderAcceptanceAddrequest);

    /**
     * Map PurchaseOrderAcceptance to PurchaseOrderAcceptanceDo
     *
     * @param purchaseOrderAcceptanceUpdaterequest purchaseOrderAcceptanceUpdaterequest
     * @return PurchaseOrderAcceptanceDto
     */
    PurchaseOrderAcceptanceDto updateRequestToDto(PurchaseOrderAcceptanceUpdaterequest purchaseOrderAcceptanceUpdaterequest);

    /**
     * Map purchaseOrderAcceptance to purchaseOrderAcceptanceDo
     *
     * @param purchaseOrderAcceptance purchaseOrderAcceptance
     * @return PurchaseOrderAcceptanceDto
     */
    @Mapping(source = "_id", target="purchaseOrderAcceptanceId")
    PurchaseOrderAcceptanceDto modelToDto(PurchaseOrderAcceptance purchaseOrderAcceptance);
}
