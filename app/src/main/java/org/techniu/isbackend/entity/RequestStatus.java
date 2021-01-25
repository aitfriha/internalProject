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
@Document(collection = "requestStatus")
@Builder
public class RequestStatus {
    @Id
    private String _id;
    private String code;
    private String name;
    private String description;
    private String masterValue;
    private boolean removable;

    @Override
    public boolean equals(Object o) {
        if (o instanceof RequestStatus) {
            RequestStatus requestStatus = (RequestStatus) o;
            return Objects.equals(_id, requestStatus._id);
        }
        return false;
    }
}
