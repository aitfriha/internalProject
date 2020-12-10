package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectorCompanyDto {
    // sector 1
    private String firstSectorId;
    private String firstSectorName;
    private String firstSectorDescription;
    // sector 2
    private String secondSectorId;
    private String secondSectorName;
    private String secondSectorDescription;
    // sector 3
    private String thirdSectorId;
    private String thirdSectorName;
    private String thirdSectorDescription;

    public SectorCompanyDto(String firstSectorId, String firstSectorName, String firstSectorDescription,
                            String secondSectorId, String secondSectorName, String secondSectorDescription) {
        this.firstSectorId=firstSectorId;
                this.firstSectorName=firstSectorName;
                this.firstSectorDescription=firstSectorDescription;
                this.secondSectorId=secondSectorId;
                this.secondSectorName=secondSectorName;
                this.secondSectorDescription=secondSectorDescription;
    }
}
