package org.techniu.isbackend.dto.model;

import lombok.Data;

@Data()
public class VisitDto {

   private String customerId;
   private String customerCode;
   private String customerName;
   private String operationId;
   private String operationCode;
   private String operationName;
   
}
