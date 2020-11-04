package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(value = "LegalCategoryType")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalCategoryType implements Serializable {

    @Id
    private String legalCategoryTypeId;
    private String name;
    private String functions;
    private String companyName;

    @DBRef
    private Company company;

}