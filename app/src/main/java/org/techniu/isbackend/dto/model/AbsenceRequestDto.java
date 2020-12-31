package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.AbsenceType;
import org.techniu.isbackend.entity.Staff;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsenceRequestDto {

    private String absenceRequestId;
    private String startDate;
    private String endDate;
    private String absenceDays;
    private String hourRate;
    private String staffId;
    private String staffName;
    private String absenceTypeId;
    private String absenceTypeName;
    private String state;
    private List<String> docExtensionList;
    private  List<byte[]> documentList;

}
