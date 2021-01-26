package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.BUSINESS_EXPENSE_TYPE_CODE_NOT_BLANK;
import static org.techniu.isbackend.exception.ValidationConstants.BUSINESS_EXPENSE_TYPE_NAME_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessExpensesTypeAddRequest {

    private String id;
    @NotBlank(message = BUSINESS_EXPENSE_TYPE_CODE_NOT_BLANK)
    private String code;
    @NotBlank(message = BUSINESS_EXPENSE_TYPE_NAME_NOT_BLANK)
    private String name;
    private boolean allowSubtypes;
    private String masterValue = "";
    private boolean removable = true;
}
