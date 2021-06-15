package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.StaffAddrequest;
import org.techniu.isbackend.controller.request.StaffContractUpdaterequest;
import org.techniu.isbackend.dto.mapper.StaffContractMapper;
import org.techniu.isbackend.dto.mapper.StaffMapper;
import org.techniu.isbackend.dto.model.StaffContractDto;
import org.techniu.isbackend.dto.model.StaffDto;
import org.techniu.isbackend.entity.StaffContract;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.StaffContractService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.StaffContract;
import static org.techniu.isbackend.exception.ExceptionType.UPDATED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/staffContract")
@CrossOrigin("*")
public class StaffContractController {

    private StaffContractService staffContractService;
    private final MapValidationErrorService mapValidationErrorService;

    private final StaffContractMapper staffContractMapper = Mappers.getMapper(StaffContractMapper.class);

    StaffContractController(StaffContractService staffContractService, MapValidationErrorService mapValidationErrorService){
        this.staffContractService = staffContractService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PutMapping("/update")
    public ResponseEntity update(@ModelAttribute @Valid StaffContractUpdaterequest staffContractUpdaterequest, BindingResult bindingResult,
                                 @RequestParam("contractDoc") MultipartFile contractDoc,
                                 @RequestParam("internalRulesDoc") MultipartFile internalRulesDoc,
                                 @RequestParam("preContractDoc") MultipartFile preContractDoc) throws IOException {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        System.out.println("b11111");
        System.out.println(staffContractUpdaterequest);
        StaffContractDto staffContractDto = staffContractMapper.updateRequestToDto(staffContractUpdaterequest);
        if(contractDoc.getContentType().equals("application/pdf")) {
            staffContractDto.setContractDoc(contractDoc.getBytes());
            System.out.println("set contract doc");
        };
        if(internalRulesDoc.getContentType().equals("application/pdf")) {
            staffContractDto.setInternalRulesDoc(internalRulesDoc.getBytes());
            System.out.println("set internal doc");
        };
        if(internalRulesDoc.getContentType().equals("application/pdf")) {
            staffContractDto.setPreContractDoc(preContractDoc.getBytes());
            System.out.println("set contract doc");
        };
        staffContractService.update(staffContractDto);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(StaffContract, UPDATED)), HttpStatus.OK);
    }

    @GetMapping("/all-by-contractType/{contractTypeId}")
    public ResponseEntity getAllByContractType(@PathVariable("contractTypeId") String contractTypeId) {
        return new ResponseEntity<Response>(Response.ok().setPayload(staffContractService.getAllByContractType(contractTypeId)), HttpStatus.OK);
    }

    @GetMapping("/all-by-legalCategoryType/{legalCategoryTypeId}")
    public ResponseEntity getAllByLegalCategoryType(@PathVariable("legalCategoryTypeId") String legalCategoryTypeId) {
        return new ResponseEntity<Response>(Response.ok().setPayload(staffContractService.getAllByLegalCategoryType(legalCategoryTypeId)), HttpStatus.OK);
    }

    @GetMapping("/all-by-contractModel/{contractModelId}")
    public ResponseEntity getAllByContractModel(@PathVariable("contractModelId") String contractModelId) {
        return new ResponseEntity<Response>(Response.ok().setPayload(staffContractService.getAllByContractModel(contractModelId)), HttpStatus.OK);
    }

}
