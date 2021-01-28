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

@Document(value = "ExternalSupplier")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalSupplier implements Serializable {
    @Id
    private String _id;
    @NotNull
    private String companyName;
    @NotNull
    private String code;
    @NotNull
    private String taxNumber;
    private String email;
    private String URL;
    @NotNull
    private String firstName;
    @NotNull
    private String fatherFamilyName;
    @NotNull
    private String motherFamilyName;

    private Date creationDate;
    private Date modificationDate;

    @DBRef
    private Address address;
}
