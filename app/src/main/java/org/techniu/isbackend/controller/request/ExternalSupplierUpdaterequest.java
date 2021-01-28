package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.techniu.isbackend.entity.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExternalSupplierUpdaterequest {
    // @NotBlank(message = IVA_CODE_NOT_BLANK)
    // @NotBlank(message = IVA_NAME_NOT_BLANK)
    private String externalSupplierId;
    private String companyName;
    private String code;
    private String taxNumber;
    private String email;
    private String URL;
    private String firstName;
    private String fatherFamilyName;
    private String motherFamilyName;
    private Address address;
}
