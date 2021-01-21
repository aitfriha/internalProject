package com.wproducts.administration.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectFieldDto {

    private String subjectFieldId;
    private String subjectFieldCode;
    private String subjectId;
    private String subjectCode;
    private String subjectFieldDescription;
    private String subjectFieldCreatedAt;
    private String subjectFieldUpdatedAt;
}
