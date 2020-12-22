package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.Address;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FinancialCompanyAddrequest {
    // @NotBlank(message = IVA_CODE_NOT_BLANK)
    // @NotBlank(message = IVA_NAME_NOT_BLANK)
    private String financialCompanyId;
    private String name;
    private String code;
    private String email;
    private String phone1;
    private String phone2;
    private String logo;
    private Address address;
}
