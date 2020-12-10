package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.AbsenceRequestAddrequest;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.*;
import org.techniu.isbackend.dto.mapper.AbsenceRequestMapper;
import org.techniu.isbackend.dto.model.AbsenceRequestDto;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.AbsenceRequestService;
import org.techniu.isbackend.service.utilities.MailMail;

import javax.mail.MessagingException;
import javax.validation.Valid;

import java.io.IOException;

import static org.techniu.isbackend.exception.EntityType.AbsenceRequest;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/absenceRequest")
@CrossOrigin("*")
public class AbsenceRequestController {
    private AbsenceRequestService absenceRequestService;
    private final MapValidationErrorService mapValidationErrorService;
    private final AbsenceRequestMapper absenceRequestMapper = Mappers.getMapper(AbsenceRequestMapper.class);

    AbsenceRequestController(AbsenceRequestService absenceRequestService,
                             MapValidationErrorService mapValidationErrorService){
        this.absenceRequestService = absenceRequestService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid AbsenceRequestAddrequest absenceRequestAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save AbsenceRequest
        System.out.println(absenceRequestMapper.addRequestToDto(absenceRequestAddrequest));
        absenceRequestService.save(absenceRequestMapper.addRequestToDto(absenceRequestAddrequest),
                absenceRequestAddrequest.getSendToName(),
                absenceRequestAddrequest.getFromName(),
                absenceRequestAddrequest.getSendToEmail());

        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(AbsenceRequest, ADDED)), HttpStatus.OK);
    }

    public ResponseEntity add(@ModelAttribute("absenceRequest") AbsenceRequestAddrequest absenceRequestAddrequest,
                              @RequestParam("doc") MultipartFile doc,
                              BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        AbsenceRequestDto absenceRequestDto = absenceRequestMapper.addRequestToDto(absenceRequestAddrequest);
        absenceRequestDto.setDocument(doc.getBytes());
        System.out.println(absenceRequestAddrequest);
        absenceRequestService.save(absenceRequestDto,
                absenceRequestAddrequest.getSendToName(),
                absenceRequestAddrequest.getFromName(),
                absenceRequestAddrequest.getSendToEmail());
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(AbsenceRequest, ADDED)), HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity getAllAbsenceRequests(){
        return new ResponseEntity<Response>(Response.ok().setPayload(absenceRequestService.getAll()), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        absenceRequestService.remove(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(AbsenceRequest, DELETED)), HttpStatus.OK);
    }


}
