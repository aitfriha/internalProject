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
public class ActionHistoryUpdaterequest {
    // @NotBlank(message = IVA_CODE_NOT_BLANK)
    // @NotBlank(message = IVA_NAME_NOT_BLANK)
    private String actionHistoryId;
    private String staffName;
    private String clientName;
    private String operationName;
    private String stateName;
    private String actionTypeName;
    private String sector;
    private Date paymentDate;
    private Date actionDate;
    private float estimatedTradeVolumeInEuro;
}
