package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(value = "EconomicStaffYear")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EconomicStaffYear implements Serializable {
    @Id
    private String _id;
    @NotNull
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

    @DBRef
    private EconomicStaff economicStaff;

    @DBRef
    private Currency currency;

}

