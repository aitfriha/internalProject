package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.techniu.isbackend.entity.AdministrativeStructureLevel;
import org.techniu.isbackend.entity.FunctionalStructureLevel;
import org.techniu.isbackend.entity.StaffDocument;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StaffDto{
    private String staffId;
    // *** general information *** //
    //
    private String photo;
    // personal info
    private String firstName;
    private String fatherFamilyName;
    private String motherFamilyName;
    private String birthday;
    private String birthCountry;
    // contact info
    private String personalPhone;
    private String personalEmail;
    private String companyPhone;
    private String companyMobilePhone;
    private String companyEmail;
    private String skype;
    private String emergencyContactName;
    private String emergencyContactPhone;
    // Address
    private String addressId;
    private String cityId;
    private String fullAddress;
    private String cityName;
    private String stateId;
    private String stateName;
    private String countryId;
    private String countryName;
    private String postCode;
    // documentation
    private List<StaffDocument> staffDocuments;
    // Contract
    private String staffContractId;
    private String companyId;
    private String companyName;
    private String associateOffice;
    private String hiringCountry;
    private String townContract;
    private String personalNumber;
    private String highDate;
    private String lowDate;
    private String registrationDate;
    private String preContractDate;
    private String contractTypeId;
    private String contractTypeName;
    private String contractTypeStateId;
    private String contractTypeState;
    private String contractTypeCountryId;
    private String contractTypeCountry;
    private String legalCategoryTypeId;
    private String legalCategoryTypeName;
    private String contractModelId;
    private String contractModelName;
    private  byte[] internalRulesDoc;
    private  byte[] contractDoc;
    private  byte[] preContractDoc;
    // economic contract
    private String staffEconomicContractInformationId;
    private String contractSalary;
    private String companyContractCost;
    private String expenses;
    private String companyExpensesCost;
    private String objectives;
    private String companyObjectivesCost;
    private String totalCompanyCost;
    private String contractSalaryDateGoing;
    private String contractSalaryDateOut;
    private String companyContractCostDateGoing;
    private String companyContractCostDateOut;
    private String expensesDateGoing;
    private String expensesDateOut;
    private String companyExpensesCostDateGoing;
    private String companyExpensesCostDateOut;
    private String objectivesDateGoing;
    private String objectivesDateOut;
    private String companyObjectivesCostDateGoing;
    private String companyObjectivesCostDateOut;
    private String totalCompanyCostDateGoing;
    private String totalCompanyCostDateOut;
    private String currencyId;
    private String currencyCode;
    private String currencyName;
    private int currencyYear;
    private int currencyMonth;
    private String changeFactor;

    // Functional Structure Level
    private List<FunctionalStructureLevel> functionalStructureLevels;
    private String isFunctionalLeader;

    //Administrative Structure Level
    private List<AdministrativeStructureLevel> administrativeStructureLevels;
    private String isAdministrativeLeader;

    //Created at
    private String createdAt;

}
