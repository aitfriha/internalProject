package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "Staff")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Builder
public class Staff {
    @Id
    private String staffId;
    private String firstName;
    private String fatherFamilyName;
    private String motherFamilyName;
    private String personalPhone;
    private String personalEmail;
    private String companyPhone;
    private String companyMobilePhone;
    private String companyEmail;
    private String skype;
    private String birthday;
    private String birthCountry;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String photo;
    private String isFunctionalLeader;
    private String isAdministrativeLeader;

    @DBRef
    private Address address;

    @DBRef
    private List<FunctionalStructureLevel> functionalStructureLevels;

    @DBRef
    private List<AdministrativeStructureLevel> administrativeStructureLevels;

    @DBRef
    private StaffContract staffContract;

    @DBRef
    private StaffEconomicContractInformation staffEconomicContractInformation;

    @DBRef
    private List<StaffDocument> staffDocuments;

    public String getFullName() {
        return firstName + " " + fatherFamilyName + " " + motherFamilyName;
    }
}
