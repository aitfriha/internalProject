package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;


@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain = true)
@Builder
public class StaffExpenseSubtype {

    private String _id;
    private String code;
    private String name;
    private String requirement;
    private boolean validate;

    @Override
    public boolean equals(Object o) {
        if (o instanceof StaffExpenseSubtype) {
            StaffExpenseSubtype subtype = (StaffExpenseSubtype) o;
            return Objects.equals(_id, subtype._id);
        }
        return false;
    }
}
