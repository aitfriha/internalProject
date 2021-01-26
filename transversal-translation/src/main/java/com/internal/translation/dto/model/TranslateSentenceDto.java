package com.internal.translation.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class TranslateSentenceDto {
    private String translateSentenceId;
    private String defaultSentenceCode;
    private String countryLanguageCode;
    private String defaultSentenceValue;
    private String translation;
    private Map<String, String> translateSentencesList;

}
