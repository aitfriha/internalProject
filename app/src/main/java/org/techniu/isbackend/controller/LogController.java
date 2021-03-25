package org.techniu.isbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.entity.ClassType;
import org.techniu.isbackend.entity.Leader;
import org.techniu.isbackend.entity.Log;
import org.techniu.isbackend.entity.LogType;
import org.techniu.isbackend.service.LogService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/log")
@CrossOrigin("*")
public class LogController {
    private LogService logService;
    LogController(LogService logService){
        this.logService = logService;
    }

    @RequestMapping(path = "download-excel/{className}",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveLog(@PathVariable(value = "className") String className){
        Log l = new Log();
        l.setUserName("John Doe");
       // l.setActionDate(new Date());
        l.setLogType(LogType.DOWNLOAD);
        System.out.println(className);
        switch (className) {
            case "country":
                l.setClassType(ClassType.COUNTRY);
            case "countryConfig":
                l.setClassType(ClassType.COUNTRYCONFIG);
        }
        logService.saveLog(l);
        return "done";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity allLogs() {
        return new ResponseEntity<Response>(Response.ok().setPayload(logService.getAll()), HttpStatus.OK);
    }
}
