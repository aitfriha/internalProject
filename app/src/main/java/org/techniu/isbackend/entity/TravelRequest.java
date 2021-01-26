package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain = true)
@Document(collection = "travelRequest")
@Builder
public class TravelRequest {

    private String _id;

    private String code;

    @DBRef
    private Staff requester;

    private Date requestDate;

    private List<Journey> journeys;

    private List<TravelRequestDocument> documents;

    @DBRef
    private RequestStatus status;

}
