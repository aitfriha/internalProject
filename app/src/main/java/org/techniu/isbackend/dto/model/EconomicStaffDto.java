package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.FinancialCompany;
import org.techniu.isbackend.entity.Staff;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EconomicStaffDto {
    @NotNull
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

    @DBRef
    private Staff staff;

    @DBRef
    private FinancialCompany financialCompany;
}
