package com.wproducts.administration.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RoleAddAbilitiesRequest {
    private String roleId;
    private List<AbilityAddRequest> roleAbilities;
}
