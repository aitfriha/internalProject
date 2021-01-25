package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;


@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain = true)
@Document(collection = "businessExpenseType")
@Builder
public class BusinessExpenseType {
    @Id
    private String _id;
    private String code;
    private String name;
    private boolean allowSubtypes;
    private String masterValue;
    private boolean removable;
    private List<BusinessExpenseSubtype> subtypes;

    @Override
    public boolean equals(Object o) {
        if (o instanceof BusinessExpenseType) {
            BusinessExpenseType type = (BusinessExpenseType) o;
            return Objects.equals(_id, type._id);
        }
        return false;
    }
}
