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

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain=true)
@Document()
@Builder
public class Ability {
	@Id
	private String _id;
	private Instant abilityCreatedAt;
	private Instant abilityUpdatedAt;
	@DBRef
	private Action abilityAction;
	/*@DBRef
	private SubjectField abilityField;*/
	@DBRef
	private Subject abilitySubject;
	private Boolean abilityValue;

}