package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.Currency;
import org.techniu.isbackend.entity.EconomicStaff;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EconomicStaffExtraDto {
    @NotNull
    private String economicStaffExtraId;
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
