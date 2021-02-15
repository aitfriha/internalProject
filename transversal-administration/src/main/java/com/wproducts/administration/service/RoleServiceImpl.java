package com.wproducts.administration.service;

import com.wproducts.administration.controller.request.AbilityAddRequest;
import com.wproducts.administration.controller.request.RoleAddAbilitiesRequest;
import com.wproducts.administration.dto.mapper.*;
import com.wproducts.administration.dto.model.ActionDto;
import com.wproducts.administration.dto.model.RoleDto;
import com.wproducts.administration.model.Role;
import com.wproducts.administration.model.Action;
import com.wproducts.administration.repository.ActionRepository;
import com.wproducts.administration.repository.RoleRepository;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static org.techniu.isbackend.exception.ExceptionType.*;


@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AbilityService abilityService;
    private final ActionRepository actionRepository;
    private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
    private final AbilityMapper abilityMapper = Mappers.getMapper(AbilityMapper.class);
    private final ActionMapper actionMapper = Mappers.getMapper(ActionMapper.class);
    private final SubjectMapper subjectMapper = Mappers.getMapper(SubjectMapper.class);
  

    public RoleServiceImpl(RoleRepository roleRepository, AbilityService abilityService, ActionRepository actionRepository) {
        this.roleRepository = roleRepository;
        this.abilityService = abilityService;
        this.actionRepository = actionRepository;
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
        ArrayList<Action> roleActions = new ArrayList<>();
        System.out.println(roleDto);
        for (String stractionDto : roleDto.getRoleActionsIds()) {
            List<Action> action= actionRepository.findByActionConcerns(stractionDto);
            roleActions.add(action.get(0));
        }
        Role role1 = roleMapper.dtoToModel(roleDto)
                .setRoleDescription(roleDto.getRoleDescription())
                .setRoleCreatedAt(Instant.now())
                .setRoleActions(roleActions);
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

        List<Action> actions = new ArrayList<>();
        for (AbilityAddRequest abilityAddRequest :
                roleAddAbilitiesRequest.getRoleAbilities()) {
          ///  actions.add(abilityService.saveAndReturnAbility(abilityAddRequest));
        }

        role.setRoleActions(actions);
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
        ArrayList<Action> roleActions = new ArrayList<>();
        for (ActionDto actionDto : roleDto.getRoleActions()) {
            roleActions.add(actionMapper.dtoToModel(actionDto));
        }
        Role role2 = roleMapper.dtoToModel(roleDto)
                .setRoleUpdatedAt(Instant.now())
                .setRoleDescription(roleDto.getRoleDescription())
                .setRoleActions(roleActions)
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
            ArrayList<ActionDto> actionDtos = new ArrayList<>();
            Set<String> actionsIds = new HashSet<>();
            RoleDto roleDto = roleMapper.modelToDto(role).setRoleName(role.getRoleName().toUpperCase());
            if (role.getRoleActions() != null) {
                for (Action action : role.getRoleActions()) {
                    ActionDto actionDto = actionMapper.modelToDto(action);
                    actionDto.setActionCreatedAt(action.getActionCreatedAt().toString());
                    if (action.getActionUpdatedAt() != null) {
                        actionDto.setActionUpdatedAt(action.getActionUpdatedAt().toString());
                    }
                    actionDtos.add(actionDto);
                    actionsIds.add(action.getActionConcerns());
                }
            }
            roleDto.setRoleActions(actionDtos);
            if (role.getRoleUpdatedAt() != null) {
                roleDto.setRoleUpdatedAt(role.getRoleUpdatedAt().toString());
            }
            if (role.getRoleCreatedAt() != null) {
                roleDto.setRoleCreatedAt(role.getRoleCreatedAt().toString());
            }
           // ActionDto actionDto =new ActionDto();
           // actionDto.setActionId("5f10ce983d7fb0748720c5da");
           // actionDto.setActionConcerns("Access");
           // actionDtos.add(actionDto);
            roleDto.setRoleActions(actionDtos);
           // actionsIds.add("Access");
            roleDto.setRoleActionsIds(actionsIds);
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
        ArrayList<ActionDto> actionDtos = new ArrayList<>();
        if (role.get().getRoleActions() != null) {
            for (Action action : role.get().getRoleActions()) {
                actionDtos.add(actionMapper.modelToDto(action));
            }
        }
        if (role.get().getRoleUpdatedAt() != null) {
            roleDto.setRoleUpdatedAt(role.get().getRoleUpdatedAt().toString());
        }
        if (role.get().getRoleCreatedAt() != null) {
            roleDto.setRoleCreatedAt(role.get().getRoleCreatedAt().toString());
        }
        if (role.get().getRoleActions() != null) {
            for (Action action : role.get().getRoleActions()) {
                ActionDto actionDto = actionMapper.modelToDto(action);
                actionDto.setActionCreatedAt(action.getActionCreatedAt().toString());
                if (action.getActionUpdatedAt() != null) {
                    actionDto.setActionUpdatedAt(action.getActionUpdatedAt().toString());
                }
                actionDtos.add(actionDto);
            }
        }
        roleDto.setRoleActions(actionDtos);
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
