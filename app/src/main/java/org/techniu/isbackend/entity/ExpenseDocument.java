package org.techniu.isbackend.entity;

import lombok.Data;
import org.bson.types.Binary;

import java.util.Date;

@Data()
public class ExpenseDocument {

    private String name;

    private String type;

    private Date uploadDate;

    private Binary data;

}
