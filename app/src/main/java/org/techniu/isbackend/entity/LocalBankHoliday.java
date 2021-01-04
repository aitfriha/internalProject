package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(value = "LocalBankHoliday")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalBankHoliday implements Serializable {

    @Id
    private String _id;
    private String name;
    private String code;
    private String startDate;
    private String endDate;
    private String totalDays;

    @DBRef
    private FinancialCompany company;

}
