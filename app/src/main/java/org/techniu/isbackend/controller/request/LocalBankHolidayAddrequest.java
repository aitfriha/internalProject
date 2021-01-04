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
public class LocalBankHolidayAddrequest {

    @NotBlank(message = LOCALBANKHOLIDAY_NAME_NOT_BLANK)
    private String name;
    @NotBlank(message = LOCALBANKHOLIDAY_CODE_NOT_BLANK)
    private String code;
    private String startDate;
    private String endDate;
    private String totalDays;
    private String financialCompanyId;

}
