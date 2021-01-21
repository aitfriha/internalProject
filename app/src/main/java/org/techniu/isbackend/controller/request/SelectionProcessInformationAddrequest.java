package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.techniu.isbackend.entity.SelectionTypeEvaluation;

import javax.validation.constraints.NotBlank;

import java.util.List;

import static org.techniu.isbackend.exception.ValidationConstants.ABSENCETYPE_CODE_NOT_BLANK;
import static org.techniu.isbackend.exception.ValidationConstants.ABSENCETYPE_NAME_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SelectionProcessInformationAddrequest {

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
    private List<String> knowledgeIdList;
    private List<String> experiences;
    // Currency
    private String currencyId;

}
