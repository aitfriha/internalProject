package com.wproducts.administration.controller.api;


import com.wproducts.administration.controller.request.MachineAddRequest;
import com.wproducts.administration.controller.request.MachineUpdateRequest;
import com.wproducts.administration.dto.mapper.MachineMapper;
import com.wproducts.administration.service.MachineService;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.Response;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.Machine;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/machine")
@CrossOrigin(origins = { "http://localhost:3001" })
public class machineController {


    private final MachineService machineService;
    private final MachineMapper machineMapper = Mappers.getMapper(MachineMapper.class);
    private final MapValidationErrorService mapValidationErrorService;

    public machineController(MachineService machineService, MapValidationErrorService mapValidationErrorService) {
        this.machineService = machineService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    /**
     * Handles the incoming POST API "/machine/add"
     *
     * @param machineAddRequest Machine Add request
     * @return MachineDto
     */
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid MachineAddRequest machineAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Create MachineDto from machineAddRequest and save
        machineService.save(machineMapper.addRequestToDto(machineAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.Machine, ADDED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/machine/update"
     *
     * @param machineUpdateRequest machine update request
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid MachineUpdateRequest machineUpdateRequest, BindingResult bindingResult) {
        // Handle validation errors
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);

        // Update machine
        machineService.updateMachine(machineMapper.updateRequestToDto(machineUpdateRequest));

        return new ResponseEntity<Response>(Response.ok().setPayload(
                getMessageTemplate(Machine, UPDATED)), HttpStatus.OK);
    }
    /**
     * Handles the incoming DELETE API "/machine/delete"
     *
     * @param id service delete request
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        machineService.removeMachine(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Machine, DELETED)), HttpStatus.OK);
    }

    /**
     * display all objects GET API "/machine/all"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllMachines() {
        return new ResponseEntity<Response>(Response.ok().setPayload(machineService.getAllMachines()), HttpStatus.OK);
    }

    /**
     * display an object GET API "/machine/id"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getOneMachine(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(machineService.getOneMachine(id)), HttpStatus.OK);
    }
}
