package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PurchaseOrderAddrequest {
    // @NotBlank(message = IVA_CODE_NOT_BLANK)
    // @NotBlank(message = IVA_NAME_NOT_BLANK)
    private String purchaseOrderId;
    private String purchaseNumber;
    private String factor;
    private String companyNIF;
    private String companyAddress;
    private String companyLogo;
    private String receptionSupplierType;
    private String internLogo;
    private String supplierAddress;
    private String supplierNIF;
    private String supplierResponsible;
    private String paymentMethod;

    private List<String> termDescription;
    private List<String> termTitle;
    private List<String> termsListe;

    private List<String> nbrConcepts;
    private List<String> itemNames;
    private List<String> description;
    private List<String> unityValue;
    private List<String> unity;
    private List<String> unityNumber;
    private List<String> valor;
    private List<String> paymentDate;
    private List<String> givingDate;
    private List<String> billingDate;

    private float totalEuro;
    private float totalLocal;
    private float valueIVAEuro;
    private float valueIVALocal;
    private float totalAmountEuro;
    private float totalAmountLocal;
    private float ivaRetentions;
    private float totalAmountRetentions;
    private float totalIvaRetention;

    private FinancialCompany companyEmit;
    private FinancialCompany internalSupplierReception;
    private ExternalSupplier externalSupplierReception;
    private Iva iva;
    private Currency currency;

}
