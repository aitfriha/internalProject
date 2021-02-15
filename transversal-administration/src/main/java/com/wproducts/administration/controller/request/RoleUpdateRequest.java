package com.wproducts.administration.controller.request;

import com.wproducts.administration.dto.model.AbilityDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RoleUpdateRequest {
    private String roleId;
    private String roleName;
    private String roleDescription;
    //private List<String> roleActions;
    private Set<String> roleActionsIds;
}
