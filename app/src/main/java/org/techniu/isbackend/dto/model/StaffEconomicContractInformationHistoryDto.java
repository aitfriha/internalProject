package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StaffEconomicContractInformationHistoryDto {

    private String staffEconomicContractInformationHistoryId;
    private String contractSalary;
    private String companyContractCost;
    private String expenses;
    private String companyExpensesCost;
    private String objectives;
    private String companyObjectivesCost;
    private String totalCompanyCost;
    private String contractSalaryDateGoing;
    private String contractSalaryDateOut;
    private String companyContractCostDateGoing;
    private String companyContractCostDateOut;
    private String expensesDateGoing;
    private String expensesDateOut;
    private String companyExpensesCostDateGoing;
    private String companyExpensesCostDateOut;
    private String objectivesDateGoing;
    private String objectivesDateOut;
    private String companyObjectivesCostDateGoing;
    private String companyObjectivesCostDateOut;
    private String totalCompanyCostDateGoing;
    private String totalCompanyCostDateOut;
    private String updatedAt;

    // Currency
    private String currencyId;
    private String currencyCode;
    private String currencyName;
    private int currencyYear;
    private int currencyMonth;
    private String changeFactor;
}
