package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class TravelRequestDocumentDto {

    private int order;

    private String name;

    private String type;

    private Date uploadDate;

    //Local Currency Data
    private String localCurrencyTypeId;
    private String localCurrencyTypeCode;
    private String localCurrencyTypeName;

    private Double localCurrencyAmount;

    //Euro Currency Data
    private String euroCurrencyTypeId;
    private String euroCurrencyTypeCode;
    private String euroCurrencyTypeName;

    private Double euroAmount;

}
