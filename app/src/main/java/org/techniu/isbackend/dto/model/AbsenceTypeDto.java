package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.Staff;
import org.techniu.isbackend.entity.StateCountry;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsenceTypeDto {

    private String absenceTypeId;
    private String code;
    private String name;
    private String description;
    private String durationType;
    private String documentsMandatory;
    private String stateId;
    private String stateName;
    private String countryName;
    private String absenceResponsibleId;
    private String absenceResponsibleName;
    private String absenceResponsibleEmail;
    private String inCopyResponsibleId;
    private String inCopyResponsibleName;
    private String inCopyResponsibleEmail;
    private String docExtension;
    private byte[] document;

}
