package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.techniu.isbackend.entity.ClientContact;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommercialOperationAddrequest {
    private String name;
    private String code;
    private String clientId;
    private String stateId;
    private String description;
    private String objectif;
    private String plannedDateQ;
    private String commercialFlowQ;
    private Date documentationDate;
    private Date paymentDate;
    private Date contractDate;
    private List<String> serviceTypeId;

    private Float estimatedTradeVolume;
    private Float estimatedTradeVolumeInEuro;
    private String devise;
    private Float contractVolume;
    private Float contractVolumeInEuro;
    private List<String> contactsIds;
}
