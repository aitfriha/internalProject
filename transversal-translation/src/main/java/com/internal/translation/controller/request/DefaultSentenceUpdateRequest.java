package com.internal.translation.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.DEFAULTSENTENCE_CODE_NOT_BLANK;
import static org.techniu.isbackend.exception.ValidationConstants.DEFAULTSENTENCE_VALUE_NOT_BLANK;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DefaultSentenceUpdateRequest {

    @NotBlank
    private String defaultSentenceId;

    @NotBlank(message = DEFAULTSENTENCE_CODE_NOT_BLANK)
    private String code;

    @NotBlank(message = DEFAULTSENTENCE_VALUE_NOT_BLANK)
    private String value;
}

