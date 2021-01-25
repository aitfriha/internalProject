package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain = true)
@Document(collection = "staffAssignment")
@Builder
public class StaffAssignment {

    @Id
    private String _id;

    @DBRef
    private Staff staff;

    @DBRef
    private CommercialOperation operation;

    private Date startDate;
    private Date endDate;
    private boolean active;

}
