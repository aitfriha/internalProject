package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
@Document(value = "Log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log implements Serializable {
    @Id
    private String logId;
    private String actionDate;
    private String userName;
    private LogType logType;
    private ClassType classType;

}
