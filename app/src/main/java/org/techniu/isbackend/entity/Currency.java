package org.techniu.isbackend.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(value = "Currency")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency implements Serializable {
    @Id
    private String _id;
    private int year;
    private int month;
    private String changeFactor;

    @DBRef
    private TypeOfCurrency typeOfCurrency;

    private Date creationDate;
    private Date modificationDate;
}
