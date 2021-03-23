package com.wproducts.administration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Document
@Builder
public class Role {

	@Id
	private String _id;
	private String roleName;
	private String roleDescription;
	/*@DBRef
	private Collection<Action> roleActions;*/
	Map<String,Boolean> actionsNames = new HashMap<String,Boolean>();
	private Instant roleCreatedAt;
	private Instant roleUpdatedAt;

}