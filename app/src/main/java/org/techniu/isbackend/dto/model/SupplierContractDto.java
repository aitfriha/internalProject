package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierContractDto {

    @NotNull
    private String supplierContractId;
    private String name;
    private String codeContract;
    @NotNull
    private String codeSupplier;
    private String document;
    @NotNull
    private String type;
    @NotNull
    private String typeClient;

    private float contractTradeVolume;
    private float changeFactor;
    private float contractTradeVolumeEuro;

    @DBRef
    private FinancialCompany financialCompany;

    @DBRef
    private ExternalSupplier externalSupplier;

    @DBRef
    private Currency currency;

    @DBRef
    private Client client;

    @DBRef
    private FinancialContract financialContract;

    @DBRef
    private PurchaseOrder purchaseOrder;

}
