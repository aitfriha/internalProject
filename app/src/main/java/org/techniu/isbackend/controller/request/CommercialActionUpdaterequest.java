package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.techniu.isbackend.entity.CommercialActionType;
import org.techniu.isbackend.entity.CommercialOperation;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommercialActionUpdaterequest {

    private String commercialActionId;
    private String descriptions;
    private String objectifs;

    private List<String> nbrActions;
    private List<String> actionDescriptions;
    private List<String> actionDates;
    private List<Object> contactsIds;

    private CommercialOperation commercialOperation;
    private CommercialActionType commercialActionType;

}
