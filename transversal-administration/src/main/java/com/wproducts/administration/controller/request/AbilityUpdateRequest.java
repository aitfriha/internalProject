package com.wproducts.administration.controller.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.ACTION_ID_NOT_BLANK;
import static org.techniu.isbackend.exception.ValidationConstants.SUBJECT_ID_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbilityUpdateRequest {
    private String abilityId;
    /*private String abilityActionCode;
    private String abilityFieldCode;
    private String abilitySubjectCode;*/

    @NotBlank(message = ACTION_ID_NOT_BLANK)
    private String abilityActionId;
    @NotBlank(message = SUBJECT_ID_NOT_BLANK)
    private String abilitySubjectId;
//    private String abilityFieldId;
}

