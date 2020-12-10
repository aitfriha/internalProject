package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.FinancialCompany;
import org.techniu.isbackend.entity.Staff;
import org.techniu.isbackend.entity.StateCountry;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EconomicStaffAddrequest {
    // @NotBlank(message = IVA_CODE_NOT_BLANK)
    // @NotBlank(message = IVA_NAME_NOT_BLANK)
    private String economicStaffId;
    private String changeFactor;
    private String employeeNumber;
    private String name;
    private String fatherName;
    private String motherName;
    private String highDate;
    private String lowDate;
    private double grosSalary;
    private double netSalary;
    private double contributionSalary;
    private double companyCost;
    private double grosSalaryEuro;
    private double netSalaryEuro;
    private double contributionSalaryEuro;
    private double companyCostEuro;

    private Staff staff;
    private FinancialCompany financialCompany;
}
