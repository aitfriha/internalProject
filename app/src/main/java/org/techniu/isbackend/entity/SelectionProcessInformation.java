package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(value = "SelectionProcessInformation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectionProcessInformation implements Serializable {

    @Id
    private String _id;
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

    private List<String> experiences;

    @DBRef
    private List<SelectionTypeEvaluation> knowledge;

    @DBRef
    private Currency currency;



}
