package org.techniu.isbackend.entity;

import lombok.Data;

import java.util.Date;

@Data()
public class SummarizedWeeklyReport {

   private int year;
   private int week;
   private String weekOfYear;
   private String employeeId;
   private String employee;
   private String companyEmail;
   private String assignedCost;
   private String concept;
   private String deliverable;
   private String assignmentType;
   private double days;
   private Date registerDate;
   private boolean editable;
   
}
