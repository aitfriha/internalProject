package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionHistoryDto {

    @NotNull
    private String actionHistoryId;
    @NotNull
    private String staffName;
    private String clientName;
    @NotNull
    private String operationName;
    @NotNull
    private String stateName;
    @NotNull
    private String actionTypeName;
    private String sector;

    private Date paymentDate;
    private Date actionDate;

    @NotNull
    private float estimatedTradeVolumeInEuro;

    private String descriptions;
    private String objectifs;

    private List<String> nbrActions;
    private List<String> actionDescriptions;
    private List<String> actionDates;
    private List<String> nbrConclusions;
    private List<String> conclusions;

    private List<LinkedHashMap> contactsIds;
    private List<ContactDto> contactsDtos;
}
