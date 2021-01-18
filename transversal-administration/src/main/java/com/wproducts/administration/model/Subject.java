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
import java.util.List;
import java.util.Optional;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain=true)
@Document()
@Builder
public class Subject {
	@Id
	private String _id;
	private String subjectCode;
	private String subjectType;
	private String subjectDescription;
	private Instant subjectCreatedAt;
	private Instant subjectUpdatedAt;
   /* @DBRef
	private List<SubjectField>  subjectFields;*/
	@DBRef
	private Subject  subjectParent;
}