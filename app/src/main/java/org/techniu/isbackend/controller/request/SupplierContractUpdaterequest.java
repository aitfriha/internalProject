package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.techniu.isbackend.entity.Currency;
import org.techniu.isbackend.entity.ExternalSupplier;
import org.techniu.isbackend.entity.FinancialCompany;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SupplierContractUpdaterequest {
    // @NotBlank(message = IVA_CODE_NOT_BLANK)
    // @NotBlank(message = IVA_NAME_NOT_BLANK)

    private String supplierContractId;
    private String name;
    private String codeContract;
    private String codeSupplier;
    private String document;
    private String type;

    private float contractTradeVolume;
    private float changeFactor;
    private float contractTradeVolumeEuro;

    private FinancialCompany financialCompany;
    private ExternalSupplier externalSupplier;
    private Currency currency;

}
