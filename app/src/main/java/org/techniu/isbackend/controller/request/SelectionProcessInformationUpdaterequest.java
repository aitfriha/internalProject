package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SelectionProcessInformationUpdaterequest {

    private String selectionProcessId;
    private String firstName;
    private String fatherFamilyName;
    private String motherFamilyName;
    private String testDate;
    private String energy;
    private String adaptability;
    private String integrity;
    private String interpersonalSensitivity;
    private String docExtension;
    private String economicProposal;
    private String economicClaimsValue;
    private String economicClaimsRange1;
    private String economicClaimsRange2;
    private String economicClaimsType;
    private String proposalType;
    private String salaryType;
    private List<String> knowledgeIdList;
    private List<String> experiences;
    // Currency
    private String currencyId;

}
