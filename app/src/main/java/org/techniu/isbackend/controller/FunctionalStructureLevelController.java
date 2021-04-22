package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.FunctionalStructureLevelUpdaterequest;
import org.techniu.isbackend.dto.mapper.FunctionalStructureLevelMapper;
import org.techniu.isbackend.dto.mapper.LegalCategoryTypeMapper;
import org.techniu.isbackend.dto.model.FunctionalStructureLevelDto;
import org.techniu.isbackend.entity.FunctionalStructureLevel;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.FunctionalStructureLevelService;

import java.util.List;

import static org.techniu.isbackend.exception.EntityType.*;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/functionalStructure")
@CrossOrigin("*")
public class FunctionalStructureLevelController {
    private FunctionalStructureLevelService functionalStructureLevelService;

    private final MapValidationErrorService mapValidationErrorService;
    private final FunctionalStructureLevelMapper functionalStructureLevelMapper = Mappers.getMapper(FunctionalStructureLevelMapper.class);

    FunctionalStructureLevelController(FunctionalStructureLevelService functionalStructureLevelService, MapValidationErrorService mapValidationErrorService){
        this.functionalStructureLevelService = functionalStructureLevelService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody List<Object> objects, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save FunctionalStructureLevel
        functionalStructureLevelService.save(objects) ;
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(FunctionalStructureLevel, ADDED)), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity getAllFunctionalStructureLevels(){
        return new ResponseEntity<Response>(Response.ok().setPayload(functionalStructureLevelService.getAll()), HttpStatus.OK);
    }

    @GetMapping("/all-by-type/{type}")
    public ResponseEntity getAllByType(@PathVariable("type") String type){
        return new ResponseEntity<Response>(Response.ok().setPayload(functionalStructureLevelService.getAllByType(type)), HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity update(@RequestBody FunctionalStructureLevelUpdaterequest functionalStructureLevelUpdaterequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        functionalStructureLevelService.update(functionalStructureLevelMapper.updateRequestToDto(functionalStructureLevelUpdaterequest), functionalStructureLevelUpdaterequest.getOldLeaderId(), functionalStructureLevelUpdaterequest.getNewLeaderId());
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(FunctionalStructureLevel, UPDATED)), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        functionalStructureLevelService.remove(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(FunctionalStructureLevel, DELETED)), HttpStatus.OK);
    }

    @GetMapping("/level-tree/{levelId}")
    public List<FunctionalStructureLevel> getFunctionalStructureTree(@PathVariable(value = "levelId") String levelId){
        return functionalStructureLevelService.getFunctionalStructureTree(levelId);
    }

    @GetMapping("/level-name/{name}")
    public FunctionalStructureLevel getLevelByName(@PathVariable(value = "name") String name){
        return functionalStructureLevelService.getLevelByName(name);
    }



    @PostMapping("/level-assign")
    public ResponseEntity setLevelStaffs(@RequestBody List<Object> objects){
         functionalStructureLevelService.setLevelStaffs(objects) ;
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(FunctionalStructureLevel, ASSIGNED_LEVEL_STAFF)), HttpStatus.OK);

    }
}

