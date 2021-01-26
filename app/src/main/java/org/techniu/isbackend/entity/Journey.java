package org.techniu.isbackend.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.List;

@Data()
public class Journey {

    private int order;

    @DBRef
    private Country fromCountry;

    @DBRef
    private StateCountry fromState;

    @DBRef
    private City fromCity;

    private Date departureDate;

    @DBRef
    private Country toCountry;

    @DBRef
    private StateCountry toState;

    @DBRef
    private City toCity;

    private Date arrivalDate;

    private String transportTypeId;

    private String lodgingTypeId;

    private String address;

    private List<Visit> visits;

}
