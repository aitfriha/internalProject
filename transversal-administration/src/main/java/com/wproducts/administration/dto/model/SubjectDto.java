package com.wproducts.administration.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectDto {

    private String subjectId;
    private String subjectCode;
    private String subjectType;
    private String subjectDescription;
    private String subjectCreatedAt;
    private String subjectUpdatedAt;

    private List<SubjectFieldDto> subjectFields;
    private  SubjectDto  subjectParent;

}
