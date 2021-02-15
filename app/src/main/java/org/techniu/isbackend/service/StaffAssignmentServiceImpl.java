package org.techniu.isbackend.service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.FinancialContractMapper;
import org.techniu.isbackend.dto.mapper.StaffAssignmentMapper;
import org.techniu.isbackend.dto.mapper.StaffMapper;
import org.techniu.isbackend.dto.model.*;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.techniu.isbackend.exception.ExceptionType.COULD_NOT_BE_UPDATED;
import static org.techniu.isbackend.exception.ExceptionType.NOT_GENERATED;
import static org.techniu.isbackend.service.utilities.Util.deleteTemporalFilesOnServer;


@Service
public class StaffAssignmentServiceImpl implements StaffAssignmentService {

    private final String temporalDocumentsDirectory = "TEMPORAL DOCUMENTS";
    private FinancialContractRepository contractRepository;
    private CommercialOperationRepository commercialOperationRepository;
    private StaffRepository staffRepository;
    private StaffAssignmentRepository staffAssignmentRepository;
    private FunctionalStructureLevelRepository functionalStructureRepository;
    private final StaffMapper staffMapper = Mappers.getMapper(StaffMapper.class);
    private final StaffAssignmentMapper staffAssignmentMapper = Mappers.getMapper(StaffAssignmentMapper.class);
    private final FinancialContractMapper financialContractMapper = Mappers.getMapper(FinancialContractMapper.class);

    public StaffAssignmentServiceImpl(FinancialContractRepository contractRepository,
                                      CommercialOperationRepository commercialOperationRepository,
                                      StaffRepository staffRepository,
                                      StaffAssignmentRepository staffAssignmentRepository,
                                      FunctionalStructureLevelRepository functionalStructureRepository) {
        this.contractRepository = contractRepository;
        this.commercialOperationRepository = commercialOperationRepository;
        this.staffRepository = staffRepository;
        this.staffAssignmentRepository = staffAssignmentRepository;
        this.functionalStructureRepository = functionalStructureRepository;
    }

    @Override
    public List<StaffDto> getEligibleStaff(String operationId) {
        ArrayList<StaffDto> listDto = new ArrayList<>();
        CommercialOperation operation = commercialOperationRepository.findBy_id(operationId);
        if (operation != null) {
            FinancialContract customerContract = contractRepository.findByCommercialOperation(operation);
            if (customerContract != null) {
                List<Staff> list = getAllStaffByFunctionalStructure(customerContract.getFunctionalStructureLevel(), new ArrayList<>());
                list.forEach(obj -> {
                    StaffDto staffDto = staffMapper.modelToDto(obj);
                    listDto.add(staffDto);
                });
            }
        }
        return listDto;
    }

    private List<Staff> getAllStaffByFunctionalStructure(FunctionalStructureLevel functionalStructure, List<Staff> list) {
        list.addAll(staffRepository.findAllByFunctionalStructureLevelsContaining(functionalStructure));
        List<FunctionalStructureLevel> children = functionalStructure.getChilds();
        if(children != null && !children.isEmpty()) {
            children.forEach(child -> {
                getAllStaffByFunctionalStructure(child, list);
            });
        }
        /*//Add all staff associated to the Functional Structure parameter
        list.addAll(staffRepository.findAllByFunctionalStructureLevel(functionalStructure));
        List<FunctionalStructureLevel> children = functionalStructureRepository.findAllByFather(functionalStructure);
        children.forEach(child -> {
            getAllStaffByFunctionalStructure(child, list);
        });*/
        return list;
    }

    @Override
    public List<StaffDto> filterStaffByEmail(String email) {
        ArrayList<StaffDto> listDto = new ArrayList<>();
        if (email != null && !email.trim().isEmpty()) {
            // Get all staff
            List<Staff> list = staffRepository.findAll();
            list.forEach(obj -> {
                if (obj.getCompanyEmail().contains(email)) {
                    StaffDto staffDto = staffMapper.modelToDto(obj);
                    listDto.add(staffDto);
                }
            });
        }
        return listDto;
    }

    @Override
    public List<FinancialContractDto> getAllCustomerContracts() {
        // Get all customer contracts
        List<FinancialContract> list = contractRepository.findAll();
        // Create a list of all customer contracts dto
        ArrayList<FinancialContractDto> listDto = new ArrayList<>();
        list.forEach(contract -> {
            if (contract.getFunctionalStructureLevel().getIsProductionLevel().equalsIgnoreCase("yes")) {
                FinancialContractDto customerContractDto = financialContractMapper.modelToDto(contract);
                listDto.add(customerContractDto);
            }
        });
        return listDto;
    }

    @Override
    public List<TreeData> getAllTreeData() {
        List<TreeData> finalList = new ArrayList<>();
        List<FinancialContractDto> customerContracts = getAllCustomerContracts();
        for (FinancialContractDto c : customerContracts) {
            if (c.getStatusCode() != 10) { //Validate that the contract is not finished (status == 10)
                TreeData temp = new TreeData();

                /*Client client = clientRepository.findBy_id(c.getClientId());
                ClientDto customerDto = customerMapper.modelToDto(client);*/
                ClientDto customerDto = new ClientDto();
                customerDto.setClientId(c.getClientId());
                customerDto.setCode(c.getClientCode());
                customerDto.setName(c.getClientName());

                temp.setCustomer(customerDto);

                int pos = finalList.indexOf(temp);

                CommercialOperationDto operationDto = new CommercialOperationDto();
                operationDto.setCommercialOperationId(c.getOperationId());
                operationDto.setCode(c.getOperationCode());
                operationDto.setName(c.getOperationName());

                if (pos == -1) {
                    temp.addOperation(operationDto);
                    finalList.add(temp);
                } else {
                    finalList.get(pos).addOperation(operationDto);
                }
            }
        }
        //DELETE ALL TEMPORAL DOCUMENTS
        deleteTemporalFilesOnServer();
        return finalList;
    }

    @Override
    public List<FinancialContractDto> getAllCustomerContractsByEmployee(String employeeId) {
        List<FinancialContractDto> result = new ArrayList<>();
        List<FinancialContractDto> customerContracts = getAllCustomerContracts();
        for (FinancialContractDto c : customerContracts) {
            if (c.getStatusCode() != 10) { //Validate that the contract is not finished (status == 10)
                CommercialOperation operation = commercialOperationRepository.findBy_id(c.getOperationId());
                List<StaffAssignment> assignments = staffAssignmentRepository.findAllByOperationAndActive(operation, true);
                AtomicBoolean add = new AtomicBoolean(false);
                assignments.forEach(el -> {
                    if (el.getStaff().getStaffId().equalsIgnoreCase(employeeId)) {
                        add.set(true);
                        return;
                    }
                });
                if (add.get()) {
                    result.add(c);
                }
            }
        }
        return result;
    }

    @Override
    public List<FinancialContractDto> getAllCustomerContractsByCompanyEmail(String companyEmail) {
        List<FinancialContractDto> result = new ArrayList<>();
        List<FinancialContractDto> customerContracts = getAllCustomerContracts();
        for (FinancialContractDto c : customerContracts) {
            if (c.getStatusCode() != 10) { //Validate that the contract is not finished (status == 10)
                CommercialOperation operation = commercialOperationRepository.findBy_id(c.getOperationId());
                List<StaffAssignment> assignments = staffAssignmentRepository.findAllByOperationAndActive(operation, true);
                AtomicBoolean add = new AtomicBoolean(false);
                assignments.forEach(el -> {
                    if (el.getStaff().getCompanyEmail().equalsIgnoreCase(companyEmail)) {
                        add.set(true);
                        return;
                    }
                });
                if (add.get()) {
                    result.add(c);
                }
            }
        }
        return result;
    }

    @Override
    public List<CommercialOperationDto> getAllOperationsByEmployeeAndCustomer(HashMap data) {
        List<CommercialOperationDto> result = new ArrayList<>();
        String employeeId = (String) data.get("employeeId");
        String customerId = (String) data.get("customerId");
        if (!employeeId.isEmpty() && !customerId.isEmpty()) {
            List<FinancialContractDto> contractList = getAllCustomerContractsByEmployee(employeeId);
            for (FinancialContractDto c : contractList) {
                if (c.getStatusCode() != 10) { //Validate that the contract is not finished (status == 10)
                    if (c.getClientId().equalsIgnoreCase(customerId)) {
                        CommercialOperationDto operationDto = new CommercialOperationDto();
                        operationDto.setCommercialOperationId(c.getOperationId());
                        operationDto.setCode(c.getOperationCode());
                        operationDto.setName(c.getOperationName());
                        result.add(operationDto);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<StaffAssignmentDto> getStaffAssignedByOperation(HashMap data) {
        String operationId = (String) data.get("operationId");
        CommercialOperation operation = commercialOperationRepository.findBy_id(operationId);
        List<StaffAssignment> list = new ArrayList<>();
        List<StaffAssignmentDto> result = new ArrayList<>();
        if (operation != null) {
            if (data.containsKey("active")) {
                boolean active = Boolean.valueOf(String.valueOf(data.get("active")));
                list = staffAssignmentRepository.findAllByOperationAndActive(operation, active);
            } else {
                list = staffAssignmentRepository.findAllByOperation(operation);
            }
        }
        Collections.sort(list, Comparator.comparing(a -> a.getStaff().getFullName()));
        list.forEach(obj -> {
            StaffAssignmentDto staffAssignmentDto = staffAssignmentMapper.modelToDto(obj);
            staffAssignmentDto.setEmployeeId(obj.getStaff().getStaffId());
            staffAssignmentDto.setPersonalNumber(obj.getStaff().getStaffContract().getPersonalNumber());
            staffAssignmentDto.setAvatar(obj.getStaff().getPhoto());
            staffAssignmentDto.setName(obj.getStaff().getFirstName());
            staffAssignmentDto.setFatherFamilyName(obj.getStaff().getFatherFamilyName());
            staffAssignmentDto.setMotherFamilyName(obj.getStaff().getMotherFamilyName());
            staffAssignmentDto.setCompany(obj.getStaff().getStaffContract().getCompany().getName());
            staffAssignmentDto.setCompanyEmail(obj.getStaff().getCompanyEmail());
            staffAssignmentDto.setOperationId(obj.getOperation().get_id());
            staffAssignmentDto.setOperationCode(obj.getOperation().getCode());
            staffAssignmentDto.setOperationName(obj.getOperation().getName());
            result.add(staffAssignmentDto);
        });

        //DELETE ALL TEMPORAL DOCUMENTS
        deleteTemporalFilesOnServer();
        return result;
    }

    @Override
    public void updateStaffOperation(List<HashMap<String, Object>> list) {
        AtomicInteger errorCount = new AtomicInteger(0);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        list.forEach(assignment -> {
            String id = (String) assignment.get("id");
            String operationId = (String) assignment.get("operationId");
            String employeeId = (String) assignment.get("employeeId");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = assignment.get("startDate") != null ? formatDate.parse(String.valueOf(assignment.get("startDate"))) : null;
                endDate = assignment.get("endDate") != null ? formatDate.parse(String.valueOf(assignment.get("endDate"))) : null;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            boolean active = Boolean.valueOf(String.valueOf(assignment.get("active")));

            CommercialOperation operation = commercialOperationRepository.findBy_id(operationId);
            Staff staff = staffRepository.findAllByStaffId(employeeId);

            if (operation != null && staff != null) {
                StaffAssignment staffAssignment = id.equalsIgnoreCase("new") ? new StaffAssignment() : staffAssignmentRepository.findStaffAssignmentBy_id(id);
                staffAssignment.setOperation(operation);
                staffAssignment.setStaff(staff);
                staffAssignment.setStartDate(startDate);
                staffAssignment.setEndDate(endDate);
                staffAssignment.setActive(active);

                staffAssignmentRepository.save(staffAssignment);
            } else {
                errorCount.getAndIncrement();
            }
        });

        if (errorCount.get() > 0) {
            throw exception(COULD_NOT_BE_UPDATED);
        }
    }

    public List<AssignmentHistoryReport> getAssignmentHistoryReport() {
        List<AssignmentHistoryReport> result = new ArrayList<>();
        List<TreeData> treeData = getAllTreeData();
        treeData.forEach(el -> {
            String customerId = el.getCustomer().getClientId();
            String customerCode = el.getCustomer().getCode();
            String customerName = el.getCustomer().getName();
            el.getOperations().forEach(operation -> {
                HashMap params = new HashMap();
                params.put("operationId", operation.getCommercialOperationId());
                List<StaffAssignmentDto> staffAssignmentList = getStaffAssignedByOperation(params);
                staffAssignmentList.forEach(assignment -> {
                    AssignmentHistoryReport assignmentHistoryReport = new AssignmentHistoryReport();
                    assignmentHistoryReport.setId(assignment.getId());

                    assignmentHistoryReport.setCustomerCode(customerCode);
                    assignmentHistoryReport.setCustomerName(customerName);

                    assignmentHistoryReport.setPersonalNumber(assignment.getPersonalNumber());
                    assignmentHistoryReport.setName(assignment.getName());
                    assignmentHistoryReport.setFatherFamilyName(assignment.getFatherFamilyName());
                    assignmentHistoryReport.setMotherFamilyName(assignment.getMotherFamilyName());
                    assignmentHistoryReport.setCompany(assignment.getCompany());
                    assignmentHistoryReport.setCompanyEmail(assignment.getCompanyEmail());

                    assignmentHistoryReport.setOperationCode(assignment.getOperationCode());
                    assignmentHistoryReport.setOperationName(assignment.getOperationName());

                    assignmentHistoryReport.setStartDate(assignment.getStartDate());
                    assignmentHistoryReport.setEndDate(assignment.getEndDate());

                    result.add(assignmentHistoryReport);
                });
            });
        });
        return result;
    }


    @Override
    public File exportStaffAssignment(HashMap params) {
        boolean generated = generateFile(params);
        if (generated) {
            //return the file from server location
            String fileType = String.valueOf(params.get("fileType"));
            String option = String.valueOf(params.get("option"));
            String fileName = (option.equalsIgnoreCase("HISTORY") ? temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "STAFF ASSIGNMENT HISTORY" : temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "STAFF ASSIGNMENT HISTORY BY OPERATION") + (fileType.equals("excel") ? ".xlsx" : ".pdf");
            return new File(fileName);
        } else {
            throw exceptionFile(NOT_GENERATED);
        }
    }


    private boolean generateFile(HashMap params) {
        boolean generated = true;
        String option = String.valueOf(params.get("option"));
        String fileType = String.valueOf(params.get("fileType"));
        String fileName = "";

        Workbook workbook = new XSSFWorkbook();

        XSSFFont tableHeaderFont = ((XSSFWorkbook) workbook).createFont();
        tableHeaderFont.setFontName("Helvetica");
        tableHeaderFont.setFontHeight(10);
        tableHeaderFont.setBold(true);

        //Create a header row
        CellStyle tableHeaderStyle = workbook.createCellStyle();
        tableHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        tableHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        tableHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        tableHeaderStyle.setFont(tableHeaderFont);

        //Create empty row style
        CellStyle emptyRowStyle = workbook.createCellStyle();
        emptyRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        emptyRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        emptyRowStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont nameFont = ((XSSFWorkbook) workbook).createFont();
        nameFont.setFontName("Helvetica");
        nameFont.setFontHeight(10);
        nameFont.setBold(true);

        CellStyle nameStyle = workbook.createCellStyle();
        nameStyle.setAlignment(HorizontalAlignment.RIGHT);
        nameStyle.setFont(nameFont);

        CellStyle dataRowStyle = workbook.createCellStyle();
        dataRowStyle.setAlignment(HorizontalAlignment.CENTER);

        switch (fileType) {
            case "excel": {
                if (option.equalsIgnoreCase("HISTORY")) {
                    List<AssignmentHistoryReport> list = getAssignmentHistoryReport();
                    Sheet sheet = workbook.createSheet("STAFF ASSIGNMENT HISTORY");

                    Row headerRow = sheet.createRow(0);
                    Cell cell_0 = headerRow.createCell(0, CellType.STRING);
                    cell_0.setCellValue("Cust. Code");
                    cell_0.setCellStyle(tableHeaderStyle);

                    Cell cell_1 = headerRow.createCell(1, CellType.STRING);
                    cell_1.setCellValue("Customer Name");
                    cell_1.setCellStyle(tableHeaderStyle);

                    Cell cell_2 = headerRow.createCell(2, CellType.STRING);
                    cell_2.setCellValue("Opt. Code");
                    cell_2.setCellStyle(tableHeaderStyle);

                    Cell cell_3 = headerRow.createCell(3, CellType.STRING);
                    cell_3.setCellValue("Operation Name");
                    cell_3.setCellStyle(tableHeaderStyle);

                    Cell cell_4 = headerRow.createCell(4, CellType.STRING);
                    cell_4.setCellValue("Employee Name");
                    cell_4.setCellStyle(tableHeaderStyle);

                    Cell cell_6 = headerRow.createCell(5, CellType.STRING);
                    cell_6.setCellValue("Company");
                    cell_6.setCellStyle(tableHeaderStyle);

                    Cell cell_7 = headerRow.createCell(6, CellType.STRING);
                    cell_7.setCellValue("Company Email");
                    cell_7.setCellStyle(tableHeaderStyle);

                    Cell cell_8 = headerRow.createCell(7, CellType.STRING);
                    cell_8.setCellValue("Start Date");
                    cell_8.setCellStyle(tableHeaderStyle);


                    Cell cell_9 = headerRow.createCell(8, CellType.STRING);
                    cell_9.setCellValue("End Date");
                    cell_9.setCellStyle(tableHeaderStyle);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
                    AtomicInteger pos = new AtomicInteger(0);
                    list.forEach(assignmentHistoryReport -> {
                        Row dataRow = sheet.createRow(pos.incrementAndGet());

                        Cell dataCell_0 = dataRow.createCell(0, CellType.STRING);
                        dataCell_0.setCellValue(assignmentHistoryReport.getCustomerCode());
                        dataCell_0.setCellStyle(dataRowStyle);

                        Cell dataCell_1 = dataRow.createCell(1, CellType.STRING);
                        dataCell_1.setCellValue(assignmentHistoryReport.getCustomerName());
                        dataCell_1.setCellStyle(dataRowStyle);

                        Cell dataCell_2 = dataRow.createCell(2, CellType.STRING);
                        dataCell_2.setCellValue(assignmentHistoryReport.getOperationCode());
                        dataCell_2.setCellStyle(dataRowStyle);


                        Cell dataCell_3 = dataRow.createCell(3, CellType.STRING);
                        dataCell_3.setCellValue(assignmentHistoryReport.getOperationName());
                        dataCell_3.setCellStyle(dataRowStyle);

                        Cell dataCell_4 = dataRow.createCell(4, CellType.STRING);
                        dataCell_4.setCellValue(assignmentHistoryReport.getName() + " " + assignmentHistoryReport.getFatherFamilyName() + " " + assignmentHistoryReport.getMotherFamilyName());
                        dataCell_4.setCellStyle(dataRowStyle);

                        Cell dataCell_6 = dataRow.createCell(5, CellType.STRING);
                        dataCell_6.setCellValue(assignmentHistoryReport.getCompany());
                        dataCell_6.setCellStyle(dataRowStyle);

                        Cell dataCell_7 = dataRow.createCell(6, CellType.STRING);
                        dataCell_7.setCellValue(assignmentHistoryReport.getCompanyEmail());
                        dataCell_7.setCellStyle(dataRowStyle);

                        Cell dataCell_8 = dataRow.createCell(7, CellType.STRING);
                        dataCell_8.setCellValue(simpleDateFormat.format(assignmentHistoryReport.getStartDate()));
                        dataCell_8.setCellStyle(dataRowStyle);

                        Cell dataCell_9 = dataRow.createCell(8, CellType.STRING);
                        dataCell_9.setCellValue(assignmentHistoryReport.getEndDate() != null ? simpleDateFormat.format(assignmentHistoryReport.getEndDate()) : "");
                        dataCell_9.setCellStyle(dataRowStyle);
                    });
                    fileName = temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "STAFF ASSIGNMENT HISTORY.xlsx";
                } else {
                    Sheet sheet = workbook.createSheet("STAFF ASSIGNMENT HISTORY BY OPERATION");
                    AtomicInteger pos = new AtomicInteger(0);
                    String operationId = String.valueOf(params.get("operationId"));
                    CommercialOperation operation = commercialOperationRepository.findBy_id(operationId);
                    FinancialContract customerContract = contractRepository.findByCommercialOperation(operation);
                    String name = "";
                    String value = "";
                    while (pos.get() < 4) {
                        switch (pos.get()) {
                            case 0: {
                                name = "Customer Code:";
                                value = customerContract.getClient().getCode();
                                break;
                            }
                            case 1: {
                                name = "Customer Name:";
                                value = customerContract.getClient().getName();
                                break;
                            }
                            case 2: {
                                name = "Operation Code:";
                                value = customerContract.getCommercialOperation().getCode();
                                break;
                            }
                            case 3: {
                                name = "Operation Name:";
                                value = customerContract.getCommercialOperation().getName();
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        Row temp = sheet.createRow(pos.get());
                        Cell nameCell = temp.createCell(0, CellType.STRING);
                        nameCell.setCellValue(name);
                        nameCell.setCellStyle(nameStyle);

                        Cell valueCell = temp.createCell(1);
                        valueCell.setCellValue(value);
                        pos.incrementAndGet();
                    }

                    Row headerRow = sheet.createRow(pos.get());
                    Cell cell_0 = headerRow.createCell(0, CellType.STRING);
                    cell_0.setCellValue("Employee Name");
                    cell_0.setCellStyle(tableHeaderStyle);

                    Cell cell_1 = headerRow.createCell(1, CellType.STRING);
                    cell_1.setCellValue("Employee Number");
                    cell_1.setCellStyle(tableHeaderStyle);

                    Cell cell_2 = headerRow.createCell(2, CellType.STRING);
                    cell_2.setCellValue("Company");
                    cell_2.setCellStyle(tableHeaderStyle);

                    Cell cell_3 = headerRow.createCell(3, CellType.STRING);
                    cell_3.setCellValue("Company Email");
                    cell_3.setCellStyle(tableHeaderStyle);

                    Cell cell_4 = headerRow.createCell(4, CellType.STRING);
                    cell_4.setCellValue("Start Date");
                    cell_4.setCellStyle(tableHeaderStyle);

                    Cell cell_6 = headerRow.createCell(5, CellType.STRING);
                    cell_6.setCellValue("End Date");
                    cell_6.setCellStyle(tableHeaderStyle);

                    HashMap data = new HashMap();
                    data.put("operationId", operationId);
                    List<StaffAssignmentDto> list = getStaffAssignedByOperation(data);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
                    list.forEach(staffAssignmentDto -> {
                        Row dataRow = sheet.createRow(pos.incrementAndGet());

                        Cell dataCell_0 = dataRow.createCell(0, CellType.STRING);
                        dataCell_0.setCellValue(staffAssignmentDto.getName() + " " + staffAssignmentDto.getFatherFamilyName() + " " + staffAssignmentDto.getMotherFamilyName());
                        dataCell_0.setCellStyle(dataRowStyle);

                        Cell dataCell_1 = dataRow.createCell(1, CellType.STRING);
                        dataCell_1.setCellValue(staffAssignmentDto.getPersonalNumber());
                        dataCell_1.setCellStyle(dataRowStyle);

                        Cell dataCell_2 = dataRow.createCell(2, CellType.STRING);
                        dataCell_2.setCellValue(staffAssignmentDto.getCompany());
                        dataCell_2.setCellStyle(dataRowStyle);

                        Cell dataCell_3 = dataRow.createCell(3, CellType.STRING);
                        dataCell_3.setCellValue(staffAssignmentDto.getCompanyEmail());
                        dataCell_3.setCellStyle(dataRowStyle);

                        Cell dataCell_4 = dataRow.createCell(4, CellType.STRING);
                        dataCell_4.setCellValue(simpleDateFormat.format(staffAssignmentDto.getStartDate()));
                        dataCell_4.setCellStyle(dataRowStyle);

                        Cell dataCell_5 = dataRow.createCell(5, CellType.STRING);
                        dataCell_5.setCellValue(staffAssignmentDto.getEndDate() != null ? simpleDateFormat.format(staffAssignmentDto.getEndDate()) : "");
                        dataCell_5.setCellStyle(dataRowStyle);
                    });
                    fileName = temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "STAFF ASSIGNMENT HISTORY BY OPERATION.xlsx";
                }
                File currDir = new File(".");
                String path = currDir.getAbsolutePath();
                String fileLocation = path.substring(0, path.length() - 1) + fileName;
                try {
                    FileOutputStream outputStream = new FileOutputStream(fileLocation);
                    workbook.write(outputStream);
                    workbook.close();
                } catch (FileNotFoundException e) {
                    generated = false;
                    e.printStackTrace();
                } catch (IOException e) {
                    generated = false;
                    e.printStackTrace();
                }
                break;
            }
            case "pdf": {
                if (option.equalsIgnoreCase("HISTORY")) {
                    List<AssignmentHistoryReport> list = getAssignmentHistoryReport();
                    Document document = new Document();
                    try {
                        PdfWriter.getInstance(document, new FileOutputStream(temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "STAFF ASSIGNMENT HISTORY.pdf"));
                        document.open();
                        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK);
                        Chunk title = new Chunk("STAFF ASSIGNMENT HISTORY", titleFont);
                        Paragraph p = new Paragraph();
                        p.add(title);
                        p.setAlignment(Element.ALIGN_CENTER);
                        document.add(p);

                        document.add(new Paragraph(" "));

                        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.BLACK);
                        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

                        PdfPTable table = new PdfPTable(9);
                        table.setWidthPercentage(100);
                        Stream.of("Cust. Code", "Customer Name", "Opt. Code", "Operation Name", "Employee Name", "Company", "Company Email", "Start Date", "End Date")
                                .forEach(columnTitle -> {
                                    PdfPCell header = new PdfPCell();
                                    header.setBackgroundColor(new BaseColor(130, 224, 170));
                                    header.setPhrase(new Phrase(columnTitle, boldFont));
                                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    header.setBorder(PdfPCell.NO_BORDER);
                                    table.addCell(header);
                                });

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
                        list.forEach(assignmentHistoryReport -> {
                            PdfPCell customerCode = new PdfPCell();
                            customerCode.setPhrase(new Phrase(assignmentHistoryReport.getCustomerCode(), dataFont));
                            customerCode.setBorder(PdfPCell.NO_BORDER);
                            customerCode.setHorizontalAlignment(Element.ALIGN_CENTER);
                            customerCode.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(customerCode);

                            PdfPCell customerName = new PdfPCell();
                            customerName.setPhrase(new Phrase(assignmentHistoryReport.getCustomerName(), dataFont));
                            customerName.setBorder(PdfPCell.NO_BORDER);
                            customerName.setHorizontalAlignment(Element.ALIGN_CENTER);
                            customerName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(customerName);

                            PdfPCell operationCode = new PdfPCell();
                            operationCode.setPhrase(new Phrase(assignmentHistoryReport.getOperationCode(), dataFont));
                            operationCode.setBorder(PdfPCell.NO_BORDER);
                            operationCode.setHorizontalAlignment(Element.ALIGN_CENTER);
                            operationCode.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(operationCode);

                            PdfPCell operationName = new PdfPCell();
                            operationName.setPhrase(new Phrase(assignmentHistoryReport.getOperationName(), dataFont));
                            operationName.setBorder(PdfPCell.NO_BORDER);
                            operationName.setHorizontalAlignment(Element.ALIGN_CENTER);
                            operationName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(operationName);

                            PdfPCell employeeName = new PdfPCell();
                            employeeName.setPhrase(new Phrase(assignmentHistoryReport.getName() + " " + assignmentHistoryReport.getFatherFamilyName() + " " + assignmentHistoryReport.getMotherFamilyName(), dataFont));
                            employeeName.setBorder(PdfPCell.NO_BORDER);
                            employeeName.setHorizontalAlignment(Element.ALIGN_CENTER);
                            employeeName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(employeeName);

                            PdfPCell company = new PdfPCell();
                            company.setPhrase(new Phrase(assignmentHistoryReport.getCompany(), dataFont));
                            company.setBorder(PdfPCell.NO_BORDER);
                            company.setHorizontalAlignment(Element.ALIGN_CENTER);
                            company.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(company);

                            PdfPCell companyEmail = new PdfPCell();
                            companyEmail.setPhrase(new Phrase(assignmentHistoryReport.getCompanyEmail(), dataFont));
                            companyEmail.setBorder(PdfPCell.NO_BORDER);
                            companyEmail.setHorizontalAlignment(Element.ALIGN_CENTER);
                            companyEmail.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(companyEmail);

                            PdfPCell startDate = new PdfPCell();
                            startDate.setPhrase(new Phrase(simpleDateFormat.format(assignmentHistoryReport.getStartDate()), dataFont));
                            startDate.setBorder(PdfPCell.NO_BORDER);
                            startDate.setHorizontalAlignment(Element.ALIGN_CENTER);
                            startDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(startDate);

                            PdfPCell endDate = new PdfPCell();
                            endDate.setPhrase(new Phrase(assignmentHistoryReport.getEndDate() != null ? simpleDateFormat.format(assignmentHistoryReport.getEndDate()) : "", dataFont));
                            endDate.setBorder(PdfPCell.NO_BORDER);
                            endDate.setHorizontalAlignment(Element.ALIGN_CENTER);
                            endDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(endDate);
                        });
                        table.setWidths(new int[]{2, 3, 2, 3, 4, 3, 4, 3, 3});
                        table.setSpacingBefore(10);
                        table.setSpacingAfter(10);
                        document.add(table);

                        document.close();
                    } catch (DocumentException e) {
                        generated = false;
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        generated = false;
                        e.printStackTrace();
                    }
                } else {
                    String operationId = String.valueOf(params.get("operationId"));
                    CommercialOperation operation = commercialOperationRepository.findBy_id(operationId);
                    FinancialContract customerContract = contractRepository.findByCommercialOperation(operation);

                    HashMap data = new HashMap();
                    data.put("operationId", operationId);
                    List<StaffAssignmentDto> list = getStaffAssignedByOperation(data);

                    Document document = new Document();
                    try {
                        PdfWriter.getInstance(document, new FileOutputStream(temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "STAFF ASSIGNMENT HISTORY BY OPERATION.pdf"));
                        document.open();
                        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK);
                        Chunk title = new Chunk("STAFF ASSIGNMENT HISTORY BY OPERATION", titleFont);
                        Paragraph p = new Paragraph();
                        p.add(title);
                        p.setAlignment(Element.ALIGN_CENTER);
                        document.add(p);

                        document.add(new Paragraph(" "));

                        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.BLACK);
                        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

                        Paragraph customerCode = new Paragraph();
                        customerCode.add(0, new Chunk("Customer Code: ", boldFont));
                        customerCode.add(1, new Chunk(customerContract.getClient().getCode(), dataFont));
                        document.add(customerCode);

                        Paragraph customerName = new Paragraph();
                        customerName.add(0, new Chunk("Customer Name: ", boldFont));
                        customerName.add(1, new Chunk(customerContract.getClient().getName(), dataFont));
                        document.add(customerName);

                        Paragraph operationCode = new Paragraph();
                        operationCode.add(0, new Chunk("Operation Code: ", boldFont));
                        operationCode.add(1, new Chunk(customerContract.getCommercialOperation().getCode(), dataFont));
                        document.add(operationCode);

                        Paragraph operationName = new Paragraph();
                        operationName.add(0, new Chunk("Operation Name: ", boldFont));
                        operationName.add(1, new Chunk(customerContract.getCommercialOperation().getName(), dataFont));
                        document.add(operationName);

                        PdfPTable table = new PdfPTable(6);
                        table.setWidthPercentage(100);
                        Stream.of("Employee Name", "Employee Number", "Company", "Company Email", "Start Date", "End Date")
                                .forEach(columnTitle -> {
                                    PdfPCell header = new PdfPCell();
                                    header.setBackgroundColor(new BaseColor(130, 224, 170));
                                    header.setPhrase(new Phrase(columnTitle, boldFont));
                                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    header.setBorder(PdfPCell.NO_BORDER);
                                    table.addCell(header);
                                });

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
                        list.forEach(staffAssignmentDto -> {
                            PdfPCell employeeName = new PdfPCell();
                            employeeName.setPhrase(new Phrase(staffAssignmentDto.getName() + " " + staffAssignmentDto.getFatherFamilyName() + " " + staffAssignmentDto.getMotherFamilyName(), dataFont));
                            employeeName.setBorder(PdfPCell.NO_BORDER);
                            employeeName.setHorizontalAlignment(Element.ALIGN_CENTER);
                            employeeName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(employeeName);

                            PdfPCell employeeNumber = new PdfPCell();
                            employeeNumber.setPhrase(new Phrase(staffAssignmentDto.getPersonalNumber(), dataFont));
                            employeeNumber.setBorder(PdfPCell.NO_BORDER);
                            employeeNumber.setHorizontalAlignment(Element.ALIGN_CENTER);
                            employeeNumber.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(employeeNumber);

                            PdfPCell company = new PdfPCell();
                            company.setPhrase(new Phrase(staffAssignmentDto.getCompany(), dataFont));
                            company.setBorder(PdfPCell.NO_BORDER);
                            company.setHorizontalAlignment(Element.ALIGN_CENTER);
                            company.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(company);

                            PdfPCell companyEmail = new PdfPCell();
                            companyEmail.setPhrase(new Phrase(staffAssignmentDto.getCompanyEmail(), dataFont));
                            companyEmail.setBorder(PdfPCell.NO_BORDER);
                            companyEmail.setHorizontalAlignment(Element.ALIGN_CENTER);
                            companyEmail.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(companyEmail);

                            PdfPCell startDate = new PdfPCell();
                            startDate.setPhrase(new Phrase(simpleDateFormat.format(staffAssignmentDto.getStartDate()), dataFont));
                            startDate.setBorder(PdfPCell.NO_BORDER);
                            startDate.setHorizontalAlignment(Element.ALIGN_CENTER);
                            startDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(startDate);

                            PdfPCell endDate = new PdfPCell();
                            endDate.setPhrase(new Phrase(staffAssignmentDto.getEndDate() != null ? simpleDateFormat.format(staffAssignmentDto.getEndDate()) : "", dataFont));
                            endDate.setBorder(PdfPCell.NO_BORDER);
                            endDate.setHorizontalAlignment(Element.ALIGN_CENTER);
                            endDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(endDate);
                        });

                        table.setWidths(new int[]{4, 2, 4, 4, 2, 2});
                        table.setSpacingBefore(10);
                        table.setSpacingAfter(10);
                        document.add(table);

                        document.close();
                    } catch (DocumentException e) {
                        generated = false;
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        generated = false;
                        e.printStackTrace();
                    }
                }
                break;
            }
            default: {
                break;
            }
        }
        return generated;
    }

    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.StaffAssignment, exceptionType, args);
    }

    private RuntimeException exceptionFile(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.File, exceptionType, args);
    }
}
