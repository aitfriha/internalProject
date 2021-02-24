package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(value = "PurchaseOrder")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {

    @Id
    private String _id;
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
    private String typeClient;

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


    @DBRef
    private FinancialCompany companyEmit;

    @DBRef
    private FinancialCompany internalSupplierReception;

    @DBRef
    private ExternalSupplier externalSupplierReception;

    @DBRef
    private Iva iva;

    @DBRef
    private Currency currency;

    @DBRef
    private Client client;

    @DBRef
    private FinancialContract financialContract;

    private Date creationDate;
    private Date modificationDate;

}
