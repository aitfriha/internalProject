package com.wproducts.administration.controller.api;

import com.wproducts.administration.controller.request.RoleAddAbilitiesRequest;
import com.wproducts.administration.controller.request.RoleAddRequest;
import com.wproducts.administration.controller.request.RoleUpdateRequest;
import com.wproducts.administration.dto.mapper.AbilityMapper;
import com.wproducts.administration.dto.mapper.RoleMapper;
import com.wproducts.administration.dto.model.AbilityDto;
import com.wproducts.administration.repository.AbilityRepository;
import com.wproducts.administration.service.RoleService;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.Response;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.Ability;
import static org.techniu.isbackend.exception.EntityType.Role;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = { "http://localhost:3001" })
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
    private final AbilityMapper abilityMapper = Mappers.getMapper(AbilityMapper.class);
    private final MapValidationErrorService mapValidationErrorService;
    private final AbilityRepository abilityRepository;

    public RoleController(RoleService roleService, MapValidationErrorService mapValidationErrorService, AbilityRepository abilityRepository) {
        this.roleService = roleService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.abilityRepository = abilityRepository;
    }

    /**
     * Handles the incoming POST API "/role/add"
     *
     * @param roleAddRequest Role Add request
     * @return RoleDto
     */
    @PostMapping("/add")
    public ResponseEntity signup(@RequestBody @Valid RoleAddRequest roleAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
//        List<String> roleAbilitiesIds = roleAddRequest.getRoleAbilities();
//        ArrayList<AbilityDto> roleAbilities = new ArrayList<>();
//        if(roleAbilitiesIds != null) {
//            for (String roleAbility : roleAbilitiesIds) {
//                roleAbilities.add(abilityMapper.modelToDto(abilityRepository.findAbilityBy_id(roleAbility)));
//            }
//        }
        roleService.save(roleMapper.addRequestToDto(roleAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Role, ADDED)), HttpStatus.OK);
    }

    /**
     * Add abilities to certain role
     *
     * @param roleAddAbilitiesRequest role Add Abilities Request
     * @return RoleDto
     */
    @PostMapping("/addAbilities")
    public ResponseEntity addAbilities(@RequestBody @Valid RoleAddAbilitiesRequest roleAddAbilitiesRequest , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);

        roleService.addAbilities(roleAddAbilitiesRequest);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Ability, ADDED)), HttpStatus.OK);

    }

    /**
     * Handles the incoming POST API "/role/update"
     *
     * @param roleUpdateRequest Role update
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid RoleUpdateRequest roleUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        List<String> roleAbilitiesIds = roleUpdateRequest.getRoleAbilities();
        ArrayList<AbilityDto> roleAbilities = new ArrayList<>();
        if (roleAbilitiesIds != null) {
            for (String roleAbility : roleAbilitiesIds) {
                roleAbilities.add(abilityMapper.modelToDto(abilityRepository.findAbilityBy_id(roleAbility)));
            }
        }
        roleService.updateRole(roleMapper.updateRequestToDto(roleUpdateRequest).setRoleAbilities(roleAbilities));
        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(Role, UPDATED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming DELETE API "/role/delete"
     *
     * @param id role delete request
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        roleService.removeRole(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Role, DELETED)), HttpStatus.OK);
    }

    /**
     * display all objects GET API "/service/all"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllRoles() {
        return new ResponseEntity<Response>(Response.ok().setPayload(roleService.getAllRoles()), HttpStatus.OK);
    }

    /**
     * display an object GET API "/role/id"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getOneRole(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(roleService.getOneRole(id)), HttpStatus.OK);
    }


}
