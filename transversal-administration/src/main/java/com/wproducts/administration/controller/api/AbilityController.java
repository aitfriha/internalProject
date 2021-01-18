package com.wproducts.administration.controller.api;

import com.wproducts.administration.controller.request.AbilityAddRequest;
import com.wproducts.administration.controller.request.AbilityUpdateRequest;
import com.wproducts.administration.dto.mapper.AbilityMapper;
import com.wproducts.administration.service.AbilityService;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.Response;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.Ability;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/ability")
@CrossOrigin(origins = { "http://localhost:3001" })
public class AbilityController {

    private final AbilityService abilityService;
    private final AbilityMapper abilityMapper = Mappers.getMapper(AbilityMapper.class);
    private final MapValidationErrorService mapValidationErrorService;

    public AbilityController(AbilityService abilityService, MapValidationErrorService mapValidationErrorService) {
        this.abilityService = abilityService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    /**
     * Handles the incoming POST API "/ability/add"
     *
     * @param abilityAddRequest Ability Add request
     * @return AbilityDto
     */
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid AbilityAddRequest abilityAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Create AbilityDto from abilityAddRequest and save
        abilityService.save(abilityAddRequest);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.Ability, ADDED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/ability/update"
     *
     * @param abilityUpdateRequest ability update request
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid AbilityUpdateRequest abilityUpdateRequest, BindingResult bindingResult) {
        // Handle validation errors
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);

        // Update ability
        abilityService.updateAbility(abilityUpdateRequest);

        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(Ability, UPDATED)), HttpStatus.OK);
    }
    /**
     * Handles the incoming DELETE API "/ability/delete"
     *
     * @param id service delete request
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        abilityService.removeAbility(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Ability, DELETED)), HttpStatus.OK);
    }

    /**
     * display all objects GET API "/ability/all"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllAbilities() {
        return new ResponseEntity<Response>(Response.ok().setPayload(abilityService.getAllAbilities()), HttpStatus.OK);
    }

    /**
     * display an object GET API "/ability/id"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getOneAbility(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(abilityService.getOneAbility(id)), HttpStatus.OK);
    }

}
