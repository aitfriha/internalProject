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
public class EconomicStaffYearUpdaterequest {
    // @NotBlank(message = IVA_CODE_NOT_BLANK)
    // @NotBlank(message = IVA_NAME_NOT_BLANK)
    private String economicStaffYearId;
    private Date yearPayment;
    private double changeFactor;
    private double grosSalaryYear;
    private double netSalaryYear;
    private double contributionSalaryYear;
    private double companyCostYear;
    private double grosSalaryEuroYear;
    private double netSalaryEuroYear;
    private double contributionSalaryEuroYear;
    private double companyCostEuroYear;

    private EconomicStaff economicStaff;
    private Currency currency;
}
