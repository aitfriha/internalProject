package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.SelectionTypeEvaluationAddrequest;
import org.techniu.isbackend.controller.request.SelectionTypeEvaluationUpdaterequest;
import org.techniu.isbackend.dto.mapper.SelectionTypeEvaluationMapper;
import org.techniu.isbackend.dto.model.SelectionTypeEvaluationDto;
import org.techniu.isbackend.entity.SelectionTypeEvaluation;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.SelectionTypeEvaluationService;

import java.util.ArrayList;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.SelectionTypeEvaluation;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/selectionTypeEvaluation")
@CrossOrigin("*")
public class SelectionTypeEvaluationController {
    private SelectionTypeEvaluationService selectionTypeEvaluationService;

    private final MapValidationErrorService mapValidationErrorService;
    private final SelectionTypeEvaluationMapper selectionTypeEvaluationMapper = Mappers.getMapper(SelectionTypeEvaluationMapper.class);

    SelectionTypeEvaluationController(SelectionTypeEvaluationService selectionTypeEvaluationService, MapValidationErrorService mapValidationErrorService){
        this.selectionTypeEvaluationService = selectionTypeEvaluationService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody List<SelectionTypeEvaluationAddrequest> list, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save SelectionTypeEvaluation
        List<SelectionTypeEvaluationDto> dtoList = new ArrayList<>();
        list.forEach(element -> {
            dtoList.add(selectionTypeEvaluationMapper.addRequestToDto(element));
        });
        selectionTypeEvaluationService.save(dtoList) ;
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(SelectionTypeEvaluation, ADDED)), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity getAllSelectionTypeEvaluations(){
        return new ResponseEntity<Response>(Response.ok().setPayload(selectionTypeEvaluationService.getAll()), HttpStatus.OK);
    }

    @GetMapping("/all-by-type/{type}")
    public ResponseEntity getAllByType(@PathVariable("type") String type){
        return new ResponseEntity<Response>(Response.ok().setPayload(selectionTypeEvaluationService.getAllByType(type)), HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity update(@RequestBody SelectionTypeEvaluationUpdaterequest selectionTypeEvaluationUpdaterequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        selectionTypeEvaluationService.update(selectionTypeEvaluationMapper.updateRequestToDto(selectionTypeEvaluationUpdaterequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(SelectionTypeEvaluation, UPDATED)), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        selectionTypeEvaluationService.remove(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(SelectionTypeEvaluation, DELETED)), HttpStatus.OK);
    }

    @GetMapping("/level-name/{name}")
    public SelectionTypeEvaluation getSelectionTypeByName(@PathVariable(value = "name") String name){
        return selectionTypeEvaluationService.getSelectionTypeByName(name);
    }

}

