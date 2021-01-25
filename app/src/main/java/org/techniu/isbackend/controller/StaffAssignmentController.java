package org.techniu.isbackend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.service.StaffAssignmentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.StaffAssignment;
import static org.techniu.isbackend.exception.ExceptionType.UPDATED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/staffAssignment")
@CrossOrigin("*")
public class StaffAssignmentController {

    private final StaffAssignmentService staffAssignmentService;


    public StaffAssignmentController(StaffAssignmentService staffAssignmentService) {
        this.staffAssignmentService = staffAssignmentService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/filterStaff/{email}")
    public ResponseEntity filterStaffByEmail(@PathVariable String email) {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                staffAssignmentService.filterStaffByEmail(email)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/eligibleStaff/{operationId}")
    public ResponseEntity getEligibleStaff(@PathVariable String operationId) {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                staffAssignmentService.getEligibleStaff(operationId)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/allCustomerContracts")
    public ResponseEntity getAllCustomerContracts() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                staffAssignmentService.getAllCustomerContracts()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/allTreeData")
    public ResponseEntity getAllTreeData() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                staffAssignmentService.getAllTreeData()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/allCustomerContractsByEmployee/{employeeId}")
    public ResponseEntity getAllCustomerContractsByEmployee(@PathVariable String employeeId) {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                staffAssignmentService.getAllCustomerContractsByEmployee(employeeId)), HttpStatus.OK);
    }

    @PostMapping(value = "/allOperationsByEmployeeAndCustomer")
    public ResponseEntity getAllOperationsByEmployeeAndCustomer(@RequestBody HashMap<String, Object> map) {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                staffAssignmentService.getAllOperationsByEmployeeAndCustomer(map)), HttpStatus.OK);
    }

    @PostMapping(value = "/staffAssignedByOperation")
    public ResponseEntity getStaffAssignedByOperation(@RequestBody HashMap<String, Object> map) {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                staffAssignmentService.getStaffAssignedByOperation(map)), HttpStatus.OK);
    }

    @PostMapping(value = "/updateStaffOperation")
    public ResponseEntity updateStaffOperation(@RequestBody List<HashMap<String, Object>> list) {
        staffAssignmentService.updateStaffOperation(list);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(StaffAssignment, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/assignmentHistoryReport")
    public ResponseEntity getAssignmentHistoryReport() {
        return new ResponseEntity<Response>(Response.ok().setPayload(
                staffAssignmentService.getAssignmentHistoryReport()), HttpStatus.OK);
    }

    @PostMapping(value = "/export")
    public ResponseEntity exportStaffAssignment(@RequestBody HashMap data ) throws IOException {
        File file = staffAssignmentService.exportStaffAssignment(data);
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

}
