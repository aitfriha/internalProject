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

@Document(value = "EconomicStaffExtra")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EconomicStaffExtra implements Serializable {
    @Id
    private String _id;
    @NotNull
    private Date extraordinaryDate;
    private double changeFactor;
    private double extraordinaryExpenses;
    private double extraordinaryExpensesEuro;
    private double extraordinaryObjectives;
    private double extraordinaryObjectivesEuro;

    @DBRef
    private EconomicStaff economicStaff;

    @DBRef
    private Currency currency;

}

