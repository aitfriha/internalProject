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
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierPaymentDto {

    @NotNull
    private String supplierPaymentId;
    private String codeSupplier;
    private String supplierBill;
    private String type;
    private String typeClient;

    private float contractTradeVolume;
    private float changeFactor;
    private float contractTradeVolumeEuro;

    private Date paymentDate;
    private Date reelPaymentDate;

    @DBRef
    private FinancialCompany financialCompany;

    @DBRef
    private ExternalSupplier externalSupplier;

    @DBRef
    private Client client;

    @DBRef
    private FinancialContract financialContract;

    @DBRef
    private PurchaseOrder purchaseOrder;

    @DBRef
    private Currency currency;

}
