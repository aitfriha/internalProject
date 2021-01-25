package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ExpenseDto {

    private String id;

    // Staff expense type data
    private String expenseTypeId;
    private boolean expenseTypeAllowSubtypes;
    private String expenseTypeMasterValue;

    // Staff expense subtype data
    private String expenseSubtypeId;

    private Date expenseDate;

    private Date registerDate;

    private Date paymentDate;

    //Staff data
    private String staffId;
    private String staffPersonalNumber;
    private String staffName;
    private String staffFatherFamilyName;
    private String staffMotherFamilyName;
    private String staffCompany;
    private String staffCompanyEmail;

    //Voucher type data
    private String voucherTypeId;
    private String voucherTypeName;
    private String voucherTypeMasterValue;

    //Local Currency Data
    private String localCurrencyTypeId;
    private String localCurrencyTypeName;

    private BigDecimal localCurrencyAmount;

    //Euro Currency Data
    private String euroCurrencyTypeId;
    private String euroCurrencyTypeName;

    private BigDecimal euroAmount;

    private ExpenseDocumentDto document;

    //ExpenseStatus data
    private String expenseStatusId;
    private String expenseStatusName;
    private String expenseStatusMasterValue;

    //-------------------------------------------- SUPPORT, LODGING AND OTHERS --------------------------------------

    //Expense Country data
    private String expenseCountryId;
    private String expenseCountryName;

    //Expense State data
    private String expenseStateId;
    private String expenseStateName;

    //Expense City data
    private String expenseCityId;
    private String expenseCityName;

    //-------------------------------------------- LODGING ----------------------------------------------------------

    private Date arrivalDate;

    private Date departureDate;

    //-------------------------------------------- OTHERS ------------------------------------------------------------

    private String description;

    //-------------------------------------------- TRANSPORT AND KMS -------------------------------------------------

    //From Country data
    private String fromCountryId;
    private String fromCountryName;

    //From State data
    private String fromStateId;
    private String fromStateName;

    //From City data
    private String fromCityId;
    private String fromCityName;

    //To Country data
    private String toCountryId;
    private String toCountryName;

    //To State data
    private String toStateId;
    private String toStateName;

    //To City data
    private String toCityId;
    private String toCityName;

    //-------------------------------------------- KMS --------------------------------------------------------------

    private Double kms;

    //------------------------------------------ SUPPORT, TRANSPORT AND KMS ------------------------------------------------------------

    private List<PersonDto> persons;

}
