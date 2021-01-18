package com.wproducts.administration.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentUpdateRequest {
    private String departmentId;
    @NotBlank(message = DEPARTMENT_CODE_NOT_BLANK)
    private String departmentCode;
    private String departmentName;
    private String departmentDescription;
}
