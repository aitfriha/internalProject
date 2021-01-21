package com.wproducts.administration.service;

import com.wproducts.administration.controller.request.AbilityAddRequest;
import com.wproducts.administration.controller.request.RoleAddAbilitiesRequest;
import com.wproducts.administration.dto.mapper.*;
import com.wproducts.administration.dto.model.AbilityDto;
import com.wproducts.administration.dto.model.RoleDto;
import com.wproducts.administration.model.Ability;
import com.wproducts.administration.model.Role;
import com.wproducts.administration.repository.RoleRepository;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;


@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AbilityService abilityService;
    private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
    private final AbilityMapper abilityMapper = Mappers.getMapper(AbilityMapper.class);
    private final ActionMapper actionMapper = Mappers.getMapper(ActionMapper.class);
    private final SubjectMapper subjectMapper = Mappers.getMapper(SubjectMapper.class);
  

    public RoleServiceImpl(RoleRepository roleRepository, AbilityService abilityService) {
        this.roleRepository = roleRepository;
        this.abilityService = abilityService;
    }


    /**
     * Register a new Role
     *
     * @param roleDto - roleDto
     */
    @Override
    public void save(RoleDto roleDto) {

        roleDto.setRoleName(roleDto.getRoleName().toLowerCase());

        Optional<Role> role = Optional.ofNullable(roleRepository.findByRoleName(roleDto.getRoleName()));
        if (roleDto.getRoleName().contains(" ")) {
            throw exception(Name_SHOULD_NOT_CONTAIN_SPACES);
        }
        if (role.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        ArrayList<Ability> roleAbilities = new ArrayList<>();
        for (AbilityDto abilityDto : roleDto.getRoleAbilities()) {
            roleAbilities.add(abilityMapper.dtoToModel(abilityDto));
        }
        Role role1 = roleMapper.dtoToModel(roleDto)
                .setRoleDescription(roleDto.getRoleDescription())
                .setRoleCreatedAt(Instant.now())
                .setRoleAbilities(roleAbilities);
        roleRepository.save(role1);
    }


    /**
     * Add abilities to certain role
     *
     * @param roleAddAbilitiesRequest - roleAddAbilitiesRequest
     */
    @Override
    public void addAbilities(RoleAddAbilitiesRequest roleAddAbilitiesRequest) {

        Role role = new Role().set_id(roleAddAbilitiesRequest.getRoleId());

        List<Ability> abilities = new ArrayList<>();
        for (AbilityAddRequest abilityAddRequest :
                roleAddAbilitiesRequest.getRoleAbilities()) {
            abilities.add(abilityService.saveAndReturnAbility(abilityAddRequest));
        }

        role.setRoleAbilities(abilities);
        role.setRoleUpdatedAt(Instant.now());

        roleRepository.save(role);

    }

    @Override
    public void updateRole(RoleDto roleDto) {

        roleDto.setRoleName(roleDto.getRoleName().toLowerCase());
        if (roleDto.getRoleName().contains(" ")) {
            throw exception(Name_SHOULD_NOT_CONTAIN_SPACES);
        }
        Optional<Role> role = Optional.ofNullable(roleRepository.findBy_id(roleDto.getRoleId()));
        if (!role.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }

        Optional<Role> role1 = Optional.ofNullable(roleRepository.findByRoleName(roleDto.getRoleName()));
        if (role1.isPresent() && !(role.get().getRoleName().equals(roleDto.getRoleName()))) {
            throw exception(DUPLICATE_ENTITY);
        }
        ArrayList<Ability> roleAbilities = new ArrayList<>();
        for (AbilityDto abilityDto : roleDto.getRoleAbilities()) {
            roleAbilities.add(abilityMapper.dtoToModel(abilityDto));
        }
        Role role2 = roleMapper.dtoToModel(roleDto)
                .setRoleUpdatedAt(Instant.now())
                .setRoleDescription(roleDto.getRoleDescription())
                .setRoleAbilities(roleAbilities)
                .setRoleCreatedAt(role.get().getRoleCreatedAt());
        // save role
        roleRepository.save(role2);
    }

    /**
     * delete Role
     *
     * @param id - id
     */
    @Override
    public void removeRole(String id) {
        Optional<Role> role = Optional.ofNullable(roleRepository.findBy_id(id));
        // If role doesn't exists
        if (!role.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        roleRepository.deleteById(id);
    }

    /**
     * all RolesDto
     *
     * @return List RolesDto
     */
    @Override
    public List<RoleDto> getAllRoles() {
        // Get all roles
        List<Role> roles = roleRepository.findAll();
        // Create a list of all roles dto
        ArrayList<RoleDto> roleDtos = new ArrayList<>();
        for (Role role : roles) {
            ArrayList<AbilityDto> abilityDtos = new ArrayList<>();
            RoleDto roleDto = roleMapper.modelToDto(role).setRoleName(role.getRoleName().toUpperCase());
            if (role.getRoleAbilities() != null) {
                for (Ability ability : role.getRoleAbilities()) {
                    AbilityDto abilityDto = abilityMapper.modelToDto(ability);
                    abilityDto.setAbilityCreatedAt(ability.getAbilityCreatedAt().toString());
                    if (ability.getAbilityUpdatedAt() != null) {
                        abilityDto.setAbilityUpdatedAt(ability.getAbilityUpdatedAt().toString());
                    }
                    if (ability.getAbilityAction() != null) {
                        abilityDto.setAbilityAction(actionMapper.modelToDto(ability.getAbilityAction()));
                    }
                    if (ability.getAbilitySubject() != null) {
                        abilityDto.setAbilitySubject(subjectMapper.modelToDto(ability.getAbilitySubject()));
                    }
                    abilityDtos.add(abilityDto);
                }
            }
            roleDto.setRoleAbilities(abilityDtos);
            if (role.getRoleUpdatedAt() != null) {
                roleDto.setRoleUpdatedAt(role.getRoleUpdatedAt().toString());
            }
            if (role.getRoleCreatedAt() != null) {
                roleDto.setRoleCreatedAt(role.getRoleCreatedAt().toString());
            }
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }

    /**
     * one RoleDto
     *
     * @param id - id
     * @return RoleDto
     */
    @Override
    public RoleDto getOneRole(String id) {
        Optional<Role> role = Optional.ofNullable(roleRepository.findBy_id(id));

        // If role doesn't exists
        if (!role.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        RoleDto roleDto = roleMapper.modelToDto(role.get());
        ArrayList<AbilityDto> abilityDtos = new ArrayList<>();
        if (role.get().getRoleAbilities() != null) {
            for (Ability ability : role.get().getRoleAbilities()) {
                abilityDtos.add(abilityMapper.modelToDto(ability));
            }
        }
        if (role.get().getRoleUpdatedAt() != null) {
            roleDto.setRoleUpdatedAt(role.get().getRoleUpdatedAt().toString());
        }
        if (role.get().getRoleCreatedAt() != null) {
            roleDto.setRoleCreatedAt(role.get().getRoleCreatedAt().toString());
        }
        if (role.get().getRoleAbilities() != null) {
            for (Ability ability : role.get().getRoleAbilities()) {
                AbilityDto abilityDto = abilityMapper.modelToDto(ability);
                abilityDto.setAbilityCreatedAt(ability.getAbilityCreatedAt().toString());
                if (ability.getAbilityUpdatedAt() != null) {
                    abilityDto.setAbilityUpdatedAt(ability.getAbilityUpdatedAt().toString());
                }
                if (ability.getAbilityAction() != null) {
                    abilityDto.setAbilityAction(actionMapper.modelToDto(ability.getAbilityAction()));
                }
                if (ability.getAbilitySubject() != null) {
                    abilityDto.setAbilitySubject(subjectMapper.modelToDto(ability.getAbilitySubject()));
                }

                abilityDtos.add(abilityDto);
            }
        }
        roleDto.setRoleAbilities(abilityDtos);
        return roleDto;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args - args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Role, exceptionType, args);
    }
}
