package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.dto.mapper.WeeklyReportMapper;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.WeeklyReportService;

import java.util.HashMap;

import static org.techniu.isbackend.exception.EntityType.WeeklyReport;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.ExceptionType.UPDATED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/weeklyReport")
@CrossOrigin("*")
public class WeeklyReportController {

    private final WeeklyReportService weeklyReportService;
    private final MapValidationErrorService mapValidationErrorService;
    private final WeeklyReportMapper weeklyReportMapper = Mappers.getMapper(WeeklyReportMapper.class);

    public WeeklyReportController(WeeklyReportService weeklyReportService, MapValidationErrorService mapValidationErrorService) {
        this.weeklyReportService = weeklyReportService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping(value = "/allSummarized")
    public ResponseEntity getAllSummarizedWeeklyReportByStaff(@RequestBody HashMap<String, Object> map) {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                weeklyReportService.getAllSummarizedWeeklyReportByStaff(map)), HttpStatus.OK);
    }

    @PostMapping(value = "/getExtended")
    public ResponseEntity getExtendedWeeklyReport(@RequestBody HashMap<String, Object> map) {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                weeklyReportService.getExtendedWeeklyReport(map)), HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity saveWeeklyReport(@RequestBody HashMap data) {
        weeklyReportService.saveWeeklyReport(data);
        ExceptionType exceptionType = String.valueOf(data.get("id")).isEmpty() ? ADDED : UPDATED;
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(WeeklyReport, exceptionType)), HttpStatus.OK);
    }

}
