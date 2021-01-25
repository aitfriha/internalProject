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
@Document(collection = "staffExpenseType")
@Builder
public class StaffExpenseType {
    @Id
    private String _id;
    private String code;
    private String name;
    private boolean allowSubtypes;
    private String masterValue;
    private boolean removable;
    private List<StaffExpenseSubtype> subtypes;

    @Override
    public boolean equals(Object o) {
        if (o instanceof StaffExpenseType) {
            StaffExpenseType type = (StaffExpenseType) o;
            return Objects.equals(_id, type._id);
        }
        return false;
    }
}
