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
public class EconomicStaffExtraAddrequest {
    private String economicStaffExtraId;
    private Date extraordinaryDate;
    private double changeFactor;
    private double extraordinaryExpenses;
    private double extraordinaryExpensesEuro;
    private double extraordinaryObjectives;
    private double extraordinaryObjectivesEuro;

    private EconomicStaff economicStaff;
    private Currency currency;
}
