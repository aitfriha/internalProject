package com.wproducts.administration.dto.mapper;

import com.wproducts.administration.controller.request.AbilityAddRequest;
import com.wproducts.administration.controller.request.AbilityUpdateRequest;
import com.wproducts.administration.dto.model.AbilityDto;
import com.wproducts.administration.model.Ability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AbilityMapper {

    /**
     * Map dto to model
     *
     * @param abilityDto abilityDto
     * @return Ability
     */
    @Mapping(source = "abilityId", target="_id")
    Ability dtoToModel(AbilityDto abilityDto);

    /**
     * Map model to dto
     *
     * @param ability ability
     * @return abilityDto
     */
    @Mapping(source = "_id", target="abilityId")
    @Mapping(target = "abilityCreatedAt", ignore=true)
    @Mapping(target = "abilityUpdatedAt", ignore=true)
    @Mapping(target = "abilityAction", ignore=true)
    @Mapping(target = "abilityField", ignore=true)
    @Mapping(target = "abilitySubject", ignore=true)
    AbilityDto modelToDto(Ability ability);

    /**
     * Map add request to dto
     *
     * @param abilityAddRequest abilityAddRequest
     * @return abilityDto
     */
    @Mapping(target = "abilityAction", ignore=true)
    @Mapping(target = "abilityField", ignore=true)
    @Mapping(target = "abilitySubject", ignore=true)
    AbilityDto addRequestToDto(AbilityAddRequest abilityAddRequest);

    /**
     * Map update request to dto
     *
     * @param AbilityUpdateRequest AbilityUpdateRequest
     * @return abilityDto
     */
    @Mapping(target = "abilityAction", ignore=true)
    @Mapping(target = "abilityField", ignore=true)
    @Mapping(target = "abilitySubject", ignore=true)
    AbilityDto updateRequestToDto(AbilityUpdateRequest AbilityUpdateRequest);
}
