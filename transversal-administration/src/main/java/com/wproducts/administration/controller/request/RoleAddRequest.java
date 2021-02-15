package com.wproducts.administration.controller.request;

import com.wproducts.administration.dto.model.AbilityDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

import static org.techniu.isbackend.exception.ValidationConstants.ROLE_NAME_NOT_BLANK;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RoleAddRequest {
    @NotBlank(message = ROLE_NAME_NOT_BLANK)
    private String roleName;
    private String roleDescription;
    private Set<String> roleActionsIds;
}
