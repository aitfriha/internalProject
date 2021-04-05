package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Document(value = "Commercial_Operation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommercialOperation{
    @Id
    private String _id;
    private String name;
    private String code;
    @DBRef
    private Client client;
    @DBRef
    private CommercialOperationStatus state;
    private String description;
    private String plannedDateQ;
    private String commercialFlowQ;
    private Date documentationDate;
    private Date paymentDate;
    private Date contractDate;
    @DBRef
    private List<ServiceType> serviceType;

    private Float estimatedTradeVolume;
    private Float contractVolume;
    private String devise;
    private Float contractVolumeInEuro;
    private Float estimatedTradeVolumeInEuro;
   // private int progress;
    @DBRef
    private List<Contact> Contacts;



}
