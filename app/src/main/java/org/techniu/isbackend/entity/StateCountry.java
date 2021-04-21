package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.techniu.isbackend.dto.model.CityDto;

import java.util.List;

@Document(value = "Commercial_State")
@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain=true)
@Builder
public class StateCountry{
    @Id
    private String _id;
    private String stateName;
    @DBRef
    private Country country;

    private List<City> cities;
}
