package com.wproducts.administration.controller.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AbilityAddRequest {
    @NotBlank(message = ACTION_ID_NOT_BLANK)
    private String abilityActionId;
    @NotBlank(message = SUBJECT_ID_NOT_BLANK)
    private String abilitySubjectId;
    private Boolean abilityValue;

}
