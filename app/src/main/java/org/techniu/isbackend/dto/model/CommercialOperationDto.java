package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.Client;
import org.techniu.isbackend.entity.CommercialOperationStatus;
import org.techniu.isbackend.entity.ServiceType;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommercialOperationDto {

    private String commercialOperationId;
    private String name;
    private String code;
    private String clientId;
    private String stateId;
    private String stateName;
    private String countryName;
    private String description;
    private String plannedDateQ;
    private String commercialFlowQ;
    private Date documentationDate;
    private Date paymentDate;
    private Date contractDate;
    private List<String> serviceTypeId;
    private List<String> serviceTypeName;

    private Float estimatedTradeVolume;
    private Float contractVolume;
    private String devise;
    private Float contractVolumeInEuro;
    private Float estimatedTradeVolumeInEuro;
    private String clientName;
    private String sector1;
    private String sector2;
    private String sector3;
    private List<String> contactsIds;
    private List<ContactDto> contactDtos;
}
