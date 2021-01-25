package org.techniu.isbackend.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data()
public class Visit {

   @DBRef
   private Client customer;
   @DBRef
   private CommercialOperation operation;
   
}
