package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class WeeklyReportDto {

    private String weekOfYear;
    //Staff data
    private String employeeId;
    private String personalNumber;
    private String avatar;
    private String name;
    private String fatherFamilyName;
    private String motherFamilyName;
    private String company;
    private String companyEmail;
    private List<WeeklyWorkDto> works;
    private List<AbsenceRequestDto> absences;
    private List<LocalBankHolidayDto> localBankHolidays;
}
