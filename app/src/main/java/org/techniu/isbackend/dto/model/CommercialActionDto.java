package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.CommercialActionType;
import org.techniu.isbackend.entity.CommercialOperation;
import org.techniu.isbackend.entity.Contact;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommercialActionDto {

    @NotNull
    private String commercialActionId;

    private String descriptions;
    private String objectifs;

    private List<String> nbrActions;
    private List<String> actionDescriptions;
    private List<String> actionDates;
    private List<LinkedHashMap> contactsIds;
    private List<ContactDto> contactsDtos;

    @DBRef
    private CommercialOperation commercialOperation;

    @DBRef
    private CommercialActionType commercialActionType;

}
