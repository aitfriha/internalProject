package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain = true)
@Document(collection = "assignmentType")
@Builder
public class AssignmentType implements Serializable {

    @Id
    private String _id;
    private String code;
    private String name;
    private String description;
}
