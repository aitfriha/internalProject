package org.techniu.isbackend.controller.request;

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
public class AdministrativeStructureLevelUpdaterequest {

    private String levelId;
    @NotBlank(message = ADMINISTRATIVESTRUCTURELEVEL_NAME_NOT_BLANK)
    private String name;
    private String description;
    @NotBlank(message = ADMINISTRATIVESTRUCTURELEVEL_TYPE_NOT_BLANK)
    private String type;
    private String oldLeaderId;
    private String newLeaderId;
    private String companyId;
}
