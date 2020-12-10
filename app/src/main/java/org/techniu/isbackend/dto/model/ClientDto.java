package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.Address;
import org.techniu.isbackend.entity.Staff;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDto {

    private String clientId;
    private String code;
    private String logo;
    private String name;
    private String type;
    private String responsibleCommercial;
    private String assistantCommercial;
    private String multinational = "No";
    private String isActive = "Yes";
    private String phone;
    private String email;
    private String webSite;
    private String cityId;
    private String city;
    private String country;
    private String countryId;
    private String stateId;
    private String addressName;
    private String postCode;
    private String countryLeader;

    private String sector1;
    private String sector2;
    private String sector3;
    private String sectorLeader;
    //
    private Date startDateResponsibleCommercial;
    private Date endDateResponsibleCommercial;
    private Date startDateAssistantCommercial;
    private Date endDateAssistantCommercial;
}
