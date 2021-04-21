package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(value = "CommercialActionType")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommercialActionType implements Serializable {

    @Id
    private String _id;
    private String typeName;
    private String description;
    private int percentage;

}
