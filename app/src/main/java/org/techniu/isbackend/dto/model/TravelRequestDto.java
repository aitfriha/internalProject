package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class TravelRequestDto {

    private String id;
    private String code;

    //Requester data
    private String requesterId;
    private String requesterPersonalNumber;
    private String requesterAvatar;
    private String requesterName;
    private String requesterFatherFamilyName;
    private String requesterMotherFamilyName;
    private String requesterCompany;
    private String requesterCompanyEmail;

    private Date requestDate;

    List<JourneyDto> journeys;

    //RequestStatus data
    private String requestStatusId;
    private String requestStatusCode;
    private String requestStatusName;
    private String requestStatusMasterValue;

    private Date firstDepartureDate;
    private Date lastArrivalDate;
    private int days;

    List<TravelRequestDocumentDto> documents;

}
