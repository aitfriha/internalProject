package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.SelectionProcessInformationAddrequest;
import org.techniu.isbackend.controller.request.SelectionProcessInformationUpdaterequest;
import org.techniu.isbackend.dto.mapper.SelectionProcessInformationMapper;
import org.techniu.isbackend.dto.model.SelectionProcessInformationDto;
import org.techniu.isbackend.entity.SelectionProcessInformation;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.SelectionProcessInformationRepository;
import org.techniu.isbackend.service.SelectionProcessInformationService;

import javax.validation.Valid;
import java.io.IOException;

import static org.techniu.isbackend.exception.EntityType.SelectionProcessInformation;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/selectionProcessInformation")
@CrossOrigin("*")
public class SelectionProcessInformationController {
    private SelectionProcessInformationService selectionProcessInformationService;
    private SelectionProcessInformationRepository selectionProcessInformationRepository;
    private final MapValidationErrorService mapValidationErrorService;
    private final SelectionProcessInformationMapper selectionProcessInformationMapper = Mappers.getMapper(SelectionProcessInformationMapper.class);

    SelectionProcessInformationController(SelectionProcessInformationService selectionProcessInformationService,
                                          SelectionProcessInformationRepository selectionProcessInformationRepository,
                                          MapValidationErrorService mapValidationErrorService){
        this.selectionProcessInformationService = selectionProcessInformationService;
        this.selectionProcessInformationRepository = selectionProcessInformationRepository;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/add")
    public ResponseEntity add(
        @ModelAttribute("selectionProcessInformation") @Valid SelectionProcessInformationAddrequest selectionProcessInformationAddrequest,
        @RequestParam("economicProposalDoc") MultipartFile economicProposalDoc,
        @RequestParam("curriculumDoc") MultipartFile curriculumDoc,
        @RequestParam("attitudeTestDoc") MultipartFile attitudeTestDoc,
        BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save SelectionProcessInformation
        SelectionProcessInformationDto selectionProcessInformationDto = selectionProcessInformationMapper.addRequestToDto(selectionProcessInformationAddrequest);
        if(!economicProposalDoc.getContentType().equals("application/json")) {
            selectionProcessInformationDto.setEconomicProposalDoc(economicProposalDoc.getBytes());
        };
        if(!curriculumDoc.getContentType().equals("application/json")) {
            selectionProcessInformationDto.setCurriculumDoc(curriculumDoc.getBytes());
        };
        if(!attitudeTestDoc.getContentType().equals("application/json")) {
            selectionProcessInformationDto.setAttitudeTestDoc(attitudeTestDoc.getBytes());
        };
        System.out.println(selectionProcessInformationAddrequest.getCurrencyId());
        System.out.println(selectionProcessInformationDto.getCurrencyId());
        selectionProcessInformationService.save(selectionProcessInformationDto, selectionProcessInformationAddrequest.getKnowledgeIdList());
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(SelectionProcessInformation, ADDED)), HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity getAllSelectionProcessInformations(){
        return new ResponseEntity<Response>(Response.ok().setPayload(selectionProcessInformationService.getAll()), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity update(
            @ModelAttribute("selectionProcessInformation") @Valid SelectionProcessInformationUpdaterequest selectionProcessInformationUpdaterequest,
            @RequestParam("economicProposalDoc") MultipartFile economicProposalDoc,
            @RequestParam("curriculumDoc") MultipartFile curriculumDoc,
            @RequestParam("attitudeTestDoc") MultipartFile attitudeTestDoc,
            BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save SelectionProcessInformation
        SelectionProcessInformationDto selectionProcessInformationDto = selectionProcessInformationMapper.updateRequestToDto(selectionProcessInformationUpdaterequest);
        SelectionProcessInformation selectionProcessInformation = selectionProcessInformationRepository.findById(selectionProcessInformationUpdaterequest.getSelectionProcessId()).get();
        if(!economicProposalDoc.getContentType().equals("application/json")) {
            selectionProcessInformationDto.setEconomicProposalDoc(economicProposalDoc.getBytes());
        } else {
            if(selectionProcessInformation.getEconomicProposalDoc() != null) {
                selectionProcessInformationDto.setEconomicProposalDoc(selectionProcessInformation.getEconomicProposalDoc());
            }
        };
        if(!curriculumDoc.getContentType().equals("application/json")) {
            selectionProcessInformationDto.setCurriculumDoc(curriculumDoc.getBytes());
        } else {
            if(selectionProcessInformation.getCurriculumDoc() != null) {
                selectionProcessInformationDto.setCurriculumDoc(selectionProcessInformation.getCurriculumDoc());
            }
        };
        if(!attitudeTestDoc.getContentType().equals("application/json")) {
            selectionProcessInformationDto.setAttitudeTestDoc(attitudeTestDoc.getBytes());
        } else {
            if(selectionProcessInformation.getAttitudeTestDoc() != null) {
                selectionProcessInformationDto.setAttitudeTestDoc(selectionProcessInformation.getAttitudeTestDoc());
            }
        };
        System.out.println(selectionProcessInformationUpdaterequest.getCurrencyId());
        System.out.println(selectionProcessInformationDto.getCurrencyId());
        selectionProcessInformationService.update(selectionProcessInformationDto, selectionProcessInformationUpdaterequest.getKnowledgeIdList());
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(SelectionProcessInformation, UPDATED)), HttpStatus.OK);
    }


    /*@PutMapping("/update")
    public ResponseEntity update( @ModelAttribute("selectionProcessInformation") @Valid SelectionProcessInformationUpdaterequest selectionProcessInformationUpdaterequest, @RequestParam("doc") MultipartFile doc,
                                  BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        SelectionProcessInformationDto selectionProcessInformationDto = selectionProcessInformationMapper.updateRequestToDto(selectionProcessInformationUpdaterequest);
        if(!doc.getContentType().equals("application/json")) {
            selectionProcessInformationDto.setDocument(doc.getBytes());
            System.out.println("set contract doc");
        };
        selectionProcessInformationService.update(selectionProcessInformationDto);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(SelectionProcessInformation, UPDATED)), HttpStatus.OK);

    }*/

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        selectionProcessInformationService.remove(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(SelectionProcessInformation, DELETED)), HttpStatus.OK);
    }


}
