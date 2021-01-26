package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.Currency;
import org.techniu.isbackend.entity.SelectionTypeEvaluation;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectionProcessInformationDto {

    private String selectionProcessId;
    private String firstName;
    private String fatherFamilyName;
    private String motherFamilyName;
    private String profile;
    private String testDate;
    private String energy;
    private String adaptability;
    private String integrity;
    private String interpersonalSensitivity;
    private String docExtension;
    private String economicCandidateProposal;
    private String economicClaimsValue;
    private String economicClaimsRange1;
    private String economicClaimsRange2;
    private String economicClaimsType;
    private String candidateProposalType;
    private String candidateSalaryType;
    private String economicCompanyProposal;
    private String objectives;
    private String companyProposalType;
    private String companySalaryType;
    private byte[] economicProposalDoc;
    private byte[] curriculumDoc;
    private byte[] attitudeTestDoc;
    private List<SelectionTypeEvaluation> knowledge;
    private List<String> experiences;

    // Currency
    private String currencyId;
    private String currencyCode;
    private String currencyName;
    private int currencyYear;
    private int currencyMonth;
    private String changeFactor;

}
