package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.techniu.isbackend.entity.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SupplierPaymentAddrequest {
    // @NotBlank(message = IVA_CODE_NOT_BLANK)
    // @NotBlank(message = IVA_NAME_NOT_BLANK)

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

    private FinancialCompany financialCompany;
    private ExternalSupplier externalSupplier;
    private Client client;
    private FinancialContract financialContract;
    private PurchaseOrder purchaseOrder;
    private Currency currency;

}
