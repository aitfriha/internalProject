package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.LocalBankHolidayAddrequest;
import org.techniu.isbackend.controller.request.LocalBankHolidayUpdaterequest;
import org.techniu.isbackend.dto.mapper.LocalBankHolidayMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.LocalBankHolidayService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.LocalBankHoliday;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/localBankHoliday")
@CrossOrigin("*")
public class LocalBankHolidayController {
    private LocalBankHolidayService localBankHolidayService;
    private final MapValidationErrorService mapValidationErrorService;
    private final LocalBankHolidayMapper localBankHolidayMapper = Mappers.getMapper(LocalBankHolidayMapper.class);

    LocalBankHolidayController(LocalBankHolidayService localBankHolidayService, MapValidationErrorService mapValidationErrorService){
        this.localBankHolidayService = localBankHolidayService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid LocalBankHolidayAddrequest localBankHolidayAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save LocalBankHoliday
        System.out.println(localBankHolidayMapper.addRequestToDto(localBankHolidayAddrequest));
        localBankHolidayService.save(localBankHolidayMapper.addRequestToDto(localBankHolidayAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(LocalBankHoliday, ADDED)), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity getAllLocalBankHolidays(){
        return new ResponseEntity<Response>(Response.ok().setPayload(localBankHolidayService.getAll()), HttpStatus.OK);
    }

    @GetMapping("/all-by-company/{companyId}")
    public ResponseEntity getAllByCompany(@PathVariable("companyId") String companyId){
        return new ResponseEntity<Response>(Response.ok().setPayload(localBankHolidayService.getAllByCompany(companyId)), HttpStatus.OK);

    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody LocalBankHolidayUpdaterequest localBankHolidayUpdaterequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        localBankHolidayService.update(localBankHolidayMapper.updateRequestToDto(localBankHolidayUpdaterequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(LocalBankHoliday, UPDATED)), HttpStatus.OK);

    }

    @DeleteMapping("/delete/id={id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        localBankHolidayService.remove(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(LocalBankHoliday, DELETED)), HttpStatus.OK);
    }


}
