package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.TravelRequestAddRequest;
import org.techniu.isbackend.controller.request.TravelRequestUpdateRequest;
import org.techniu.isbackend.dto.mapper.TravelRequestMapper;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.TravelRequestService;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.techniu.isbackend.exception.EntityType.TravelRequest;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/travelRequest")
@CrossOrigin("*")
public class TravelRequestController {

    private final TravelRequestService travelRequestService;
    private final MapValidationErrorService mapValidationErrorService;
    private final TravelRequestMapper travelRequestMapper = Mappers.getMapper(TravelRequestMapper.class);

    public TravelRequestController(TravelRequestService travelRequestService, MapValidationErrorService mapValidationErrorService) {
        this.travelRequestService = travelRequestService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping(value = "/all")
    public ResponseEntity getAllTravelRequests(@RequestBody HashMap data) {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                travelRequestService.getAllTravelRequests(data)), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity addTravelRequest(@RequestBody @Valid TravelRequestAddRequest travelRequestAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        travelRequestService.saveTravelRequest(travelRequestMapper.addRequestToDto(travelRequestAddRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(TravelRequest, ADDED)), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity updateTravelRequest(@RequestBody @Valid TravelRequestUpdateRequest travelRequestUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.mapValidationService(bindingResult);
        }
        travelRequestService.updateTravelRequest(travelRequestMapper.updateRequestToDto(travelRequestUpdateRequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(TravelRequest, UPDATED)), HttpStatus.OK);
    }

    @PostMapping(value = "/changeStatus")
    public ResponseEntity changeStatusTravelRequest(@RequestBody HashMap data) {
        travelRequestService.changeStatusTravelRequest(data);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(TravelRequest, UPDATED)), HttpStatus.OK);
    }

    @PostMapping(value = "/export")
    public ResponseEntity exportTravelRequests(@RequestBody HashMap data) throws IOException {
        File file = travelRequestService.exportTravelRequests(data);
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

    @RequestMapping(method = RequestMethod.GET, value = "/downloadDocuments/{travelRequestId}")
    public ResponseEntity downloadDocumentsOfTravelRequest(@PathVariable String travelRequestId) throws IOException {
        byte[] buffer = travelRequestService.downloadDocumentsOfTravelRequest(travelRequestId);
        HttpHeaders header = new HttpHeaders();
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"DOCUMENTS.zip\"");
        ResponseEntity response = ResponseEntity
                .ok()
                .contentLength(buffer.length)
                .contentType(MediaType.parseMediaType("application/binary"))
                .headers(header)
                .body(new InputStreamResource(new ByteArrayInputStream(buffer)));
        return response;
    }

    @PostMapping(value = "/approve", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity approveTravelRequests(@RequestParam("files") MultipartFile[] files,
                                                @RequestParam("localCurrencyTypes") String[] localCurrencyTypes,
                                                @RequestParam("localCurrencyAmounts") String[] localCurrencyAmounts,
                                                @RequestParam("travelRequestId") String travelRequestId,
                                                @RequestParam("status") String status) {
        HashMap data = new HashMap();
        data.put("travelRequestId", travelRequestId);
        data.put("status", status);
        data.put("files", files);
        data.put("localCurrencyTypes", localCurrencyTypes);
        data.put("localCurrencyAmounts", localCurrencyAmounts);
        travelRequestService.approveTravelRequest(data);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(TravelRequest, APPROVED)), HttpStatus.OK);
    }
}
