package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class WeeklyWorkDto {

    private String id;
    private int year;
    private int week;
    //Customer data
    private String customerId;
    private String customerCode;
    private String customerName;
    //Operation data
    private String operationId;
    private String operationCode;
    private String operationName;
    //AssignmentType data
    private String assignmentTypeId;
    private String assignmentTypeCode;
    private String assignmentTypeName;
    private String deliverable;
    private double monday;
    private double tuesday;
    private double wednesday;
    private double thursday;
    private double friday;
    private Date registerDate;
}
