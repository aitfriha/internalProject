package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(value = "SupplierType")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierType implements Serializable {

    @Id
    private String _id;
    private String name;
    private String description;
    private boolean operationAssociated;
    private boolean internalOrder;

}
