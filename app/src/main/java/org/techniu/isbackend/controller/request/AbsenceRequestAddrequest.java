package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.ABSENCETYPE_CODE_NOT_BLANK;
import static org.techniu.isbackend.exception.ValidationConstants.ABSENCETYPE_NAME_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AbsenceRequestAddrequest {

    private String startDate;
    private String endDate;
    private String absenceDays;
    private String startHour;
    private String endHour;
    private String absenceHours;
    private String staffId;
    private String absenceTypeId;
    private String sendToName;
    private String fromName;
    private String sendToEmail;
}
