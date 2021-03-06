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
public class AbilityDto {
    private String abilityId;
    private ActionDto abilityAction;
    private SubjectFieldDto abilityField;
    private SubjectDto abilitySubject;
    private String abilityCreatedAt;
    private String abilityUpdatedAt;
}
