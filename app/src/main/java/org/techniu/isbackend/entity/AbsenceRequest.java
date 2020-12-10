package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(value = "AbsenceRequest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsenceRequest implements Serializable {

    @Id
    private String _id;
    private String startDate;
    private String endDate;
    private String absenceDays;
    private String startHour;
    private String endHour;
    private String absenceHours;
    private  byte[] document;

    @DBRef
    private Staff staff;

    @DBRef
    private AbsenceType absenceType;


}
