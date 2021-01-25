package org.techniu.isbackend.entity;

import lombok.Data;

import java.util.List;

@Data
public class WeeklyReport {

   private String weekOfYear;
   private Staff staff;
   private List<WeeklyWork> works;
   private List<AbsenceRequest> absences;
   private List<LocalBankHoliday> localBankHolidays;
}
