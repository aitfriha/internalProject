package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain = true)
@Document(collection = "travelRequestEmailAddress")
@Builder
public class TravelRequestEmailAddress {
    @Id
    private String _id;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (o instanceof TravelRequestEmailAddress) {
            TravelRequestEmailAddress email = (TravelRequestEmailAddress) o;
            return Objects.equals(_id, email._id);
        }
        return false;
    }
}
