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
import org.techniu.isbackend.dto.mapper.ExpenseDocumentMapper;
import org.techniu.isbackend.dto.mapper.ExpenseMapper;
import org.techniu.isbackend.dto.mapper.PersonMapper;
import org.techniu.isbackend.dto.model.*;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.FileSystems;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;
import static org.techniu.isbackend.exception.ExceptionType.NOT_GENERATED;
import static org.techniu.isbackend.service.utilities.Util.deleteTemporalFilesOnServer;
import static org.techniu.isbackend.service.utilities.Util.getMonthInLetter;


@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final String temporalDocumentsDirectory = "TEMPORAL DOCUMENTS";
    private ExpenseStatusRepository expenseStatusRepository;
    private ExpenseRepository expenseRepository;
    private StaffRepository staffRepository;
    private TypeOfCurrencyRepository currencyTypeRepository;
    private VoucherTypeRepository voucherTypeRepository;
    private StaffExpenseTypeRepository staffExpenseTypeRepository;
    private PersonTypeRepository personTypeRepository;
    private CurrencyService currencyService;
    private StaffExpensesTypesService staffExpensesTypesService;

    private CountryRepository countryRepository;
    private StateCountryRepository stateCountryRepository;
    private CityRepository cityRepository;

    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);
    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    private final ExpenseDocumentMapper docMapper = Mappers.getMapper(ExpenseDocumentMapper.class);

    public ExpenseServiceImpl(ExpenseStatusRepository expenseStatusRepository, ExpenseRepository expenseRepository,
                              StaffRepository staffRepository,
                              TypeOfCurrencyRepository currencyTypeRepository,
                              VoucherTypeRepository voucherTypeRepository,
                              StaffExpenseTypeRepository staffExpenseTypeRepository,
                              PersonTypeRepository personTypeRepository,
                              CurrencyService currencyService,
                              StaffExpensesTypesService staffExpensesTypesService,
                              CountryRepository countryRepository,
                              StateCountryRepository stateCountryRepository,
                              CityRepository cityRepository) {
        this.expenseStatusRepository = expenseStatusRepository;
        this.expenseRepository = expenseRepository;
        this.staffRepository = staffRepository;
        this.currencyTypeRepository = currencyTypeRepository;
        this.voucherTypeRepository = voucherTypeRepository;
        this.staffExpenseTypeRepository = staffExpenseTypeRepository;
        this.personTypeRepository = personTypeRepository;
        this.currencyService = currencyService;
        this.staffExpensesTypesService = staffExpensesTypesService;

        this.countryRepository = countryRepository;
        this.stateCountryRepository = stateCountryRepository;
        this.cityRepository = cityRepository;
    }


    @Override
    public List<ExpenseDto> getAllExpenses(HashMap data) {
        List<ExpenseDto> result = new ArrayList<>();
        String employeeId = data.containsKey("employeeId") ? (String) data.get("employeeId") : "";
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
        Staff employee = !employeeId.isEmpty() ? staffRepository.findAllByStaffId(employeeId) : null;
        List<Expense> expensesList = employee != null ? expenseRepository.findAllByStaff(employee) : expenseRepository.findAll();

        Collections.sort(expensesList, Comparator.comparing(Expense::getExpenseDate).reversed());

        for (Expense expense : expensesList) {
            Date expenseDate = expense.getExpenseDate();
            Calendar calendar = Calendar.getInstance();
            boolean proceed = false;
            switch (period) {
                case "month": {
                    calendar.setTime(expenseDate);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int actualMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
                    proceed = month == actualMonth;
                    break;
                }
                case "year": {
                    calendar.setTime(expenseDate);
                    int year = calendar.get(Calendar.YEAR);
                    int actualYear = Calendar.getInstance().get(Calendar.YEAR);
                    proceed = year == actualYear;
                    break;
                }
                default: {
                    if (sDate == null || eDate == null) {
                        if (sDate != null) {
                            proceed = sDate.compareTo(expenseDate) < 0;
                        } else {
                            proceed = eDate.compareTo(expenseDate) > 0;
                        }
                    } else {
                        proceed = (sDate.compareTo(expenseDate) < 0) && (eDate.compareTo(expenseDate) > 0);
                    }
                    break;
                }
            }
            if (proceed) {
                //COMMON DATA
                ExpenseDto expenseDto = expenseMapper.modelToDto(expense);
                ExpenseDocumentDto docDto = docMapper.modelToDto(expense.getDocument());
                expenseDto.setDocument(docDto);

                List<PersonDto> persons = new ArrayList<>();
                List<Person> personList = expense.getPersons() != null ? expense.getPersons() : new ArrayList<>();
                personList.forEach(person -> {
                    PersonDto personDto = personMapper.modelToDto(person);
                    persons.add(personDto);
                });
                expenseDto.setPersons(persons);

                String masterValueType = expenseDto.getExpenseTypeMasterValue();
                if (masterValueType.equalsIgnoreCase("SUPPORT") || masterValueType.equalsIgnoreCase("LODGING") || masterValueType.equalsIgnoreCase("OTHERS")) {

                    expenseDto.setExpenseCountryId(expense.getExpenseCountry().getCountryId());
                    expenseDto.setExpenseStateId(expense.getExpenseState().get_id());
                    expenseDto.setExpenseCityId(expense.getExpenseCity().get_id());

                } else if (masterValueType.equalsIgnoreCase("TRANSPORT") || masterValueType.equalsIgnoreCase("KMS")) {

                    expenseDto.setFromCountryId(expense.getFromCountry().getCountryId());
                    expenseDto.setFromStateId(expense.getFromState().get_id());
                    expenseDto.setFromCityId(expense.getFromCity().get_id());

                    expenseDto.setToCountryId(expense.getToCountry().getCountryId());
                    expenseDto.setToStateId(expense.getToState().get_id());
                    expenseDto.setToCityId(expense.getToCity().get_id());

                }
                result.add(expenseDto);
            }
        }
        //DELETE ALL TEMPORAL DOCUMENTS
        deleteTemporalFilesOnServer();
        return result;
    }

    @Override
    public void saveExpense(HashMap data) {
        //------------------------------------- COMMON DATA ----------------------------------------------------------
        String type = String.valueOf(data.get("type"));
        String employeeId = String.valueOf(data.get("employeeId"));
        String expenseId = String.valueOf(data.get("expenseId"));
        String expenseDate = String.valueOf(data.get("expenseDate"));
        String statusMasterValue = String.valueOf(data.get("status"));
        String voucherTypeId = String.valueOf(data.get("voucherTypeId"));
        String localCurrencyTypeId = String.valueOf(data.get("localCurrencyTypeId"));
        String localCurrencyAmount = String.valueOf(data.get("localCurrencyAmount"));
        //------------------------------------  ADDITIONAL DATA ------------------------------------------------------
        String subtypeId = String.valueOf(data.get("subtypeId"));
        String expenseCountryId = String.valueOf(data.get("expenseCountryId"));
        String expenseStateId = String.valueOf(data.get("expenseStateId"));
        String expenseCityId = String.valueOf(data.get("expenseCityId"));
        String arrivalDate = String.valueOf(data.get("arrivalDate"));
        String departureDate = String.valueOf(data.get("departureDate"));
        String description = String.valueOf(data.get("description"));
        String fromCountryId = String.valueOf(data.get("fromCountryId"));
        String fromStateId = String.valueOf(data.get("fromStateId"));
        String fromCityId = String.valueOf(data.get("fromCityId"));
        String toCountryId = String.valueOf(data.get("toCountryId"));
        String toStateId = String.valueOf(data.get("toStateId"));
        String toCityId = String.valueOf(data.get("toCityId"));
        String kms = String.valueOf(data.get("kms"));
        List<HashMap> personList = (List<HashMap>) data.get("persons");

        try {
            Expense expense = !expenseId.isEmpty() ? expenseRepository.findExpenseBy_id(expenseId) : new Expense();

            Date today = Calendar.getInstance().getTime();
            expense.setRegisterDate(today);


            expense.setExpenseDate(new SimpleDateFormat("dd/MM/yyyy").parse(expenseDate));


            StaffExpenseType staffExpenseType = staffExpenseTypeRepository.findStaffExpenseTypeByMasterValue(type);
            expense.setExpenseType(staffExpenseType);

            if (staffExpenseType.isAllowSubtypes()) {
                expense.setExpenseSubtypeId(subtypeId);
            }

            Staff staff = staffRepository.findAllByStaffId(employeeId);
            expense.setStaff(staff);

            VoucherType voucherType = voucherTypeRepository.findVoucherTypeBy_id(voucherTypeId);
            expense.setVoucherType(voucherType);

            ExpenseStatus expenseStatus = expenseStatusRepository.findExpenseStatusByMasterValue(statusMasterValue);
            expense.setStatus(expenseStatus);

            TypeOfCurrency localCurrencyType = currencyTypeRepository.findAllBy_id(localCurrencyTypeId);
            expense.setLocalCurrencyType(localCurrencyType);

            BigDecimal localAmount = new BigDecimal(Double.parseDouble(localCurrencyAmount)).setScale(2, RoundingMode.HALF_UP);
            expense.setLocalCurrencyAmount(localAmount);

            TypeOfCurrency euroCurrencyType = currencyTypeRepository.findByCurrencyCode("EUR");
            expense.setEuroCurrencyType(euroCurrencyType);

            CurrencyDto currencyManagementDto = currencyService.getLastDataByCurrencyType(localCurrencyTypeId);
            BigDecimal euroAmount = localAmount.multiply(new BigDecimal(currencyManagementDto.getChangeFactor())).setScale(2, RoundingMode.HALF_UP);
            expense.setEuroAmount(euroAmount);

            if (type.equalsIgnoreCase("SUPPORT") || type.equalsIgnoreCase("LODGING") || type.equalsIgnoreCase("OTHERS")) {

                Country expenseCountry = countryRepository.getByCountryId(expenseCountryId);
                expense.setExpenseCountry(expenseCountry);

                StateCountry expenseState = stateCountryRepository.findStateCountryBy_id(expenseStateId);
                expense.setExpenseState(expenseState);

                City expenseCity = cityRepository.findCityBy_id(expenseCityId);
                expense.setExpenseCity(expenseCity);

                if (type.equalsIgnoreCase("LODGING")) {
                    expense.setArrivalDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(arrivalDate));
                    expense.setDepartureDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(departureDate));
                }

                if (type.equalsIgnoreCase("OTHERS")) {
                    expense.setDescription(description);
                }
            }

            if (type.equalsIgnoreCase("TRANSPORT") || type.equalsIgnoreCase("KMS")) {

                Country fromCountry = countryRepository.getByCountryId(fromCountryId);
                expense.setFromCountry(fromCountry);

                StateCountry fromState = stateCountryRepository.findStateCountryBy_id(fromStateId);
                expense.setFromState(fromState);

                City fromCity = cityRepository.findCityBy_id(fromCityId);
                expense.setFromCity(fromCity);

                Country toCountry = countryRepository.getByCountryId(toCountryId);
                expense.setToCountry(toCountry);

                StateCountry toState = stateCountryRepository.findStateCountryBy_id(toStateId);
                expense.setToState(toState);

                City toCity = cityRepository.findCityBy_id(toCityId);
                expense.setToCity(toCity);

                if (type.equalsIgnoreCase("KMS")) {
                    expense.setKms(Double.parseDouble(kms));
                }
            }

            if (type.equalsIgnoreCase("SUPPORT") || type.equalsIgnoreCase("TRANSPORT") || type.equalsIgnoreCase("KMS")) {
                List<Person> persons = new ArrayList<>();
                for (HashMap obj : personList) {
                    PersonType personType = personTypeRepository.findPersonTypeBy_id(String.valueOf(obj.get("personTypeId")));
                    Person p = new Person();
                    p.setPersonType(personType);
                    p.setCompanyName(String.valueOf(obj.get("companyName")));
                    p.setName(String.valueOf(obj.get("name")));
                    p.setFatherFamilyName(String.valueOf(obj.get("fatherFamilyName")));
                    p.setMotherFamilyName(String.valueOf(obj.get("motherFamilyName")));

                    persons.add(p);
                }
                expense.setPersons(persons);
            }
            expenseRepository.save(expense);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveExpenseWithFile(HashMap data) {
        //------------------------------------- COMMON DATA ----------------------------------------------------------
        String type = String.valueOf(data.get("type"));
        String employeeId = String.valueOf(data.get("employeeId"));
        String expenseId = String.valueOf(data.get("expenseId"));
        String expenseDate = String.valueOf(data.get("expenseDate"));
        String statusMasterValue = String.valueOf(data.get("status"));
        String voucherTypeId = String.valueOf(data.get("voucherTypeId"));
        String localCurrencyTypeId = String.valueOf(data.get("localCurrencyTypeId"));
        String localCurrencyAmount = String.valueOf(data.get("localCurrencyAmount"));
        //------------------------------------  ADDITIONAL DATA ------------------------------------------------------
        String subtypeId = String.valueOf(data.get("subtypeId"));
        MultipartFile file = (MultipartFile) data.get("file");
        String expenseCountryId = String.valueOf(data.get("expenseCountryId"));
        String expenseStateId = String.valueOf(data.get("expenseStateId"));
        String expenseCityId = String.valueOf(data.get("expenseCityId"));
        String arrivalDate = String.valueOf(data.get("arrivalDate"));
        String departureDate = String.valueOf(data.get("departureDate"));
        String description = String.valueOf(data.get("description"));
        String fromCountryId = String.valueOf(data.get("fromCountryId"));
        String fromStateId = String.valueOf(data.get("fromStateId"));
        String fromCityId = String.valueOf(data.get("fromCityId"));
        String toCountryId = String.valueOf(data.get("toCountryId"));
        String toStateId = String.valueOf(data.get("toStateId"));
        String toCityId = String.valueOf(data.get("toCityId"));
        String kms = String.valueOf(data.get("kms"));
        String[] personTypeIds = (String[]) data.get("personTypeIds");
        String[] personCompanyNames = (String[]) data.get("personCompanyNames");
        String[] personNames = (String[]) data.get("personNames");
        String[] personFatherFamilyNames = (String[]) data.get("personFatherFamilyNames");
        String[] personMotherFamilyNames = (String[]) data.get("personMotherFamilyNames");

        try {
            Expense expense = !expenseId.isEmpty() ? expenseRepository.findExpenseBy_id(expenseId) : new Expense();

            Date today = Calendar.getInstance().getTime();
            expense.setRegisterDate(today);


            expense.setExpenseDate(new SimpleDateFormat("dd/MM/yyyy").parse(expenseDate));


            StaffExpenseType staffExpenseType = staffExpenseTypeRepository.findStaffExpenseTypeByMasterValue(type);
            expense.setExpenseType(staffExpenseType);

            if (staffExpenseType.isAllowSubtypes()) {
                expense.setExpenseSubtypeId(subtypeId);
            }

            Staff staff = staffRepository.findAllByStaffId(employeeId);
            expense.setStaff(staff);

            VoucherType voucherType = voucherTypeRepository.findVoucherTypeBy_id(voucherTypeId);
            expense.setVoucherType(voucherType);

            ExpenseStatus expenseStatus = expenseStatusRepository.findExpenseStatusByMasterValue(statusMasterValue);
            expense.setStatus(expenseStatus);

            TypeOfCurrency localCurrencyType = currencyTypeRepository.findAllBy_id(localCurrencyTypeId);
            expense.setLocalCurrencyType(localCurrencyType);

            BigDecimal localAmount = new BigDecimal(Double.parseDouble(localCurrencyAmount)).setScale(2, RoundingMode.HALF_UP);
            expense.setLocalCurrencyAmount(localAmount);

            TypeOfCurrency euroCurrencyType = currencyTypeRepository.findByCurrencyCode("EUR");
            expense.setEuroCurrencyType(euroCurrencyType);

            CurrencyDto currencyManagementDto = currencyService.getLastDataByCurrencyType(localCurrencyTypeId);
            BigDecimal euroAmount = localAmount.multiply(new BigDecimal(currencyManagementDto.getChangeFactor())).setScale(2, RoundingMode.HALF_UP);
            expense.setEuroAmount(euroAmount);

            if (type.equalsIgnoreCase("SUPPORT") || type.equalsIgnoreCase("LODGING") || type.equalsIgnoreCase("OTHERS")) {

                Country expenseCountry = countryRepository.getByCountryId(expenseCountryId);
                expense.setExpenseCountry(expenseCountry);

                StateCountry expenseState = stateCountryRepository.findStateCountryBy_id(expenseStateId);
                expense.setExpenseState(expenseState);

                City expenseCity = cityRepository.findCityBy_id(expenseCityId);
                expense.setExpenseCity(expenseCity);

                if (type.equalsIgnoreCase("LODGING")) {
                    expense.setArrivalDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(arrivalDate));
                    expense.setDepartureDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(departureDate));
                }

                if (type.equalsIgnoreCase("OTHERS")) {
                    expense.setDescription(description);
                }
            }

            if (type.equalsIgnoreCase("TRANSPORT") || type.equalsIgnoreCase("KMS")) {

                Country fromCountry = countryRepository.getByCountryId(fromCountryId);
                expense.setFromCountry(fromCountry);

                StateCountry fromState = stateCountryRepository.findStateCountryBy_id(fromStateId);
                expense.setFromState(fromState);

                City fromCity = cityRepository.findCityBy_id(fromCityId);
                expense.setFromCity(fromCity);

                Country toCountry = countryRepository.getByCountryId(toCountryId);
                expense.setToCountry(toCountry);

                StateCountry toState = stateCountryRepository.findStateCountryBy_id(toStateId);
                expense.setToState(toState);

                City toCity = cityRepository.findCityBy_id(toCityId);
                expense.setToCity(toCity);

                if (type.equalsIgnoreCase("KMS")) {
                    expense.setKms(Double.parseDouble(kms));
                }
            }


            if (type.equalsIgnoreCase("SUPPORT") || type.equalsIgnoreCase("TRANSPORT") || type.equalsIgnoreCase("KMS")) {
                List<Person> persons = new ArrayList<>();
                for (int i = 0; i < personTypeIds.length; i++) {
                    PersonType personType = personTypeRepository.findPersonTypeBy_id(personTypeIds[i]);
                    Person p = new Person();
                    p.setPersonType(personType);
                    p.setCompanyName(personCompanyNames[i]);
                    p.setName(personNames[i]);
                    p.setFatherFamilyName(personFatherFamilyNames[i]);
                    p.setMotherFamilyName(personMotherFamilyNames[i]);

                    persons.add(p);
                }
                expense.setPersons(persons);
            }

            if (!voucherType.getMasterValue().equalsIgnoreCase("DONT EXIST") && file != null) {
                ExpenseDocument doc = new ExpenseDocument();
                doc.setName(file.getOriginalFilename());
                doc.setType(file.getContentType());
                doc.setUploadDate(today);
                doc.setData(new Binary(file.getBytes()));

                expense.setDocument(doc);
            }

            expenseRepository.save(expense);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeStatusExpense(HashMap data) {
        String expenseId = String.valueOf(data.get("expenseId"));
        String expenseStatusMasterValue = String.valueOf(data.get("expenseStatusMasterValue"));
        Optional<Expense> obj = expenseRepository.findById(expenseId);
        if (obj.isPresent()) {
            Expense expense = obj.get();
            ExpenseStatus expenseStatus = expenseStatusRepository.findExpenseStatusByMasterValue(expenseStatusMasterValue);
            expense.setStatus(expenseStatus);
            expenseRepository.save(expense);
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }


    @Override
    public boolean existsExpensesWithStatus(String statusId) {
        ExpenseStatus expenseStatus = expenseStatusRepository.findExpenseStatusBy_id(statusId);
        List<Expense> expenseList = expenseRepository.findAllByStatus(expenseStatus);
        return !expenseList.isEmpty();
    }

    @Override
    public File exportExpenses(HashMap data) {

        String employeeId = data.containsKey("employeeId") ? (String) data.get("employeeId") : "";
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

        Staff staff = !employeeId.isEmpty() ? staffRepository.findAllByStaffId(employeeId) : null;

        List<Expense> initialList = staff != null ? expenseRepository.findAllByStaff(staff) : expenseRepository.findAll();
        List<Expense> expenseList = new ArrayList<>();

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
        for (Expense expense : initialList) {
            Date requestDate = expense.getExpenseDate();
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
                expenseList.add(expense);
            }
        }

        Collections.sort(expenseList, Comparator.comparing(Expense::getExpenseDate).reversed());
        String option = staff != null ? "BY EMPLOYEE" : "ALL";
        boolean generated = generateFile(fileType, expenseList, timePeriod, option);
        if (generated) {
            //return the file from server location
            String fileName = temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "EXPENSES" + (fileType.equals("excel") ? ".xlsx" : ".pdf");
            return new File(fileName);
        } else {
            throw exceptionFile(NOT_GENERATED);
        }
    }


    @Override
    public byte[] downloadDocumentOfExpense(String expenseId) {
        Expense expense = expenseRepository.findExpenseBy_id(expenseId);
        ExpenseDocument doc = expense.getDocument();
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            InputStream in = new ByteArrayInputStream(doc.getData().getData());
            byte[] bytes = new byte[1024];
            int length;
            while ((length = in.read(bytes)) >= 0) {
                bos.write(bytes, 0, length);
            }
            in.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    //------------------------------------------------------------------------------------------------------------------

    private boolean generateFile(String fileType, List<Expense> expensesList, String timePeriod, String option) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.00");
        boolean generated = true;
        double totalEuroAmount = 0;
        double paidEuroAmount = 0;
        switch (fileType) {
            case "excel": {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("EXPENSES LIST (" + timePeriod + ")");

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

                String[] columns = option.equalsIgnoreCase("ALL") ? new String[]{"Status", "Expense Type",
                        "Expense Subtype", "Name", "Father Family Name", "Mother Family Name", "Company Name", "Company Email", "Expense Date",
                        "Register Date", "Voucher Type", "Local Currency Type", "Local Currency Amount", "Euro Amount", "Payment Date"
                } : new String[]{"Status", "Expense Type", "Expense Subtype", "Expense Date", "Register Date",
                        "Voucher Type", "Local Currency Type", "Local Currency Amount", "Euro Amount", "Payment Date"};

                AtomicInteger pos = new AtomicInteger(0);
                Row headerRow = sheet.createRow(pos.get());
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i, CellType.STRING);
                    cell.setCellValue(columns[i]);
                    cell.setCellStyle(tableHeaderStyle);
                }

                for (Expense expense : expensesList) {
                    Row dataRow = sheet.createRow(pos.incrementAndGet());
                    for (int i = 0; i < columns.length; i++) {
                        Cell dataCell = dataRow.createCell(i, CellType.STRING);
                        switch (i) {
                            case 0: {
                                dataCell.setCellValue(expense.getStatus().getName());
                                break;
                            }
                            case 1: {
                                dataCell.setCellValue(expense.getExpenseType().getName());
                                break;
                            }
                            case 2: {
                                if (expense.getExpenseType().isAllowSubtypes()) {
                                    StaffExpenseSubtypeDto staffExpenseSubtypeDto = staffExpensesTypesService.getSubtypeBy(expense.getExpenseType().getMasterValue(), expense.getExpenseSubtypeId());
                                    dataCell.setCellValue(staffExpenseSubtypeDto.getName());
                                } else {
                                    dataCell = dataRow.createCell(i, CellType.BLANK);
                                }
                                break;
                            }
                            case 3: {
                                dataCell.setCellValue(option.equalsIgnoreCase("ALL") ? String.valueOf(expense.getStaff().getFirstName()) : new SimpleDateFormat("yyyy/MM/dd").format(expense.getExpenseDate()));
                                break;
                            }
                            case 4: {
                                dataCell.setCellValue(option.equalsIgnoreCase("ALL") ? String.valueOf(expense.getStaff().getFatherFamilyName()) : new SimpleDateFormat("yyyy/MM/dd hh:mm a").format(expense.getRegisterDate()));
                                break;
                            }
                            case 5: {
                                dataCell.setCellValue(option.equalsIgnoreCase("ALL") ? String.valueOf(expense.getStaff().getMotherFamilyName()) : expense.getVoucherType().getName());
                                break;
                            }
                            case 6: {
                                dataCell.setCellValue(option.equalsIgnoreCase("ALL") ? String.valueOf(expense.getStaff().getStaffContract().getCompany().getName()) : expense.getLocalCurrencyType().getCurrencyName());
                                break;
                            }
                            case 7: {
                                dataCell.setCellValue(option.equalsIgnoreCase("ALL") ? String.valueOf(expense.getStaff().getCompanyEmail()) : decimalFormat.format(expense.getLocalCurrencyAmount().doubleValue()));
                                break;
                            }
                            case 8: {
                                dataCell = dataRow.createCell(i, CellType.NUMERIC);
                                dataCell.setCellValue(option.equalsIgnoreCase("ALL") ? new SimpleDateFormat("yyyy/MM/dd").format(expense.getExpenseDate()) : decimalFormat.format(expense.getEuroAmount().doubleValue()));
                                break;
                            }
                            case 9: {
                                if (option.equalsIgnoreCase("ALL")) {
                                    dataCell.setCellValue(new SimpleDateFormat("yyyy/MM/dd hh:mm a").format(expense.getRegisterDate()));
                                } else {
                                    if (expense.getPaymentDate() != null) {
                                        dataCell.setCellValue(new SimpleDateFormat("yyyy/MM/dd hh:mm a").format(expense.getPaymentDate()));
                                    } else {
                                        dataCell = dataRow.createCell(i, CellType.BLANK);
                                    }
                                }
                                break;
                            }
                            case 10: {
                                dataCell.setCellValue(expense.getVoucherType().getName());
                                break;
                            }
                            case 11: {
                                dataCell.setCellValue(expense.getLocalCurrencyType().getCurrencyName());
                                break;
                            }
                            case 12: {
                                dataCell = dataRow.createCell(i, CellType.NUMERIC);
                                dataCell.setCellValue(decimalFormat.format(expense.getLocalCurrencyAmount().doubleValue()));
                                break;
                            }
                            case 13: {
                                dataCell = dataRow.createCell(i, CellType.NUMERIC);
                                dataCell.setCellValue(decimalFormat.format(expense.getEuroAmount().doubleValue()));
                                break;
                            }
                            case 14: {
                                if (expense.getPaymentDate() != null) {
                                    dataCell.setCellValue(new SimpleDateFormat("yyyy/MM/dd hh:mm a").format(expense.getPaymentDate()));
                                } else {
                                    dataCell = dataRow.createCell(i, CellType.BLANK);
                                }
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        dataCell.setCellStyle(dataRowStyle);
                    }
                    totalEuroAmount += expense.getEuroAmount().doubleValue();
                    if (expense.getStatus().getMasterValue().equalsIgnoreCase("PAID")) {
                        paidEuroAmount += expense.getEuroAmount().doubleValue();
                    }
                }

                for (String columnTitle : Arrays.asList("Paid", "Total")) {
                    Row dataRow = sheet.createRow(pos.incrementAndGet());
                    Cell nameCell = dataRow.createCell(option.equalsIgnoreCase("ALL") ? 12 : 7, CellType.STRING);
                    nameCell.setCellValue(columnTitle);
                    nameCell.setCellStyle(totalRowStyle);

                    Cell valueCell = dataRow.createCell(option.equalsIgnoreCase("ALL") ? 13 : 8, CellType.NUMERIC);
                    valueCell.setCellValue(columnTitle.equalsIgnoreCase("Paid") ? paidEuroAmount > 0 ? decimalFormat.format(paidEuroAmount) : "0.00" : decimalFormat.format(totalEuroAmount));
                    valueCell.setCellStyle(totalRowStyle);
                }

                String fileName = temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "EXPENSES.xlsx";

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
                    PdfWriter.getInstance(document, new FileOutputStream(temporalDocumentsDirectory + FileSystems.getDefault().getSeparator() + "EXPENSES.pdf"));
                    document.open();
                    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK);
                    Chunk title = new Chunk("EXPENSES LIST", titleFont);
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

                    String[] columns = new String[]{"Expense Type", " Expense Subtype", "Expense Date", "Voucher Type", "Local Currency Type", "Local Currency Amount", "Euro Amount", "Payment Date"};

                    for (Expense expense : expensesList) {
                        Paragraph status = new Paragraph();
                        status.add(0, new Chunk("Status: ", boldFont));
                        String statusName = expense.getStatus().getName();
                        Chunk statusData = new Chunk(statusName, dataWhiteFont);
                        switch (expense.getStatus().getMasterValue()) {
                            case "PAID": {
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
                        Paragraph registerDate = new Paragraph();
                        registerDate.add(0, new Chunk("Register Date: ", boldFont));
                        registerDate.add(1, new Chunk(new SimpleDateFormat("yyyy/MM/dd hh:mm a").format(expense.getRegisterDate()), dataFont));

                        document.add(status);
                        document.add(registerDate);
                        if (option.equalsIgnoreCase("ALL")) {
                            Paragraph fullName = new Paragraph();
                            fullName.add(0, new Chunk("Employee Name: ", boldFont));
                            fullName.add(1, new Chunk(expense.getStaff().getFullName(), dataFont));

                            Paragraph company = new Paragraph();
                            company.add(0, new Chunk("Company Name: ", boldFont));
                            company.add(1, new Chunk(expense.getStaff().getStaffContract().getCompany().getName(), dataFont));

                            Paragraph companyEmail = new Paragraph();
                            companyEmail.add(0, new Chunk("Company Email: ", boldFont));
                            companyEmail.add(1, new Chunk(expense.getStaff().getCompanyEmail(), dataFont));

                            document.add(fullName);
                            document.add(company);
                            document.add(companyEmail);
                        }

                        PdfPTable expensesTable = new PdfPTable(8);
                        expensesTable.setWidthPercentage(100);
                        for (int i = 0; i < columns.length; i++) {
                            PdfPCell header = new PdfPCell();
                            header.setBackgroundColor(new BaseColor(130, 224, 170));
                            header.setPhrase(new Phrase(columns[i], boldFont));
                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                            header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            header.setBorder(PdfPCell.NO_BORDER);
                            expensesTable.addCell(header);
                        }
                        for (int i = 0; i < columns.length; i++) {
                            PdfPCell cell = new PdfPCell();
                            switch (i) {
                                case 0: {
                                    cell.setPhrase(new Phrase(expense.getExpenseType().getName(), dataFont));
                                    break;
                                }
                                case 1: {
                                    String subtypeName = "";
                                    if (expense.getExpenseType().isAllowSubtypes()) {
                                        StaffExpenseSubtypeDto staffExpenseSubtypeDto = staffExpensesTypesService.getSubtypeBy(expense.getExpenseType().getMasterValue(), expense.getExpenseSubtypeId());
                                        subtypeName = staffExpenseSubtypeDto.getName();
                                    }
                                    cell.setPhrase(new Phrase(subtypeName, dataFont));
                                    break;
                                }
                                case 2: {
                                    cell.setPhrase(new Phrase(new SimpleDateFormat("yyyy/MM/dd").format(expense.getExpenseDate()), dataFont));
                                    break;
                                }
                                case 3: {
                                    cell.setPhrase(new Phrase(expense.getVoucherType().getName(), dataFont));
                                    break;
                                }
                                case 4: {
                                    cell.setPhrase(new Phrase(expense.getLocalCurrencyType().getCurrencyName(), dataFont));
                                    break;
                                }
                                case 5: {
                                    cell.setPhrase(new Phrase(decimalFormat.format(expense.getLocalCurrencyAmount().doubleValue()), dataFont));
                                    break;
                                }
                                case 6: {
                                    cell.setPhrase(new Phrase(decimalFormat.format(expense.getEuroAmount().doubleValue()), dataFont));
                                    break;
                                }
                                case 7: {
                                    if (expense.getPaymentDate() != null) {
                                        cell.setPhrase(new Phrase(new SimpleDateFormat("yyyy/MM/dd hh:mm a").format(expense.getPaymentDate()), dataFont));
                                    } else {
                                        cell.setPhrase(new Phrase("", dataFont));
                                    }
                                    break;
                                }
                                default: {
                                    break;
                                }
                            }
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            expensesTable.addCell(cell);
                        }
                        totalEuroAmount += expense.getEuroAmount().doubleValue();
                        if (expense.getStatus().getMasterValue().equalsIgnoreCase("PAID")) {
                            paidEuroAmount += expense.getEuroAmount().doubleValue();
                        }
                        expensesTable.setWidths(new int[]{2, 2, 2, 3, 3, 3, 2, 3});
                        expensesTable.setSpacingBefore(10);
                        expensesTable.setSpacingAfter(10);
                        document.add(expensesTable);

                        document.add(new DottedLineSeparator());
                    }

                    PdfPTable totalTable = new PdfPTable(8);
                    totalTable.setWidthPercentage(100);
                    for (String columnTitle : Arrays.asList("Paid", "Total")) {
                        for (int i = 0; i < 8; i++) {
                            if (i != 5 && i != 6) {
                                PdfPCell emptyCell = new PdfPCell();
                                emptyCell.setPhrase(new Phrase("", boldFont));
                                emptyCell.setBorder(PdfPCell.NO_BORDER);
                                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                emptyCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                totalTable.addCell(emptyCell);
                            } else {
                                if (i == 5) {
                                    PdfPCell nameCell = new PdfPCell();
                                    nameCell.setPhrase(new Phrase(columnTitle, boldFont));
                                    nameCell.setBorder(PdfPCell.NO_BORDER);
                                    nameCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                    nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    totalTable.addCell(nameCell);
                                } else {
                                    PdfPCell valueCell = new PdfPCell();
                                    valueCell.setPhrase(new Phrase(columnTitle.equalsIgnoreCase("Paid") ? paidEuroAmount > 0 ? decimalFormat.format(paidEuroAmount) : "0.00" : decimalFormat.format(totalEuroAmount), boldFont));
                                    valueCell.setBorder(PdfPCell.NO_BORDER);
                                    valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                    valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    totalTable.addCell(valueCell);
                                }
                            }
                        }
                    }
                    totalTable.setWidths(new int[]{2, 2, 2, 3, 3, 3, 2, 3});
                    totalTable.setSpacingBefore(10);
                    totalTable.setSpacingAfter(10);
                    document.add(totalTable);

                    document.close();
                } catch (DocumentException | FileNotFoundException e) {
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
        return MainException.throwException(EntityType.Expense, exceptionType, args);
    }

    private RuntimeException exceptionFile(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.File, exceptionType, args);
    }

}
