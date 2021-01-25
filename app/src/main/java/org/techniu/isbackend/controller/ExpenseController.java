package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.dto.mapper.ExpenseMapper;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.ExpenseService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.ExceptionType.UPDATED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/expense")
@CrossOrigin("*")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final MapValidationErrorService mapValidationErrorService;
    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);

    public ExpenseController(ExpenseService expenseService, MapValidationErrorService mapValidationErrorService) {
        this.expenseService = expenseService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping(value = "/all")
    public ResponseEntity getAllExpenses(@RequestBody HashMap data) {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                expenseService.getAllExpenses(data)), HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity saveExpense(@RequestBody HashMap data){
        String expenseId = String.valueOf(data.get("expenseId"));
        expenseService.saveExpense(data);
        ExceptionType exceptionType = !expenseId.isEmpty() ? UPDATED : ADDED;
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.Expense, exceptionType)), HttpStatus.OK);

    }

    @PostMapping(value = "/saveWithFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity saveExpenseWithFile(@RequestParam("type") String type,
                                              @RequestParam("employeeId") String employeeId,
                                              @RequestParam("expenseId") String expenseId,
                                              @RequestParam("expenseDate") String expenseDate,
                                              @RequestParam("voucherTypeId") String voucherTypeId,
                                              @RequestParam("status") String status,
                                              @RequestParam("localCurrencyTypeId") String localCurrencyType,
                                              @RequestParam("localCurrencyAmount") String localCurrencyAmount,
                                              @RequestParam("subtypeId") String subtypeId,
                                              @RequestParam("file") MultipartFile file,
                                              @RequestParam("expenseCountryId") String expenseCountryId,
                                              @RequestParam("expenseStateId") String expenseStateId,
                                              @RequestParam("expenseCityId") String expenseCityId,
                                              @RequestParam("arrivalDate") String arrivalDate,
                                              @RequestParam("departureDate") String departureDate,
                                              @RequestParam("description") String description,
                                              @RequestParam("fromCountryId") String fromCountryId,
                                              @RequestParam("fromStateId") String fromStateId,
                                              @RequestParam("fromCityId") String fromCityId,
                                              @RequestParam("toCountryId") String toCountryId,
                                              @RequestParam("toStateId") String toStateId,
                                              @RequestParam("toCityId") String toCityId,
                                              @RequestParam("kms") String kms,
                                              @RequestParam("personTypeIds") String[] personTypeIds,
                                              @RequestParam("personCompanyNames") String[] personCompanyNames,
                                              @RequestParam("personNames") String[] personNames,
                                              @RequestParam("personFatherFamilyNames") String[] personFatherFamilyNames,
                                              @RequestParam("personMotherFamilyNames") String[] personMotherFamilyNames) {
        HashMap data = new HashMap();
        data.put("type", type);
        data.put("employeeId", employeeId);
        data.put("expenseId", expenseId);
        data.put("expenseDate", expenseDate);
        data.put("voucherTypeId", voucherTypeId);
        data.put("status", status);
        data.put("localCurrencyTypeId", localCurrencyType);
        data.put("localCurrencyAmount", localCurrencyAmount);
        data.put("subtypeId", subtypeId);
        data.put("file", file);
        data.put("expenseCountryId", expenseCountryId);
        data.put("expenseStateId", expenseStateId);
        data.put("expenseCityId", expenseCityId);
        data.put("arrivalDate", arrivalDate);
        data.put("departureDate", departureDate);
        data.put("description", description);
        data.put("fromCountryId", fromCountryId);
        data.put("fromStateId", fromStateId);
        data.put("fromCityId", fromCityId);
        data.put("toCountryId", toCountryId);
        data.put("toStateId", toStateId);
        data.put("toCityId", toCityId);
        data.put("kms", kms);
        data.put("personTypeIds", personTypeIds);
        data.put("personCompanyNames", personCompanyNames);
        data.put("personNames", personNames);
        data.put("personFatherFamilyNames", personFatherFamilyNames);
        data.put("personMotherFamilyNames", personMotherFamilyNames);
        expenseService.saveExpenseWithFile(data);
        ExceptionType exceptionType = !expenseId.isEmpty() ? UPDATED : ADDED;
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.Expense, exceptionType)), HttpStatus.OK);
    }


    @PostMapping(value = "/changeStatus")
    public ResponseEntity changeStatusExpense(@RequestBody HashMap data) {
        expenseService.changeStatusExpense(data);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(EntityType.Expense, UPDATED)), HttpStatus.OK);
    }

    @PostMapping(value = "/export")
    public ResponseEntity exportExpenses(@RequestBody HashMap data) throws IOException {
        File file = expenseService.exportExpenses(data);
        HttpHeaders header = new HttpHeaders();
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
        ResponseEntity response = ResponseEntity
                .ok()
                .contentLength(Files.size(Paths.get(file.getPath())))
                .contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .headers(header)
                .body(Files.readAllBytes(Paths.get(file.getPath())));
        return response;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/downloadDocument/{expenseId}")
    public ResponseEntity downloadDocumentOfExpense(@PathVariable String expenseId) throws IOException {
        byte[] buffer = expenseService.downloadDocumentOfExpense(expenseId);
        HttpHeaders header = new HttpHeaders();
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"DOCUMENT.pdf\"");
        ResponseEntity response = ResponseEntity
                .ok()
                .contentLength(buffer.length)
                .contentType(MediaType.parseMediaType("application/binary"))
                .headers(header)
                .body(new InputStreamResource(new ByteArrayInputStream(buffer)));
        return response;
    }

}
