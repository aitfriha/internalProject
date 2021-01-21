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
public class FinancialCompanyDto {

    @NotNull
    private String financialCompanyId;
    @NotNull
    private String name;
    private String code;
    private String taxNumber;
    private String email;
    @NotNull
    private String phone1;
    private String phone2;
    private String logo;

    @DBRef
    private Address address;

}
