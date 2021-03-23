package com.wproducts.administration.service;


import com.wproducts.administration.controller.request.RoleAddAbilitiesRequest;
import com.wproducts.administration.dto.model.RoleDto;

import java.util.List;

public interface RoleService {
    /**
     * Register a new Role
     *
     * @param roleDto - roleDto
     */
    void save(RoleDto roleDto);

    /**
     * Add abilities to a certain role
     *
     * @param roleAddAbilitiesRequest roleAddAbilitiesRequest
     */
    void addAbilities(RoleAddAbilitiesRequest roleAddAbilitiesRequest);
    /**
     * Update Role
     *
     * @param roleDto - roleDto
     */
    void updateRole(RoleDto roleDto,String oldRoleName);

    /**
     * delete Role
     *
     * @param id - id
     */
    void removeRole(String id);

    /**
     * all RolesDto
     *
     * @return List RolesDto
     */
    List<RoleDto> getAllRoles();

    /**
     * one RoleDto
     *
     * @param id - id
     * @return RoleDto
     */
    RoleDto getOneRole(String id);
}
