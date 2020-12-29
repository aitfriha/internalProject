package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactDto {
    private String contactId;
    private String civilityId;
    private String civilityName;
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
    // company
    private String companyId;
    // address
    private String cityId;
    private String countryStateId;
    private String countryId;
    private String fullAddress;
    private String cityName;
    private String stateName;
    private String countryName;
    private String postCode;
    private String suppliersArea;
    private String supplierType;

}
