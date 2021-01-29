package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(value = "PurchaseOrderAcceptance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderAcceptance implements Serializable {

    @Id
    private String _id;
    private String generatedPurchase;
    private String adminAcceptance;
    private String operationalAcceptance;

}
