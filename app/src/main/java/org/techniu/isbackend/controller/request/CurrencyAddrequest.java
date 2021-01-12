package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.techniu.isbackend.entity.TypeOfCurrency;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CurrencyAddrequest {
    // @NotBlank(message = CONTRACTSTATUS_CODE_NOT_BLANK)

    private String currencyId;
    private int year;
    private int month;
    private String changeFactor;
    private TypeOfCurrency typeOfCurrency;
}
