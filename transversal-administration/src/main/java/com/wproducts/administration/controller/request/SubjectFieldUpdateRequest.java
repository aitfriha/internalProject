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
public class SubjectFieldUpdateRequest {
    private String subjectFieldId;
    @NotBlank(message = FIELD_CODE_NOT_BLANK)
    private String subjectFieldCode;
    private String subjectFieldDescription;
}
