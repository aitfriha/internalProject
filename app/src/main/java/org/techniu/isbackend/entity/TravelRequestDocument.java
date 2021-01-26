package org.techniu.isbackend.entity;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

@Data()
public class TravelRequestDocument {

    private int order;

    private String name;

    private String type;

    private Date uploadDate;

    @DBRef
    private TypeOfCurrency localCurrencyType;

    private Double localCurrencyAmount;

    @DBRef
    private TypeOfCurrency euroCurrencyType;

    private Double euroAmount;

    private Binary data;

}
