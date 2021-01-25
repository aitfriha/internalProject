package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.BUSINESS_EXPENSE_SUBTYPE_CODE_NOT_BLANK;
import static org.techniu.isbackend.exception.ValidationConstants.BUSINESS_EXPENSE_SUBTYPE_NAME_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessExpensesSubtypeAddRequest {

    private String type;
    private String masterValueType;
    private String id = new ObjectId().toString();
    @NotBlank(message = BUSINESS_EXPENSE_SUBTYPE_CODE_NOT_BLANK)
    private String code;
    @NotBlank(message = BUSINESS_EXPENSE_SUBTYPE_NAME_NOT_BLANK)
    private String name;
    private boolean requiresApproval;
}
