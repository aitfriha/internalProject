package org.techniu.isbackend.service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.Binary;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.techniu.isbackend.dto.mapper.JourneyMapper;
import org.techniu.isbackend.dto.mapper.TravelRequestDocumentMapper;
import org.techniu.isbackend.dto.mapper.TravelRequestMapper;
import org.techniu.isbackend.dto.mapper.VisitMapper;
import org.techniu.isbackend.dto.model.*;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.RequestStatusRepository;
import org.techniu.isbackend.repository.StaffRepository;
import org.techniu.isbackend.repository.TravelRequestRepository;
import org.techniu.isbackend.repository.TypeOfCurrencyRepository;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.FileSystems;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;
import static org.techniu.isbackend.exception.ExceptionType.NOT_GENERATED;
import static org.techniu.isbackend.service.utilities.Util.*;


@Service
public class TravelRequestServiceImpl implements TravelRequestService {

    private final String temporalDocumentsDirectory = "TEMPORAL DOCUMENTS";
    private TravelRequestRepository travelRequestRepository;
    private StaffRepository staffRepository;
    private BusinessExpensesTypesService businessExpensesTypesService;
    private RequestStatusRepository requestStatusRepository;
    private TypeOfCurrencyRepository currencyTypeRepository;

    private final TravelRequestMapper travelRequestMapper = Mappers.getMapper(TravelRequestMapper.class);
    private final JourneyMapper journeyMapper = Mappers.getMapper(JourneyMapper.class);
    private final VisitMapper visitMapper = Mappers.getMapper(VisitMapper.class);
    private final TravelRequestDocumentMapper docMapper = Mappers.getMapper(TravelRequestDocumentMapper.class);

    private EmailSenderService emailSenderService;
    private TravelRequestEmailAddressService travelRequestEmailAddressService;
    private CurrencyService currencyService;

    public TravelRequestServiceImpl(TravelRequestRepository travelRequestRepository, RequestStatusRepository requestStatusRepository,
                                    StaffRepository staffRepository,
                                    TypeOfCurrencyRepository currencyTypeRepository,
                                    EmailSenderService emailSenderService,
                                    BusinessExpensesTypesService businessExpensesTypesService,
                                    TravelRequestEmailAddressService travelRequestEmailAddressService,
                                    CurrencyService currencyService) {
        this.travelRequestRepository = travelRequestRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.staffRepository = staffRepository;
        this.businessExpensesTypesService = businessExpensesTypesService;
        this.currencyTypeRepository = currencyTypeRepository;
        this.emailSenderService = emailSenderService;
        this.travelRequestEmailAddressService = travelRequestEmailAddressService;
        this.currencyService = currencyService;
    }


    @Override
    public List<TravelRequestDto> getAllTravelRequests(HashMap data) {
        List<TravelRequestDto> result = new ArrayList<>();
        String companyEmail = data.containsKey("companyEmail") ? (String) data.get("companyEmail") : "";
        String period = (String) data.get("period");
        Date sDate = null;
        Date eDate = null;
        try {
            if (data.get("startDate") != null) {
                sDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(data.get("startDate")));
            }
            if (data.get("endDate") != null) {
                eDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(data.get("endDate")));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Staff requester = !companyEmail.isEmpty() ? staffRepository.findByCompanyEmail(companyEmail) : null;
        List<TravelRequest> travelRequestsList = requester != null ? travelRequestRepository.findAllByRequester(requester) : travelRequestRepository.findAll();

        Collections.sort(travelRequestsList, Comparator.comparing(TravelRequest::getRequestDate).reversed());

        for (TravelRequest travelRequest : travelRequestsList) {
            Date requestDate = travelRequest.getRequestDate();
            Calendar calendar = Calendar.getInstance();
            boolean proceed = false;
            switch (period) {
                case "month": {
                    calendar.setTime(requestDate);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int actualMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
                    proceed = month == actualMonth;
                    break;
                }
                case "year": {
                    calendar.setTime(requestDate);
                    int year = calendar.get(Calendar.YEAR);
                    int actualYear = Calendar.getInstance().get(Calendar.YEAR);
                    proceed = year == actualYear;
                    break;
                }
                default: {
                    if (sDate == null || eDate == null) {
                        if (sDate != null) {
                            proceed = sDate.compareTo(requestDate) < 0;
                        } else {
                            proceed = eDate.compareTo(requestDate) > 0;
                        }
                    } else {
                        proceed = (sDate.compareTo(requestDate) < 0) && (eDate.compareTo(requestDate) > 0);
                    }
                    break;
                }
            }
            if (proceed) {
                TravelRequestDto travelRequestDto = travelRequestMapper.modelToDto(travelRequest);
                List<JourneyDto> journeys = new ArrayList<>();

                List<Journey> orderedJourneys = travelRequest.getJourneys();
                Collections.sort(orderedJourneys, Comparator.comparing(a -> a.getOrder()));
                orderedJourneys.forEach(j -> {
                    JourneyDto journeyDto = journeyMapper.modelToDto(j);

                    BusinessExpenseSubtypeDto transportType = businessExpensesTypesService.getSubtypeBy("TRANSPORT", j.getTransportTypeId());
                    BusinessExpenseSubtypeDto lodgingType = businessExpensesTypesService.getSubtypeBy("LODGING", j.getLodgingTypeId());

                    journeyDto.setTransportTypeName(transportType.getName());
                    journeyDto.setLodgingTypeName(lodgingType.getName());

                    List<VisitDto> visits = new ArrayList<>();
                    j.getVisits().forEach(v -> {
                        VisitDto visitDto = visitMapper.modelToDto(v);
                        visits.add(visitDto);
                    });
                    journeyDto.setVisits(visits);
                    journeys.add(journeyDto);
                });

                List<TravelRequestDocumentDto> documents = new ArrayList<>();
                List<TravelRequestDocument> initialDocuments = travelRequest.getDocuments() != null ? travelRequest.getDocuments() : new ArrayList<>();
                initialDocuments.forEach(doc -> {
                    TravelRequestDocumentDto docDto = docMapper.modelToDto(doc);
                    documents.add(docDto);
                });

                Date firstDepartureDate = journeys.get(0).getDepartureDate();
                Date lastArrivalDate = journeys.get(journeys.size() - 1).getArrivalDate();
                int days = Long.valueOf(ChronoUnit.DAYS.between(asLocalDate(firstDepartureDate), asLocalDate(lastArrivalDate))).intValue();
                travelRequestDto.setJourneys(journeys);
                travelRequestDto.setDocuments(documents);
                travelRequestDto.setFirstDepartureDate(firstDepartureDate);
                travelRequestDto.setLastArrivalDate(lastArrivalDate);
                travelRequestDto.setDays(days);
                result.add(travelRequestDto);
            }
        }
        //DELETE ALL TEMPORAL DOCUMENTS
        deleteTemporalFilesOnServer();
        return result;
    }

    @Override
    public void saveTravelRequest(TravelRequestDto travelRequestDto) {
        TravelRequest travelRequest = travelRequestMapper.dtoToModel(travelRequestDto);

        SecureRandom random = new SecureRandom();
        String code = new BigInteger(32, random).toString(16).toUpperCase();
        travelRequest.setCode(code);

        Staff requester = staffRepository.findAllByStaffId(travelRequestDto.getRequesterId());
        travelRequest.setRequester(requester);

        List<Journey> journeyList = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);
        travelRequestDto.getJourneys().forEach(journeyDto -> {
            BusinessExpenseSubtypeDto transportType = businessExpensesTypesService.getSubtypeBy("TRANSPORT", journeyDto.getTransportTypeId());
            BusinessExpenseSubtypeDto lodgingType = businessExpensesTypesService.getSubtypeBy("LODGING", journeyDto.getLodgingTypeId());
            if (transportType.isRequiresApproval() || lodgingType.isRequiresApproval()) {
                count.getAndIncrement();
            }
            List<Visit> visitList = new ArrayList<>();
            journeyDto.getVisits().forEach(visitDto -> {
                Visit visit = visitMapper.dtoToModel(visitDto);
                visitList.add(visit);
            });
            Journey journey = journeyMapper.dtoToModel(journeyDto);
            journey.setVisits(visitList);

            journeyList.add(journey);
        });
        travelRequest.setJourneys(journeyList);

        String statusMasterValue = count.get() > 0 ? "PENDING APPROVAL" : "REQUESTED";
        RequestStatus requestStatus = requestStatusRepository.findRequestStatusByMasterValue(statusMasterValue);
        travelRequest.setStatus(requestStatus);

        travelRequestRepository.save(travelRequest);
        //Send emails
        sendEmail("ADD", travelRequest);
    }

    @Override
    public void updateTravelRequest(TravelRequestDto travelRequestDto) {
        // Find travel request by id
        Optional<TravelRequest> obj = travelRequestRepository.findById(travelRequestDto.getId());
        if (obj.isPresent()) {
            TravelRequest travelRequest = travelRequestMapper.dtoToModel(travelRequestDto);

            Staff requester = staffRepository.findAllByStaffId(travelRequestDto.getRequesterId());
            travelRequest.setRequester(requester);

            List<Journey> journeyList = new ArrayList<>();
            AtomicInteger count = new AtomicInteger(0);
            travelRequestDto.getJourneys().forEach(journeyDto -> {
                BusinessExpenseSubtypeDto transportType = businessExpensesTypesService.getSubtypeBy("TRANSPORT", journeyDto.getTransportTypeId());
                BusinessExpenseSubtypeDto lodgingType = businessExpensesTypesService.getSubtypeBy("LODGING", journeyDto.getLodgingTypeId());
                if (transportType.isRequiresApproval() || lodgingType.isRequiresApproval()) {
                    count.getAndIncrement();
                }
                List<Visit> visitList = new ArrayList<>();
                journeyDto.getVisits().forEach(visitDto -> {
                    Visit visit = visitMapper.dtoToModel(visitDto);
                    visitList.add(visit);
                });
                Journey journey = journeyMapper.dtoToModel(journeyDto);
                journey.setVisits(visitList);

                journeyList.add(journey);
            });
            travelRequest.setJourneys(journeyList);

            String statusMasterValue = count.get() > 0 ? "PENDING APPROVAL" : "REQUESTED";
            RequestStatus requestStatus = requestStatusRepository.findRequestStatusByMasterValue(statusMasterValue);
            travelRequest.setStatus(requestStatus);

            travelRequestRepository.save(travelRequest);

            //Send emails
            sendEmail("UPDATE", travelRequest);

        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void changeStatusTravelRequest(HashMap data) {
        String travelRequestId = String.valueOf(data.get("travelRequestId"));
        String requestStatusMasterValue = String.valueOf(data.get("requestStatusMasterValue"));
        Optional<TravelRequest> obj = travelRequestRepository.findById(travelRequestId);
        if (obj.isPresent()) {
            TravelRequest travelRequest = obj.get();
            RequestStatus requestStatus = requestStatusRepository.findRequestStatusByMasterValue(requestStatusMasterValue);
            travelRequest.setStatus(requestStatus);
            travelRequestRepository.save(travelRequest);
            //Send email
            sendEmail(travelRequest.getStatus().getMasterValue(), travelRequest);
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public File exportTravelRequests(HashMap data) {

        String companyEmail = data.containsKey("companyEmail") ? (String) data.get("companyEmail") : "";
        String fileType = String.valueOf(data.get("fileType"));

        String period = (String) data.get("period");
        Date sDate = null;
        Date eDate = null;
        try {
            if (data.get("startDate") != null) {
                sDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(data.get("startDate")));
            }
            if (data.get("endDate") != null) {
                eDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(data.get("endDate")));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Staff requester = !companyEmail.isEmpty() ? staffRepository.findByCompanyEmail(companyEmail) : null;

        List<TravelRequest> initialList = requester != null ? travelRequestRepository.findAllByRequester(requester) : travelRequestRepository.findAll();
        List<TravelRequest> travelRequestList = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String currentMonth = getMonthInLetter(Calendar.getInstance().get(Calendar.MONTH));
        String timePeriod = "";
        switch (period) {
            case "month": {
                timePeriod = currentMonth + ", " + currentYear;
                break;
            }
            case "year": {
                timePeriod = currentYear;
                break;
            }
            default: {
                if (sDate == null || eDate == null) {
                    if (sDate != null) {
                        timePeriod = "from " + dateFormat.format(sDate);

                    } else {
                        timePeriod = "until " + dateFormat.format(eDate);
                    }
                } else {
                    timePeriod = "from " + dateFormat.format(sDate) + " to " + dateFormat.format(eDate);
                }
                break;
            }
        }
        for (TravelRequest travelRequest : initialList) {
            Date requestDate = travelRequest.getRequestDate();
            Calendar calendar = Calendar.getInstance();
            boolean proceed = false;
            switch (period) {
                case "month": {
                    calendar.setTime(requestDate);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int actualMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
                    proceed = month == actualMonth;
                    break;
                }
                case "year": {
                    calendar.setTime(requestDate);
                    int year = calendar.get(Calendar.YEAR);
                    int actualYear = Calendar.getInstance().get(Calendar.YEAR);
                    proceed = year == actualYear;
                    break;
                }
                default: {
                    if (sDate == null || eDate == null) {
                        if (sDate != null) {
                            proceed = sDate.compareTo(requestDate) < 0;

                        } else {
                            proceed = eDate.compareTo(requestDate) > 0;
                        }
                    } else {
                        proceed = (sDate.compareTo(requestDate) < 0) && (eDate.compareTo(requestDate) > 0);
                    }
                    break;
                }
            }
            if (proceed) {
                travelRequestList.add(travelRequest);
            }
        }

        Collections.sort(travelRequestList, Comparator.comparing(TravelRequest::getRequestDate).reversed());
        String option = requester != null ? "BY REQUESTER" : "ALL";
        boolean generated = generateFile(fileType, travelRequestList, timePeriod, option);
        if (generated) {
            //return the file from server location
            String fileName =  temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "TRAVEL REQUESTS" + (fileType.equals("excel") ? ".xlsx" : ".pdf");
            return new File(fileName);
        } else {
            throw exceptionFile(NOT_GENERATED);
        }
    }

    @Override
    public byte[] downloadDocumentsOfTravelRequest(String travelRequestId) {
        TravelRequest travelRequest = travelRequestRepository.findTravelRequestBy_id(travelRequestId);

        ByteArrayOutputStream bos = null;
        ZipOutputStream zipOut = null;
        try {
            bos = new ByteArrayOutputStream();
            zipOut = new ZipOutputStream(bos);

            for(TravelRequestDocument doc : travelRequest.getDocuments()) {
                InputStream in = new ByteArrayInputStream(doc.getData().getData());
                ZipEntry zipEntry = new ZipEntry(doc.getName());
                try {
                    zipOut.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = in.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            zipOut.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    public void approveTravelRequest(HashMap data) {
        String travelRequestId = String.valueOf(data.get("travelRequestId"));
        String status = String.valueOf(data.get("status"));
        MultipartFile[] files = (MultipartFile[]) data.get("files");
        String[] localCurrencyTypes = (String[]) data.get("localCurrencyTypes");
        String[] localCurrencyAmounts = (String[]) data.get("localCurrencyAmounts");

        TravelRequest travelRequest = travelRequestRepository.findTravelRequestBy_id(travelRequestId);

        if (travelRequest != null) {
            RequestStatus requestStatus = requestStatusRepository.findRequestStatusByMasterValue(status);
            travelRequest.setStatus(requestStatus);

            Date today = Calendar.getInstance().getTime();
            TypeOfCurrency euroCurrencyType = currencyTypeRepository.findByCurrencyCode("EUR");
            List<TravelRequestDocument> documents = new ArrayList<>();
            try {
                for (int i = 0; i < files.length; i++) {

                    TravelRequestDocument doc = new TravelRequestDocument();
                    doc.setOrder(documents.size() + 1);
                    doc.setName(files[i].getOriginalFilename());
                    doc.setType(files[i].getContentType());
                    TypeOfCurrency localCurrencyType = currencyTypeRepository.findAllBy_id(localCurrencyTypes[i]);
                    doc.setLocalCurrencyType(localCurrencyType);
                    BigDecimal localAmount = new BigDecimal(localCurrencyAmounts[i]);
                    doc.setLocalCurrencyAmount(localAmount.doubleValue());
                    doc.setEuroCurrencyType(euroCurrencyType);
                    CurrencyDto currencyManagementDto = currencyService.getLastDataByCurrencyType(localCurrencyTypes[i]);
                    BigDecimal euroAmount = localAmount.multiply(new BigDecimal(currencyManagementDto.getChangeFactor())).setScale(2, RoundingMode.HALF_UP);
                    doc.setEuroAmount(euroAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
                    doc.setData(new Binary(files[i].getBytes()));
                    doc.setUploadDate(today);

                    documents.add(doc);
                }

                travelRequest.setDocuments(documents);
                travelRequestRepository.save(travelRequest);

                //Send email to the requester
                sendEmail("APPROVED", travelRequest);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public boolean existsTravelRequestsWithStatus(String statusId) {
        RequestStatus requestStatus = requestStatusRepository.findRequestStatusBy_id(statusId);
        List<TravelRequest> travelRequestList = travelRequestRepository.findAllByStatus(requestStatus);
        return !travelRequestList.isEmpty();
    }

    @Override
    public boolean existsTravelRequestsWithBusinessExpenseSubtype(HashMap data) {
        AtomicBoolean exists = new AtomicBoolean(false);
        String id = String.valueOf(data.get("id"));
        String type = String.valueOf(data.get("type"));
        List<TravelRequest> list = travelRequestRepository.findAll();
        switch (type) {
            case "TRANSPORT": {
                list.forEach(travelRequest -> {
                    travelRequest.getJourneys().forEach(journey -> {
                        if (journey.getTransportTypeId().equals(id)) {
                            exists.set(true);
                            return;
                        }
                    });
                });
                break;
            }
            case "LODGING": {
                list.forEach(travelRequest -> {
                    travelRequest.getJourneys().forEach(journey -> {
                        if (journey.getLodgingTypeId().equals(id)) {
                            exists.set(true);
                            return;
                        }
                    });
                });
                break;
            }
            default: {
                break;
            }
        }
        return exists.get();
    }
    //------------------------------------------------------------------------------------------------------------------

    private boolean sendEmail(String option, TravelRequest travelRequest) {
        //Send emails
        List<String> emailList = new ArrayList<>();
        if (!option.equalsIgnoreCase("APPROVED") && !option.equalsIgnoreCase("REJECTED")) {
            List<TravelRequestEmailAddressDto> emailAddresses = travelRequestEmailAddressService.getAllEmailAddresses();
            emailAddresses.forEach(obj -> {
                emailList.add(obj.getEmail());
            });
        }
        String[] to = (!option.equalsIgnoreCase("APPROVED") && !option.equalsIgnoreCase("REJECTED")) ? emailList.toArray(new String[emailList.size()]) : new String[]{travelRequest.getRequester().getCompanyEmail()};
        String subject = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        String message = "";
        switch (option) {
            case "APPROVED": {
                subject = "The travel request with code " + travelRequest.getCode() + " has been approved!!!";
                message = "<html><p>Your travel request with code <b>" + travelRequest.getCode() + "</b> has been approved.</p>" +
                        "<p>Please, check your travel request list in our platform and download the attached documents.</p></html>";
                break;
            }
            case "CANCELED": {
                subject = "The travel request with code " + travelRequest.getCode() + " has been canceled!!!";
                break;
            }
            case "REJECTED": {
                to = new String[]{travelRequest.getRequester().getCompanyEmail()};
                subject = "The travel request with code " + travelRequest.getCode() + " has been rejected!!!";
                break;
            }
            default: {
                subject = option.equalsIgnoreCase("ADD") ? "You have a new travel request - " + travelRequest.getCode() + " !!!" : "The travel request with code " + travelRequest.getCode() + " has been updated!!!";
                String color = travelRequest.getStatus().getMasterValue().equalsIgnoreCase("REQUESTED") ? "#F1C40F" : "#0A79DF";
                message = "<html><h2>TRAVEL REQUEST INFORMATION</h2>" +
                        "<style type=text/css>span{ background-color:" + color + "; color: #FFFFFF; padding: 5px; } </style> " +
                        "<p><b>Request Status: </b><span>" + travelRequest.getStatus().getName() + "</span></p>" +
                        "<p><b>Request Code: </b>" + travelRequest.getCode() + "</p>" +
                        "<p><b>Requester Full Name: </b>" + travelRequest.getRequester().getFullName() + "</p>" +
                        "<p><b>Requester Personal Number: </b>" + travelRequest.getRequester().getStaffContract().getPersonalNumber() + "</p>" +
                        "<p><b>Requester Company Email: </b>" + travelRequest.getRequester().getCompanyEmail() + "</p>" +
                        "<p><b>Requester Company: </b>" + travelRequest.getRequester().getStaffContract().getCompany().getName() + "</p>" +
                        "<br/>" +
                        "<h3>Journey List</h3>" +
                        "<table table-layout: fixed style=\"width:100%\">" +
                        "<tbody>" +
                        "<tr bgcolor=\"82E0AA\">" +
                        "<th>No</th>" +
                        "<th>From</th>" +
                        "<th>Departure Date</th>" +
                        "<th>To</th>" +
                        "<th>Arrival Date</th>" +
                        "<th>Transport Type</th>" +
                        "<th>Lodging Type</th>" +
                        "<th>Nearest address</th>" +
                        "</tr>";

                String visitMessage = "<br/>" +
                        "<h3>Customers and operations to visit</h3>" +
                        "<table table-layout: fixed style=\"width:100%\">" +
                        "<tbody>" +
                        "<tr bgcolor=\"82E0AA\">" +
                        "<th>Journey No.</th>" +
                        "<th>Customer Code</th>" +
                        "<th>Customer Name Date</th>" +
                        "<th>Operation Code</th>" +
                        "<th>Operation Name</th>" +
                        "</tr>";
                for (Journey journey : travelRequest.getJourneys()) {
                    message = message.concat("<tr>");
                    message = message.concat("<td style=\"text-align:center\">").concat(String.valueOf(journey.getOrder())).concat("</td>");
                    message = message.concat("<td style=\"text-align:center\">").concat(journey.getFromCountry().getCountryName() + " " + journey.getFromState().getStateName() + " " + journey.getFromCity().getCityName()).concat("</td>");
                    message = message.concat("<td style=\"text-align:center\">").concat(simpleDateFormat.format(journey.getDepartureDate())).concat("</td>");
                    message = message.concat("<td style=\"text-align:center\">").concat(journey.getToCountry().getCountryName() + " " + journey.getToState().getStateName() + " " + journey.getToCity().getCityName()).concat("</td>");
                    message = message.concat("<td style=\"text-align:center\">").concat(simpleDateFormat.format(journey.getArrivalDate())).concat("</td>");

                    BusinessExpenseSubtypeDto transportType = businessExpensesTypesService.getSubtypeBy("TRANSPORT", journey.getTransportTypeId());
                    BusinessExpenseSubtypeDto lodgingType = businessExpensesTypesService.getSubtypeBy("LODGING", journey.getLodgingTypeId());

                    message = message.concat("<td style=\"text-align:center\">").concat(transportType.getName()).concat("</td>");
                    message = message.concat("<td style=\"text-align:center\">").concat(lodgingType.getName()).concat("</td>");
                    message = message.concat("<td style=\"text-align:center\">").concat(journey.getAddress()).concat("</td>");
                    message = message.concat("</tr>");

                    for (Visit visit : journey.getVisits()) {
                        visitMessage = visitMessage.concat("<tr>");
                        visitMessage = visitMessage.concat("<td style=\"text-align:center\">").concat(String.valueOf(journey.getOrder())).concat("</td>");
                        visitMessage = visitMessage.concat("<td style=\"text-align:center\">").concat(visit.getCustomer().getCode()).concat("</td>");
                        visitMessage = visitMessage.concat("<td style=\"text-align:center\">").concat(visit.getCustomer().getName()).concat("</td>");
                        visitMessage = visitMessage.concat("<td style=\"text-align:center\">").concat(visit.getOperation().getCode()).concat("</td>");
                        visitMessage = visitMessage.concat("<td style=\"text-align:center\">").concat(visit.getOperation().getName()).concat("</td>");
                        visitMessage = visitMessage.concat("</tr>");
                    }
                }
                message = message.concat("</tbody>");
                message = message.concat("</table>");
                message = message.concat(visitMessage);
                message = message.concat("</html>");
                break;
            }
        }
        return emailSenderService.sendSimpleMessage(to, subject, message);
    }

    private boolean generateFile(String fileType, List<TravelRequest> travelRequestList, String timePeriod, String option) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        boolean generated = true;
        switch (fileType) {
            case "excel": {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("TRAVEL REQUESTS LIST (" + timePeriod + ")");

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

                CellStyle totalRowStyle = workbook.createCellStyle();
                totalRowStyle.setAlignment(HorizontalAlignment.CENTER);
                totalRowStyle.setFont(nameFont);

                AtomicInteger pos = new AtomicInteger(0);
                AtomicInteger lastPos = new AtomicInteger(0);
                travelRequestList.forEach(travelRequest -> {
                    int count = 0;
                    while (count < 6) {
                        lastPos.set(pos.get() + count);
                        Row temp = sheet.createRow(lastPos.get());
                        String name = "";
                        String value = "";
                        switch (count) {
                            case 0: {
                                name = "Status:";
                                value = travelRequest.getStatus().getName();
                                break;
                            }
                            case 1: {
                                name = "Code:";
                                value = travelRequest.getCode();
                                break;
                            }
                            case 2: {
                                name = "Request Date:";
                                value = simpleDateFormat.format(travelRequest.getRequestDate());
                                break;
                            }
                            case 3: {
                                name = "Requester Name:";
                                value = travelRequest.getRequester().getFullName();
                                break;
                            }
                            case 4: {
                                name = "Company Email:";
                                value = travelRequest.getRequester().getCompanyEmail();
                                break;
                            }
                            case 5: {
                                name = "Company Name:";
                                value = travelRequest.getRequester().getStaffContract().getCompany().getName();
                                break;
                            }
                        }
                        Cell nameCell = temp.createCell(0, CellType.STRING);
                        nameCell.setCellValue(name);
                        nameCell.setCellStyle(nameStyle);

                        Cell valueCell = temp.createCell(1);
                        valueCell.setCellValue(value);

                        count++;
                    }
                    lastPos.getAndIncrement();

                    Row headerRow = sheet.createRow(lastPos.get());
                    Cell cell_0 = headerRow.createCell(0, CellType.STRING);
                    cell_0.setCellValue("No.");
                    cell_0.setCellStyle(tableHeaderStyle);

                    Cell cell_1 = headerRow.createCell(1, CellType.STRING);
                    cell_1.setCellValue("From");
                    cell_1.setCellStyle(tableHeaderStyle);

                    Cell cell_2 = headerRow.createCell(2, CellType.STRING);
                    cell_2.setCellValue("Departure Date");
                    cell_2.setCellStyle(tableHeaderStyle);

                    Cell cell_3 = headerRow.createCell(3, CellType.STRING);
                    cell_3.setCellValue("To");
                    cell_3.setCellStyle(tableHeaderStyle);

                    Cell cell_4 = headerRow.createCell(4, CellType.STRING);
                    cell_4.setCellValue("Arrival Date");
                    cell_4.setCellStyle(tableHeaderStyle);

                    Cell cell_5 = headerRow.createCell(5, CellType.STRING);
                    cell_5.setCellValue("Transport Type");
                    cell_5.setCellStyle(tableHeaderStyle);

                    Cell cell_6 = headerRow.createCell(6, CellType.STRING);
                    cell_6.setCellValue("Lodging Type");
                    cell_6.setCellStyle(tableHeaderStyle);

                    Cell cell_7 = headerRow.createCell(7, CellType.STRING);
                    cell_7.setCellValue("Nearest Address");
                    cell_7.setCellStyle(tableHeaderStyle);

                    List<HashMap> visits = new ArrayList<>();
                    travelRequest.getJourneys().forEach(journey -> {
                        Row dataRow = sheet.createRow(lastPos.incrementAndGet());
                        Cell dataCell_0 = dataRow.createCell(0, CellType.STRING);
                        dataCell_0.setCellValue(String.valueOf(journey.getOrder()));
                        dataCell_0.setCellStyle(dataRowStyle);

                        Cell dataCell_1 = dataRow.createCell(1, CellType.STRING);
                        dataCell_1.setCellValue(journey.getFromCity().getCityName() + " " + journey.getFromState().getStateName() + " " + journey.getFromCountry().getCountryName());
                        dataCell_1.setCellStyle(dataRowStyle);

                        Cell dataCell_2 = dataRow.createCell(2, CellType.STRING);
                        dataCell_2.setCellValue(simpleDateFormat.format(journey.getDepartureDate()));
                        dataCell_2.setCellStyle(dataRowStyle);

                        Cell dataCell_3 = dataRow.createCell(3, CellType.STRING);
                        dataCell_3.setCellValue(journey.getToCity().getCityName() + " " + journey.getToState().getStateName() + " " + journey.getToCountry().getCountryName());
                        dataCell_3.setCellStyle(dataRowStyle);

                        Cell dataCell_4 = dataRow.createCell(4, CellType.STRING);
                        dataCell_4.setCellValue(simpleDateFormat.format(journey.getArrivalDate()));
                        dataCell_4.setCellStyle(dataRowStyle);

                        BusinessExpenseSubtypeDto transportType = businessExpensesTypesService.getSubtypeBy("TRANSPORT", journey.getTransportTypeId());
                        BusinessExpenseSubtypeDto lodgingType = businessExpensesTypesService.getSubtypeBy("LODGING", journey.getLodgingTypeId());

                        Cell dataCell_5 = dataRow.createCell(5, CellType.STRING);
                        dataCell_5.setCellValue(transportType.getName());
                        dataCell_5.setCellStyle(dataRowStyle);

                        Cell dataCell_6 = dataRow.createCell(6, CellType.STRING);
                        dataCell_6.setCellValue(lodgingType.getName());
                        dataCell_6.setCellStyle(dataRowStyle);

                        Cell dataCell_7 = dataRow.createCell(7, CellType.STRING);
                        dataCell_7.setCellValue(journey.getAddress());
                        dataCell_7.setCellStyle(dataRowStyle);

                        HashMap visitData = new HashMap();
                        visitData.put("journey", journey.getOrder());
                        visitData.put("visits", journey.getVisits());

                        visits.add(visitData);

                    });
                    Row emptyRow = sheet.createRow(lastPos.incrementAndGet());
                    emptyRow.createCell(0, CellType.BLANK);

                    Row visitHeaderRow = sheet.createRow(lastPos.incrementAndGet());
                    Cell visitCell_0 = visitHeaderRow.createCell(0, CellType.STRING);
                    visitCell_0.setCellValue("Journey No.");
                    visitCell_0.setCellStyle(tableHeaderStyle);

                    Cell visitCell_1 = visitHeaderRow.createCell(1, CellType.STRING);
                    visitCell_1.setCellValue("Customer Code");
                    visitCell_1.setCellStyle(tableHeaderStyle);

                    Cell visitCell_2 = visitHeaderRow.createCell(2, CellType.STRING);
                    visitCell_2.setCellValue("Customer Name");
                    visitCell_2.setCellStyle(tableHeaderStyle);

                    Cell visitCell_3 = visitHeaderRow.createCell(3, CellType.STRING);
                    visitCell_3.setCellValue("Operation Code");
                    visitCell_3.setCellStyle(tableHeaderStyle);

                    Cell visitCell_4 = visitHeaderRow.createCell(4, CellType.STRING);
                    visitCell_4.setCellValue("Operation Name");
                    visitCell_4.setCellStyle(tableHeaderStyle);

                    visits.forEach(visitObject -> {
                        String order = String.valueOf(visitObject.get("journey"));
                        ((List<Visit>) visitObject.get("visits")).forEach(visit -> {

                            Row dataRow = sheet.createRow(lastPos.incrementAndGet());
                            Cell dataCell_0 = dataRow.createCell(0, CellType.STRING);
                            dataCell_0.setCellValue(order);
                            dataCell_0.setCellStyle(dataRowStyle);

                            Cell dataCell_1 = dataRow.createCell(1, CellType.STRING);
                            dataCell_1.setCellValue(visit.getCustomer().getCode());
                            dataCell_1.setCellStyle(dataRowStyle);

                            Cell dataCell_2 = dataRow.createCell(2, CellType.STRING);
                            dataCell_2.setCellValue(visit.getCustomer().getName());
                            dataCell_2.setCellStyle(dataRowStyle);

                            Cell dataCell_3 = dataRow.createCell(3, CellType.STRING);
                            dataCell_3.setCellValue(visit.getOperation().getCode());
                            dataCell_3.setCellStyle(dataRowStyle);

                            Cell dataCell_4 = dataRow.createCell(4, CellType.STRING);
                            dataCell_4.setCellValue(visit.getOperation().getName());
                            dataCell_4.setCellStyle(dataRowStyle);
                        });
                    });

                    if (option.equalsIgnoreCase("ALL")) {
                        emptyRow = sheet.createRow(lastPos.incrementAndGet());
                        emptyRow.createCell(0, CellType.BLANK);

                        Row documentHeaderRow = sheet.createRow(lastPos.incrementAndGet());
                        Cell documentCell_0 = documentHeaderRow.createCell(0, CellType.STRING);
                        documentCell_0.setCellValue("No.");
                        documentCell_0.setCellStyle(tableHeaderStyle);

                        Cell documentCell_1 = documentHeaderRow.createCell(1, CellType.STRING);
                        documentCell_1.setCellValue("File Name");
                        documentCell_1.setCellStyle(tableHeaderStyle);

                        Cell documentCell_2 = documentHeaderRow.createCell(2, CellType.STRING);
                        documentCell_2.setCellValue("Upload Date");
                        documentCell_2.setCellStyle(tableHeaderStyle);

                        Cell documentCell_3 = documentHeaderRow.createCell(3, CellType.STRING);
                        documentCell_3.setCellValue("Local Currency Name");
                        documentCell_3.setCellStyle(tableHeaderStyle);

                        Cell documentCell_4 = documentHeaderRow.createCell(4, CellType.STRING);
                        documentCell_4.setCellValue("Local Currency Amount");
                        documentCell_4.setCellStyle(tableHeaderStyle);

                        Cell documentCell_5 = documentHeaderRow.createCell(5, CellType.STRING);
                        documentCell_5.setCellValue("Euro Amount");
                        documentCell_5.setCellStyle(tableHeaderStyle);

                        double totalEuroAmount = 0;
                        for(TravelRequestDocument documentObject : travelRequest.getDocuments()) {
                            Row dataRow = sheet.createRow(lastPos.incrementAndGet());
                            Cell dataCell_0 = dataRow.createCell(0, CellType.STRING);
                            dataCell_0.setCellValue(documentObject.getOrder());
                            dataCell_0.setCellStyle(dataRowStyle);

                            Cell dataCell_1 = dataRow.createCell(1, CellType.STRING);
                            dataCell_1.setCellValue(documentObject.getName());
                            dataCell_1.setCellStyle(dataRowStyle);

                            Cell dataCell_2 = dataRow.createCell(2, CellType.STRING);
                            dataCell_2.setCellValue(simpleDateFormat.format(documentObject.getUploadDate()));
                            dataCell_2.setCellStyle(dataRowStyle);

                            Cell dataCell_3 = dataRow.createCell(3, CellType.STRING);
                            dataCell_3.setCellValue(documentObject.getLocalCurrencyType().getCurrencyName());
                            dataCell_3.setCellStyle(dataRowStyle);

                            Cell dataCell_4 = dataRow.createCell(4, CellType.STRING);
                            dataCell_4.setCellValue(decimalFormat.format(documentObject.getLocalCurrencyAmount()));
                            dataCell_4.setCellStyle(dataRowStyle);

                            Cell dataCell_5 = dataRow.createCell(5, CellType.STRING);
                            dataCell_5.setCellValue(decimalFormat.format(documentObject.getEuroAmount()));
                            dataCell_5.setCellStyle(dataRowStyle);

                            totalEuroAmount += documentObject.getEuroAmount();
                        }

                        Row dataRow = sheet.createRow(lastPos.incrementAndGet());

                        Cell totalCell = dataRow.createCell(4, CellType.STRING);
                        totalCell.setCellValue("Total");
                        totalCell.setCellStyle(totalRowStyle);

                        Cell totalValueCell = dataRow.createCell(5, CellType.NUMERIC);
                        totalValueCell.setCellValue(decimalFormat.format(totalEuroAmount));
                        totalValueCell.setCellStyle(totalRowStyle);
                    }

                    emptyRow = sheet.createRow(lastPos.incrementAndGet());
                    emptyRow.setRowStyle(emptyRowStyle);

                    pos.set(lastPos.incrementAndGet());
                });

                String fileName = temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "TRAVEL REQUESTS.xlsx";

                try {
                    FileOutputStream outputStream = new FileOutputStream(fileName);
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
                Document document = new Document();
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "TRAVEL REQUESTS.pdf"));
                    document.open();
                    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK);
                    Chunk title = new Chunk("TRAVEL REQUESTS LIST", titleFont);
                    Paragraph p = new Paragraph();
                    p.add(title);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);

                    Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
                    Chunk subtitle = new Chunk("(" + timePeriod + ")", subtitleFont);
                    p = new Paragraph();
                    p.add(subtitle);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);

                    document.add(new Paragraph(" "));

                    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.BLACK);
                    Font dataWhiteFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
                    Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

                    document.add(new Paragraph());
                    travelRequestList.forEach(travelRequest -> {
                        Paragraph status = new Paragraph();
                        status.add(0, new Chunk("Status: ", boldFont));
                        String statusName = travelRequest.getStatus().getName();
                        Chunk statusData = new Chunk(statusName, dataWhiteFont);
                        switch (travelRequest.getStatus().getMasterValue()) {
                            case "REQUESTED": {
                                statusData.setBackground(new BaseColor(241, 196, 15), 2, 2, 2, 4);
                                break;
                            }
                            case "PENDING APPROVAL": {
                                statusData.setBackground(new BaseColor(10, 121, 223), 2, 2, 2, 4);
                                break;
                            }
                            case "APPROVED": {
                                statusData.setBackground(new BaseColor(39, 174, 96), 2, 2, 2, 4);
                                break;
                            }
                            default: {
                                statusData.setBackground(new BaseColor(203, 67, 53), 2, 2, 2, 4);
                                break;
                            }
                        }
                        status.add(1, statusData);

                        Paragraph code = new Paragraph();
                        code.add(0, new Chunk("Code: ", boldFont));
                        code.add(1, new Chunk(travelRequest.getCode(), dataFont));

                        Paragraph requestDate = new Paragraph();
                        requestDate.add(0, new Chunk("Request Date: ", boldFont));
                        requestDate.add(1, new Chunk(simpleDateFormat.format(travelRequest.getRequestDate()), dataFont));

                        Paragraph requester = new Paragraph();
                        requester.add(0, new Chunk("Requester Name: ", boldFont));
                        requester.add(1, new Chunk(travelRequest.getRequester().getFullName(), dataFont));

                        Paragraph companyEmail = new Paragraph();
                        companyEmail.add(0, new Chunk("Company Email: ", boldFont));
                        companyEmail.add(1, new Chunk(travelRequest.getRequester().getCompanyEmail(), dataFont));

                        Paragraph company = new Paragraph();
                        company.add(0, new Chunk("Company Name: ", boldFont));
                        company.add(1, new Chunk(travelRequest.getRequester().getStaffContract().getCompany().getName(), dataFont));
                        try {
                            document.add(status);
                            document.add(code);
                            document.add(requestDate);
                            document.add(requester);
                            document.add(companyEmail);
                            document.add(company);

                            PdfPTable journeyTable = new PdfPTable(8);
                            journeyTable.setWidthPercentage(100);
                            Stream.of("No.", "From", "Departure Date", "To", "Arrival Date", "Transport Type", "Lodging Type", "Nearest Address")
                                    .forEach(columnTitle -> {
                                        PdfPCell header = new PdfPCell();
                                        header.setBackgroundColor(new BaseColor(130, 224, 170));
                                        header.setPhrase(new Phrase(columnTitle, boldFont));
                                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        header.setBorder(PdfPCell.NO_BORDER);
                                        journeyTable.addCell(header);
                                    });

                            List<HashMap> visits = new ArrayList<>();
                            travelRequest.getJourneys().forEach(journey -> {
                                PdfPCell numberData = new PdfPCell();
                                numberData.setPhrase(new Phrase(String.valueOf(journey.getOrder()), dataFont));
                                numberData.setBorder(PdfPCell.NO_BORDER);
                                numberData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                numberData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                journeyTable.addCell(numberData);

                                PdfPCell fromData = new PdfPCell();
                                fromData.setPhrase(new Phrase(journey.getFromCity().getCityName() + " " + journey.getFromState().getStateName() + " " + journey.getFromCountry().getCountryName(), dataFont));
                                fromData.setBorder(PdfPCell.NO_BORDER);
                                fromData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                fromData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                journeyTable.addCell(fromData);

                                PdfPCell departureDateData = new PdfPCell();
                                departureDateData.setPhrase(new Phrase(simpleDateFormat.format(journey.getDepartureDate()), dataFont));
                                departureDateData.setBorder(PdfPCell.NO_BORDER);
                                departureDateData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                departureDateData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                journeyTable.addCell(departureDateData);

                                PdfPCell toData = new PdfPCell();
                                toData.setPhrase(new Phrase(journey.getToCity().getCityName() + " " + journey.getToState().getStateName() + " " + journey.getToCountry().getCountryName(), dataFont));
                                toData.setBorder(PdfPCell.NO_BORDER);
                                toData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                toData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                journeyTable.addCell(toData);

                                PdfPCell arrivalDateData = new PdfPCell();
                                arrivalDateData.setPhrase(new Phrase(simpleDateFormat.format(journey.getArrivalDate()), dataFont));
                                arrivalDateData.setBorder(PdfPCell.NO_BORDER);
                                arrivalDateData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                arrivalDateData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                journeyTable.addCell(arrivalDateData);

                                BusinessExpenseSubtypeDto transportType = businessExpensesTypesService.getSubtypeBy("TRANSPORT", journey.getTransportTypeId());
                                BusinessExpenseSubtypeDto lodgingType = businessExpensesTypesService.getSubtypeBy("LODGING", journey.getLodgingTypeId());

                                PdfPCell transportTypeData = new PdfPCell();
                                transportTypeData.setPhrase(new Phrase(transportType.getName(), dataFont));
                                transportTypeData.setBorder(PdfPCell.NO_BORDER);
                                transportTypeData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                transportTypeData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                journeyTable.addCell(transportTypeData);

                                PdfPCell lodgingTypeData = new PdfPCell();
                                lodgingTypeData.setPhrase(new Phrase(lodgingType.getName(), dataFont));
                                lodgingTypeData.setBorder(PdfPCell.NO_BORDER);
                                lodgingTypeData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                lodgingTypeData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                journeyTable.addCell(lodgingTypeData);

                                PdfPCell addressData = new PdfPCell();
                                addressData.setPhrase(new Phrase(journey.getAddress(), dataFont));
                                addressData.setBorder(PdfPCell.NO_BORDER);
                                addressData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                addressData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                journeyTable.addCell(addressData);

                                HashMap visitHashMap = new HashMap();
                                visitHashMap.put("journey", journey.getOrder());
                                visitHashMap.put("visits", journey.getVisits());

                                visits.add(visitHashMap);
                            });

                            journeyTable.setWidths(new int[]{1, 4, 3, 4, 3, 3, 3, 4});
                            journeyTable.setSpacingBefore(10);
                            journeyTable.setSpacingAfter(10);
                            document.add(journeyTable);

                            PdfPTable visitsTable = new PdfPTable(5);
                            visitsTable.setWidthPercentage(100);
                            Stream.of("Journey No.", "Customer Code", "Customer Name", "Operation Code", "Operation Name")
                                    .forEach(columnTitle -> {
                                        PdfPCell header = new PdfPCell();
                                        header.setBackgroundColor(new BaseColor(130, 224, 170));
                                        header.setPhrase(new Phrase(columnTitle, boldFont));
                                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        header.setBorder(PdfPCell.NO_BORDER);
                                        visitsTable.addCell(header);
                                    });

                            visits.forEach(visitObject -> {
                                String order = String.valueOf(visitObject.get("journey"));
                                ((List<Visit>) visitObject.get("visits")).forEach(visit -> {
                                    PdfPCell numberData = new PdfPCell();
                                    numberData.setPhrase(new Phrase(order, dataFont));
                                    numberData.setBorder(PdfPCell.NO_BORDER);
                                    numberData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    numberData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    visitsTable.addCell(numberData);

                                    PdfPCell fromData = new PdfPCell();
                                    fromData.setPhrase(new Phrase(visit.getCustomer().getCode(), dataFont));
                                    fromData.setBorder(PdfPCell.NO_BORDER);
                                    fromData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    fromData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    visitsTable.addCell(fromData);

                                    PdfPCell departureDateData = new PdfPCell();
                                    departureDateData.setPhrase(new Phrase(visit.getCustomer().getName(), dataFont));
                                    departureDateData.setBorder(PdfPCell.NO_BORDER);
                                    departureDateData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    departureDateData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    visitsTable.addCell(departureDateData);

                                    PdfPCell toData = new PdfPCell();
                                    toData.setPhrase(new Phrase(visit.getOperation().getCode(), dataFont));
                                    toData.setBorder(PdfPCell.NO_BORDER);
                                    toData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    toData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    visitsTable.addCell(toData);

                                    PdfPCell arrivalDateData = new PdfPCell();
                                    arrivalDateData.setPhrase(new Phrase(visit.getOperation().getName(), dataFont));
                                    arrivalDateData.setBorder(PdfPCell.NO_BORDER);
                                    arrivalDateData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    arrivalDateData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    visitsTable.addCell(arrivalDateData);
                                });
                            });

                            visitsTable.setWidths(new int[]{2, 2, 4, 2, 4});
                            visitsTable.setSpacingAfter(10);
                            document.add(visitsTable);

                            if (option.equalsIgnoreCase("ALL") && (travelRequest.getDocuments() != null && !travelRequest.getDocuments().isEmpty())) {
                                PdfPTable documentsTable = new PdfPTable(6);
                                documentsTable.setWidthPercentage(100);
                                Stream.of("No.", "File Name", "Upload Date", "Local Currency Name", "Local Currency Amount", "Euro Amount")
                                        .forEach(columnTitle -> {
                                            PdfPCell header = new PdfPCell();
                                            header.setBackgroundColor(new BaseColor(130, 224, 170));
                                            header.setPhrase(new Phrase(columnTitle, boldFont));
                                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                            header.setBorder(PdfPCell.NO_BORDER);
                                            documentsTable.addCell(header);
                                        });

                                double totalEuroAmount = 0;
                                for (int i = 0; i <= travelRequest.getDocuments().size(); i++) {
                                    if (i < travelRequest.getDocuments().size()) {
                                        TravelRequestDocument doc = travelRequest.getDocuments().get(i);

                                        PdfPCell numberData = new PdfPCell();
                                        numberData.setPhrase(new Phrase(String.valueOf(doc.getOrder()), dataFont));
                                        numberData.setBorder(PdfPCell.NO_BORDER);
                                        numberData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        numberData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        documentsTable.addCell(numberData);

                                        PdfPCell nameData = new PdfPCell();
                                        nameData.setPhrase(new Phrase(doc.getName(), dataFont));
                                        nameData.setBorder(PdfPCell.NO_BORDER);
                                        nameData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        nameData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        documentsTable.addCell(nameData);

                                        PdfPCell uploadDateData = new PdfPCell();
                                        uploadDateData.setPhrase(new Phrase(simpleDateFormat.format(doc.getUploadDate()), dataFont));
                                        uploadDateData.setBorder(PdfPCell.NO_BORDER);
                                        uploadDateData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        uploadDateData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        documentsTable.addCell(uploadDateData);

                                        PdfPCell localCurrencyNameData = new PdfPCell();
                                        localCurrencyNameData.setPhrase(new Phrase(doc.getLocalCurrencyType().getCurrencyName(), dataFont));
                                        localCurrencyNameData.setBorder(PdfPCell.NO_BORDER);
                                        localCurrencyNameData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        localCurrencyNameData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        documentsTable.addCell(localCurrencyNameData);

                                        PdfPCell localCurrencyAmountData = new PdfPCell();
                                        localCurrencyAmountData.setPhrase(new Phrase(decimalFormat.format(doc.getLocalCurrencyAmount()), dataFont));
                                        localCurrencyAmountData.setBorder(PdfPCell.NO_BORDER);
                                        localCurrencyAmountData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        localCurrencyAmountData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        documentsTable.addCell(localCurrencyAmountData);

                                        PdfPCell euroAmountData = new PdfPCell();
                                        euroAmountData.setPhrase(new Phrase(decimalFormat.format(doc.getEuroAmount()), dataFont));
                                        euroAmountData.setBorder(PdfPCell.NO_BORDER);
                                        euroAmountData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        euroAmountData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        documentsTable.addCell(euroAmountData);

                                        totalEuroAmount += doc.getEuroAmount();
                                    } else {
                                        int count = 0;
                                        while (count < 4) {
                                            PdfPCell emptyData = new PdfPCell();
                                            emptyData.setPhrase(new Phrase("", dataFont));
                                            emptyData.setBorder(PdfPCell.NO_BORDER);
                                            emptyData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            emptyData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                            documentsTable.addCell(emptyData);
                                            count++;
                                        }
                                        PdfPCell totalName = new PdfPCell();
                                        totalName.setPhrase(new Phrase("Total", boldFont));
                                        totalName.setBorder(PdfPCell.NO_BORDER);
                                        totalName.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        totalName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        documentsTable.addCell(totalName);

                                        PdfPCell totalData = new PdfPCell();
                                        totalData.setPhrase(new Phrase(decimalFormat.format(totalEuroAmount), boldFont));
                                        totalData.setBorder(PdfPCell.NO_BORDER);
                                        totalData.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        totalData.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        documentsTable.addCell(totalData);
                                    }
                                }

                                documentsTable.setWidths(new int[]{2, 5, 2, 3, 3, 3});
                                documentsTable.setSpacingAfter(10);
                                document.add(documentsTable);
                            }

                            document.add(new DottedLineSeparator());
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    });
                    document.close();
                } catch (DocumentException e) {
                    generated = false;
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    generated = false;
                    e.printStackTrace();
                }
                break;
            }
            default: {
                break;
            }
        }
        return generated;
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.TravelRequest, exceptionType, args);
    }

    private RuntimeException exceptionFile(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.File, exceptionType, args);
    }

}
