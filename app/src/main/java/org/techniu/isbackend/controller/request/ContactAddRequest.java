package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ContactAddRequest {
    private String civilityId;
    @NotBlank(message = CONTACT_FIRSTNAME_NOT_BLANK)
    private String firstName;
    private String fatherFamilyName;
    private String motherFamilyName;
    private String department;
    @NotBlank(message = CONTACT_POSITION_NOT_BLANK)
    private String position;
    private String companyFixPhone;
    private String companyMobilePhone;
    @NotBlank(message = CONTACT_COMPANYEMAIL_NOT_BLANK)
    private String companyEmail;
    private String personalMobilePhone;
    @NotBlank(message = CONTACT_PERSONALEMAIL_NOT_BLANK)
    private String personalEmail;
    private String skype;
    private String photo;
    // company
    @NotBlank(message = CONTACT_COMPANY_NOT_BLANK)
    private String companyId;
    // address
    @NotBlank(message = CONTACT_CITY_NOT_BLANK)
    private String cityId;
    private String fullAddress;
    private String postCode;
    private String suppliersArea;
    private String supplierType;
}
