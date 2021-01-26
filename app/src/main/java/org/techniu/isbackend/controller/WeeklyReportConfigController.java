package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.WeeklyReportConfigUpdateRequest;
import org.techniu.isbackend.dto.mapper.WeeklyReportConfigMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.WeeklyReportConfigService;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.WeeklyReportConfig;
import static org.techniu.isbackend.exception.ExceptionType.UPDATED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/weeklyReportConfig")
@CrossOrigin("*")
public class WeeklyReportConfigController {

    private final WeeklyReportConfigService weeklyReportConfigService;
    private final MapValidationErrorService mapValidationErrorService;
    private final WeeklyReportConfigMapper weeklyReportConfigMapper = Mappers.getMapper(WeeklyReportConfigMapper.class);

    public WeeklyReportConfigController(WeeklyReportConfigService weeklyReportConfigService, MapValidationErrorService mapValidationErrorService) {
        this.weeklyReportConfigService = weeklyReportConfigService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get")
    public ResponseEntity getWeeklyReportConfig() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                weeklyReportConfigService.getConfiguration()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/{id}")
    public ResponseEntity getWeeklyReportConfigById(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                weeklyReportConfigService.getConfigurationById(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity saveAllWeeklyReportConfig(@RequestBody @Valid WeeklyReportConfigUpdateRequest weeklyReportConfigUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        weeklyReportConfigService.updateConfiguration(weeklyReportConfigMapper.updateRequestToDto(weeklyReportConfigUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(WeeklyReportConfig, UPDATED)), HttpStatus.OK);
    }
}
