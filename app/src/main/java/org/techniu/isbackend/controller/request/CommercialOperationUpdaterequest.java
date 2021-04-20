package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommercialOperationUpdaterequest {
    private String commercialOperationId;
    private String name;
    private String code;
    private String clientId;
    private String stateId;
    private String description;
    private String plannedDateQ;
    private String commercialFlowQ;
    private Date documentationDate;
    private Date paymentDate;
    private Date contractDate;
    private Float estimatedTradeVolume;
    private Float contractVolume;
    private String devise;
    private Float contractVolumeInEuro;
    private Float estimatedTradeVolumeInEuro;
}
