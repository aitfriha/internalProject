package com.wproducts.administration.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private String userId;
    private String userCompanyId;
    private String userNationalId;
    private String userPassportId;
    private String userEmail;
    private String userPassword;
    private String userFullName;
    private String userMobileNumber;
    private String userStatus;
    private boolean userIsActive;
    private String userCountryLanguage;
    //private boolean isAdmin;
    private Set<RoleDto> userRoles;
    private Set<String> userRolesIds;
    //private DepartmentDto userDepartment;
    private String userDepartmentId;
    private String userCreatedAt;
    private String userUpdatedAt;


}
