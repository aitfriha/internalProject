package com.wproducts.administration.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

import static org.techniu.isbackend.exception.ValidationConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserAddRequest {

    private String userId;
    private String userCompanyId;
    private String userNationalId;
    private String userPassportId;
    private String userCreatedAt;
    private String userUpdatedAt;

    @NotBlank(message = USER_EMAIL_NOT_BLANK)
    private String userEmail;
    //@NotBlank(message = USER_PASSWORD_NOT_BLANK)
    private String userPassword;
    @NotBlank(message = USER_FULL_NAME_NOT_BLANK)
    private String userFullName;
//    @NotBlank(message = USER_MOBILE_NUMBER_NOT_BLANK)
    private String userMobileNumber;
//    @NotBlank(message = USER_STATUS_NOT_BLANK)
    private String userStatus;
    @NotNull(message = USER_ACTIVE_NOT_NULL)
    private boolean userIsActive;
   // @NotBlank(message = USER_COUNTRY_LANGUAGE_NOT_BLANK)
   // private String userCountryLanguage;
     @NotEmpty(message = USER_ROLES_NOT_EMPTY)
     private Set<String> userRolesIds;
    //private Set<String> userRoles;
//    @NotBlank(message = USER_DEPARTMENT_NOT_BLANK)
    private String userDepartmentId;
}
