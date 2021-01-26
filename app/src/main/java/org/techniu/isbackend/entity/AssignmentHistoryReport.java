package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain = true)
@Builder
public class AssignmentHistoryReport {

    private String id;
    //Customer data
    private String customerCode;
    private String customerName;
    //Staff data
    private String personalNumber;
    private String name;
    private String fatherFamilyName;
    private String motherFamilyName;
    private String company;
    private String companyEmail;
    //Operation data
    private String operationCode;
    private String operationName;
    private Date startDate;
    private Date endDate;
}
