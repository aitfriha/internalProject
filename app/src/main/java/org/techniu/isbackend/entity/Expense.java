package org.techniu.isbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain = true)
@Document(collection = "expense")
@Builder
public class Expense {

    //------------------------------------------- COMMONS DATA -------------------------------------------------------
    private String _id;

    @DBRef
    private StaffExpenseType expenseType;

    private String expenseSubtypeId;

    private Date expenseDate;

    private Date registerDate;

    private Date paymentDate;

    @DBRef
    private Staff staff;

    @DBRef
    private TypeOfCurrency localCurrencyType;

    private BigDecimal localCurrencyAmount;

    @DBRef
    private TypeOfCurrency euroCurrencyType;

    private BigDecimal euroAmount;

    @DBRef
    private VoucherType voucherType;

    private ExpenseDocument document;

    @DBRef
    private ExpenseStatus status;

    //-------------------------------------------- SUPPORT, LODGING AND OTHERS --------------------------------------

    @DBRef
    private Country expenseCountry;

    @DBRef
    private StateCountry expenseState;

    @DBRef
    private City expenseCity;

    //-------------------------------------------- LODGING ----------------------------------------------------------

    private Date arrivalDate;

    private Date departureDate;

    //-------------------------------------------- OTHERS ------------------------------------------------------------

    private String description;

    //-------------------------------------------- TRANSPORT AND KMS -------------------------------------------------

    @DBRef
    private Country fromCountry;

    @DBRef
    private StateCountry fromState;

    @DBRef
    private City fromCity;

    @DBRef
    private Country toCountry;

    @DBRef
    private StateCountry toState;

    @DBRef
    private City toCity;

    //-------------------------------------------- KMS --------------------------------------------------------------

    private Double kms;


    //------------------------------------------ SUPPORT, TRANSPORT AND KMS ------------------------------------------------------------

    private List<Person> persons;

}
