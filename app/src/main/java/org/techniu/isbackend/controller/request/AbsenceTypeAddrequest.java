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
public class AbsenceTypeAddrequest {

    @NotBlank(message = ABSENCETYPE_CODE_NOT_BLANK)
    private String code;
    @NotBlank(message = ABSENCETYPE_NAME_NOT_BLANK)
    private String name;
    private String description;
    private String durationType;
    private String documentsMandatory;
    private String stateId;
    private String absenceResponsibleId;
    private String inCopyResponsibleId;
    private String docExtension;

}
