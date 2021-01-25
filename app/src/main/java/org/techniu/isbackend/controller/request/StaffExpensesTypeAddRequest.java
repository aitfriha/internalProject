package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.STAFF_EXPENSE_TYPE_CODE_NOT_BLANK;
import static org.techniu.isbackend.exception.ValidationConstants.STAFF_EXPENSE_TYPE_NAME_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StaffExpensesTypeAddRequest {

    private String id;
    @NotBlank(message = STAFF_EXPENSE_TYPE_CODE_NOT_BLANK)
    private String code;
    @NotBlank(message = STAFF_EXPENSE_TYPE_NAME_NOT_BLANK)
    private String name;
    private boolean allowSubtypes;
    private String masterValue = "";
    private boolean removable = true;
}
