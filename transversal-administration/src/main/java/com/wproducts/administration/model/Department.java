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
public class Department {
	@Id
	private String _id;
	private String departmentCode;
	private String departmentName;
	private String departmentDescription;
	private Instant departmentCreatedAt;
	private Instant departmentUpdatedAt;

}