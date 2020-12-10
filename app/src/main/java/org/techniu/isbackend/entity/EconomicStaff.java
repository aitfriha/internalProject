package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "EconomicStaff")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Builder
public class EconomicStaff {
    @Id
    private String _id;
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
