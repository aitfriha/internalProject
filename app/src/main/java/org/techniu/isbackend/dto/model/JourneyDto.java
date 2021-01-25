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

public class JourneyDto {

    private int order;

    //From country data
    private String fromCountryId;
    private String fromCountryCode;
    private String fromCountryName;

    //From state data
    private String fromStateId;
    private String fromStateName;

    //From city data
    private String fromCityId;
    private String fromCityName;

    private Date departureDate;

    //To country data
    private String toCountryId;
    private String toCountryCode;
    private String toCountryName;

    //To state data
    private String toStateId;
    private String toStateName;

    //To city data
    private String toCityId;
    private String toCityName;

    private Date arrivalDate;

    private String transportTypeId;
    private String transportTypeName;
    private String lodgingTypeId;
    private String lodgingTypeName;
    private String address;

    private List<VisitDto> visits;


}
