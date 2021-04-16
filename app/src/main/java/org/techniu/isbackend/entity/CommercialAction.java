package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gitlab.api.models.GitlabEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(value = "CommercialAction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommercialAction implements Serializable {

    @Id
    private String _id;
    private String descriptions;
    private String objectifs;

    private List<String> nbrActions;
    private List<String> actionDescriptions;
    private List<String> actionDates;

    @DBRef
    private CommercialOperation commercialOperation;

    @DBRef
    private CommercialActionType commercialActionType;

    @DBRef
    private List<Contact> contacts;

}
