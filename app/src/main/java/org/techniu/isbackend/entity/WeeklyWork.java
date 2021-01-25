package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain = true)
@Document(collection = "weeklyWork")
@Builder
public class WeeklyWork {

    private String _id;
    private int year;
    private int week;

    @DBRef
    private Client customer;

    @DBRef
    private CommercialOperation operation;

    private String deliverable;

    @DBRef
    private Staff staff;

    @DBRef
    private AssignmentType assignmentType;

    private double monday;
    private double tuesday;
    private double wednesday;
    private double thursday;
    private double friday;
    private Date registerDate;

}
