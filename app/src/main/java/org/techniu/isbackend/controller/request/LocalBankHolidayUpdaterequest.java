package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.*;
import static org.techniu.isbackend.exception.ValidationConstants.LOCALBANKHOLIDAY_CODE_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LocalBankHolidayUpdaterequest {

    private String localBankHolidayId;
    @NotBlank(message = LOCALBANKHOLIDAY_NAME_NOT_BLANK)
    private String name;
    @NotBlank(message = LOCALBANKHOLIDAY_CODE_NOT_BLANK)
    private String code;
    private String startDate;
    private String endDate;
    private String totalDays;
}
