package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(value = "StaffContractHistory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffContractHistory implements Serializable {
    @Id
    private String _id;
    private StaffContract staffContractHistory;
    private String updatedAt;

    @DBRef
    private StaffContract staffContract;


}
