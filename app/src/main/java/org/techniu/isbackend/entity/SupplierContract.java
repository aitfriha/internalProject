package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(value = "SupplierContract")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierContract implements Serializable {

    @Id
    private String _id;
    private String name;
    private String codeContract;
    private String codeSupplier;
    private String document;
    private String type;

    private float contractTradeVolume;
    private float changeFactor;
    private float contractTradeVolumeEuro;

    @DBRef
    private FinancialCompany financialCompany;

    @DBRef
    private ExternalSupplier externalSupplier;

    @DBRef
    private Currency currency;

}
