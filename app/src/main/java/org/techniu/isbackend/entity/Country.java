package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.techniu.isbackend.dto.model.StateCountryDto;

import java.io.Serializable;
import java.util.List;

@Document(value = "Commercial_Country")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country  {
    @Id
    private String countryId;
    private String countryName;
    private String phonePrefix;
    private String countryCode;

    private List<StateCountryDto> stateCountryList;
}
