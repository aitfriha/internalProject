package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(value = "SupplierPayment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierPayment implements Serializable {

    @Id
    private String _id;
    private String codeSupplier;
    private String supplierBill;
    private String type;
    private String typeClient;

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

}
