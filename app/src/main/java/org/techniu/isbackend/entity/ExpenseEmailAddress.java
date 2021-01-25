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
@Document(collection = "expenseEmailAddress")
@Builder
public class ExpenseEmailAddress {
    @Id
    private String _id;
    private String email;
    private String action;

    @Override
    public boolean equals(Object o) {
        if (o instanceof ExpenseEmailAddress) {
            ExpenseEmailAddress email = (ExpenseEmailAddress) o;
            return Objects.equals(_id, email._id);
        }
        return false;
    }
}
