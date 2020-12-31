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
import org.techniu.isbackend.controller.request.AbsenceRequestAddrequest;
import org.techniu.isbackend.controller.request.AbsenceRequestUpdaterequest;
import org.techniu.isbackend.controller.request.AbsenceTypeUpdaterequest;
import org.techniu.isbackend.dto.mapper.AbsenceRequestMapper;
import org.techniu.isbackend.dto.model.AbsenceRequestDto;
import org.techniu.isbackend.dto.model.AbsenceRequestDto;
import org.techniu.isbackend.dto.model.AbsenceTypeDto;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.AbsenceRequestService;
import org.techniu.isbackend.service.utilities.MailMail;

import javax.mail.MessagingException;
import javax.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.*;
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
    public ResponseEntity add(@ModelAttribute("absenceRequest") AbsenceRequestAddrequest absenceRequestAddrequest,
                              @RequestParam("docExtensionList") List<String> docExtensionList,
                              @RequestParam("docList") List<MultipartFile> docList,
                              BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save AbsenceRequest
        List<byte[]> documentList = new ArrayList<>();
        AbsenceRequestDto absenceRequestDto = absenceRequestMapper.addRequestToDto(absenceRequestAddrequest);

        docList.forEach(doc-> {
            try {
                documentList.add(doc.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        absenceRequestDto.setDocumentList(documentList);
        absenceRequestDto.setDocExtensionList(docExtensionList);
        System.out.println(absenceRequestAddrequest);
        absenceRequestService.save(absenceRequestDto,
                absenceRequestAddrequest.getFromName());
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(AbsenceRequest, ADDED)), HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity getAllAbsenceRequests(){
        return new ResponseEntity<Response>(Response.ok().setPayload(absenceRequestService.getAll()), HttpStatus.OK);
    }

    @GetMapping("/all-by-absenceType/{absenceTypeId}")
    public ResponseEntity getAllByAbsenceType(@PathVariable("absenceTypeId") String absenceTypeId) {
        return new ResponseEntity<Response>(Response.ok().setPayload(absenceRequestService.getAllByAbsenceType(absenceTypeId)), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody @Valid AbsenceRequestUpdaterequest absenceRequestUpdaterequest,
                                 BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        System.out.println("is update");
        AbsenceRequestDto absenceRequestDto = absenceRequestMapper.updateRequestToDto(absenceRequestUpdaterequest);
        System.out.println(absenceRequestDto);
        absenceRequestService.update(absenceRequestDto);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(AbsenceRequest, UPDATED)), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        absenceRequestService.remove(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(AbsenceRequest, DELETED)), HttpStatus.OK);
    }


}
