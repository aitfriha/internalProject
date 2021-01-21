package com.wproducts.administration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Document
@Builder
public class User {

	@Id
	private String _id;
	private String userCompanyId;
	private String userNationalId;
	private String userPassportId;
	@Indexed(unique = true)
	private String userEmail;
	private String userPassword;
	private String userFullName;
	private String userMobileNumber;
	private String userStatus;
	private boolean userIsActive;
	private String userCountryLanguage;
	@DBRef
	private Collection<Role> userRoles;
	@DBRef
	private Department userDepartment;
	private Instant userCreatedAt;
	private Instant userUpdatedAt;

}