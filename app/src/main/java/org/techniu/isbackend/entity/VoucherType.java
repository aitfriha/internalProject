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
@Document(collection = "voucherType")
@Builder
public class VoucherType {
    @Id
    private String _id;
    private String code;
    private String name;
    private String description;
    private boolean removable;
    private String masterValue;

    @Override
    public boolean equals(Object o) {
        if (o instanceof VoucherType) {
            VoucherType voucherType = (VoucherType) o;
            return Objects.equals(_id, voucherType._id);
        }
        return false;
    }
}
