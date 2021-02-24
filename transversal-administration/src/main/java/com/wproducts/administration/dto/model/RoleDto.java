package com.wproducts.administration.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wproducts.administration.model.Ability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto {

    private String roleId;
    private String roleName;
    private String roleDescription;
    /*private Collection<ActionDto> roleActions;*/
    private String roleCreatedAt;
    private String roleUpdatedAt;
    /*private Set<String> roleActionsIds;*/
    Map<String,Boolean> actionsNames = new HashMap<String,Boolean>();
}
