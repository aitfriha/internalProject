package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(value = "ActionHistory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionHistory implements Serializable {

    @Id
    private String _id;

    private String staffName;
    private String clientName;
    private String operationName;
    private String stateName;
    private String actionTypeName;
    private String sector;

    private Date paymentDate;
    private Date actionDate;

    private float estimatedTradeVolumeInEuro;

    private String descriptions;
    private String objectifs;

    private List<String> nbrActions;
    private List<String> actionDescriptions;
    private List<String> actionDates;
    private List<String> nbrConclusions;
    private List<String> conclusions;

    @DBRef
    private List<Contact> contacts;

}
