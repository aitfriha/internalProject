package com.internal.translation.controller.request;

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
public class TranslateSentenceUpdateRequest {

    private String translateSentenceId;

    @NotBlank(message = TRANSLATESENTENCE_COUNTRY_LANGUAGE_CODE_NOT_BLANK)
    private String countryLanguageCode;

    @NotBlank(message = TRANSLATESENTENCE_DEFAULT_SENTENCE_CODE_NOT_BLANK)
    private String defaultSentenceCode;

    @NotBlank(message = TRANSLATESENTENCE_TRANSLATION_NOT_BLANK)
    private String translation;
}

