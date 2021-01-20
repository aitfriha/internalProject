package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Document(value = "FinancialCompany")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialCompany implements Serializable {
    @Id
    private String _id;
    @NotNull
    private String name;
    private String code;
    private String taxNumber;
    private String email;
    private String phone1;
    private String phone2;
    private String logo;

    private Date creationDate;
    private Date modificationDate;

    @DBRef
    private Address address;
}
