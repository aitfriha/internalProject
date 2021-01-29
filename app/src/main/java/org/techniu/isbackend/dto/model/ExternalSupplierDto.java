package org.techniu.isbackend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.techniu.isbackend.entity.Address;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalSupplierDto {

    @NotNull
    private String externalSupplierId;
    @NotNull
    private String companyName;
    @NotNull
    private String code;
    @NotNull
    private String taxNumber;
    private String email;
    private String url;
    @NotNull
    private String firstName;
    @NotNull
    private String fatherFamilyName;
    @NotNull
    private String motherFamilyName;

    @DBRef
    private Address address;

}
