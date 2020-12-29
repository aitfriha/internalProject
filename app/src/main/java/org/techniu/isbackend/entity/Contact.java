package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document()
@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain=true)
@Builder
public class Contact {
    @Id
    private String _id;
    private String firstName;
    private String fatherFamilyName;
    private String motherFamilyName;
    private String department;
    private String position;
    private String companyFixPhone;
    private String companyMobilePhone;
    private String companyEmail;
    private String personalMobilePhone;
    private String personalEmail;
    private String skype;
    private String photo;
    @DBRef
    private Address address;
    @DBRef
    private Client client;
    @DBRef
    private CivilityTitle civilityTitle;
    private String suppliersArea;
    private String supplierType;
}
