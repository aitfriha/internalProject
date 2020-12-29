package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.techniu.isbackend.entity.Currency;
import org.techniu.isbackend.entity.EconomicStaff;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EconomicStaffMonthAddrequest {
    // @NotBlank(message = IVA_CODE_NOT_BLANK)
    // @NotBlank(message = IVA_NAME_NOT_BLANK)
    private String economicStaffMonthId;
    private Date monthPayment;
    private double changeFactor;
    private double grosSalaryMonth;
    private double netSalaryMonth;
    private double contributionSalaryMonth;
    private double companyCostMonth;
    private double grosSalaryEuroMonth;
    private double netSalaryEuroMonth;
    private double contributionSalaryEuroMonth;
    private double companyCostEuroMonth;

    private EconomicStaff economicStaff;
    private Currency currency;
}
