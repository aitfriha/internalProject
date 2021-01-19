package com.wproducts.administration.service;

import com.wproducts.administration.controller.request.AbilityAddRequest;
import com.wproducts.administration.controller.request.AbilityUpdateRequest;
import com.wproducts.administration.controller.request.RoleAddAbilitiesRequest;
import com.wproducts.administration.dto.model.AbilityDto;
import com.wproducts.administration.model.Ability;

import java.util.List;

public interface AbilityService {

    /**
     * Register a new Ability
     *
     * @param abilityAddRequest - abilityAddRequest
     */
    void save(AbilityAddRequest abilityAddRequest);

    /**
     * Register a new Ability & return it
     *
     * @param abilityAddRequest abilityAddRequest
     * @return Ability
     */
    Ability saveAndReturnAbility(AbilityAddRequest abilityAddRequest);

    /**
     * Update Ability
     *
     * @param abilityUpdateRequest - abilityUpdateRequest
     */
    void updateAbility(AbilityUpdateRequest abilityUpdateRequest);

    /**
     * delete Ability
     *
     * @param id - id
     */
    void removeAbility(String id);

    /**
     * all AbilitiesDto
     *
     * @return List AbilitiesDto
     */
    List<AbilityDto> getAllAbilities();

    /**
     * one AbilityDto
     *
     * @param id - id
     * @return AbilityDto
     */
    AbilityDto getOneAbility(String id);
}
