package com.internal.translation.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TranslateSentenceAddListRequest {
    private String countryLanguageCode;
    private Map<String, String> translateSentencesList;
}
