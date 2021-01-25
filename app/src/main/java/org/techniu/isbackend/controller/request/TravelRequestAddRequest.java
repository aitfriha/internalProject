package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.techniu.isbackend.dto.model.JourneyDto;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TravelRequestAddRequest {

    private String id;
    private String code;
    private String requesterId;
    private Date requestDate;
    private List<JourneyDto> journeys;
    private String requestStatusId;

}
