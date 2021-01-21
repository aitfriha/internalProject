package com.wproducts.administration.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain=true)
@Document()
@Builder
public class Action {
	@Id
	private String _id;
	private String actionCode;
	private String actionConcerns;
	private String actionDescription;
	private Instant actionCreatedAt;
	private Instant actionUpdatedAt;

}