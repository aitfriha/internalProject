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

@Document(value = "EconomicStaffMonth")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EconomicStaffMonth implements Serializable {
    @Id
    private String _id;
    @NotNull
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

    @DBRef
    private EconomicStaff economicStaff;

    @DBRef
    private Currency currency;

}

