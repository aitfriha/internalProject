package com.wproducts.administration.dto.mapper;

import com.wproducts.administration.controller.request.RoleAddRequest;
import com.wproducts.administration.controller.request.RoleUpdateRequest;
import com.wproducts.administration.dto.model.RoleDto;
import com.wproducts.administration.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    /**
     * Map dto to model
     *
     * @param roleDto roleDto
     * @return Role
     */
    @Mapping(source = "roleId", target="_id")
    Role dtoToModel(RoleDto roleDto);
    /**
     * Map model to dto
     *
     * @param role role
     * @return RoleDto
     */
    @Mapping(source = "_id", target="roleId")
    @Mapping(target = "roleCreatedAt", ignore=true)
    @Mapping(target = "roleUpdatedAt", ignore=true)
    RoleDto modelToDto(Role role);

    /**
     * Map add request to dto
     *
     * @param roleAddRequest roleAddRequest
     * @return RoleDto
     */
    @Mapping(target = "roleUpdatedAt", ignore=true)
    RoleDto addRequestToDto(RoleAddRequest roleAddRequest);

    /**
     * Map update request to dto
     *
     * @param roleUpdateRequest roleUpdateRequest
     * @return RoleDto
     */

    @Mapping(target = "roleCreatedAt", ignore=true)
    RoleDto updateRequestToDto(RoleUpdateRequest roleUpdateRequest);

}
