package org.techniu.isbackend.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data()
public class Person {

    @DBRef
    private PersonType personType;

    private String companyName;

    private String name;

    private String fatherFamilyName;

    private String motherFamilyName;

}
