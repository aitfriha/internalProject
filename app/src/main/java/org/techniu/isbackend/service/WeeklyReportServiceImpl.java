package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.AbsenceRequestMapper;
import org.techniu.isbackend.dto.mapper.LocalBankHolidayMapper;
import org.techniu.isbackend.dto.mapper.StaffMapper;
import org.techniu.isbackend.dto.mapper.WeeklyWorkMapper;
import org.techniu.isbackend.dto.model.*;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.techniu.isbackend.exception.ExceptionType.EMPTY_MANDATORY_FIELDS;
import static org.techniu.isbackend.exception.ExceptionType.ERRORS_IN_JOURNAL_VALUES;
import static org.techniu.isbackend.service.utilities.Util.*;


@Service
public class WeeklyReportServiceImpl implements WeeklyReportService {

    private WeeklyWorkRepository weeklyWorkRepository;

    private AbsenceRequestRepository absenceRepository;

    private LocalBankHolidayRepository localBankHolidayRepository;

    private StaffRepository staffRepository;

    private ClientRepository customerRepository;

    private CommercialOperationRepository operationRepository;

    private AssignmentTypeRepository assignmentTypeRepository;

    private final AbsenceRequestMapper absenceMapper = Mappers.getMapper(AbsenceRequestMapper.class);
    private final LocalBankHolidayMapper localBankHolidayMapper = Mappers.getMapper(LocalBankHolidayMapper.class);
    private final WeeklyWorkMapper weeklyWorkMapper = Mappers.getMapper(WeeklyWorkMapper.class);
    private final StaffMapper staffMapper = Mappers.getMapper(StaffMapper.class);

    public WeeklyReportServiceImpl(WeeklyWorkRepository weeklyWorkRepository,
                                   AbsenceRequestRepository absenceRepository,
                                   LocalBankHolidayRepository localBankHolidayRepository,
                                   StaffRepository staffRepository,
                                   ClientRepository customerRepository,
                                   CommercialOperationRepository operationRepository,
                                   AssignmentTypeRepository assignmentTypeRepository) {
        this.weeklyWorkRepository = weeklyWorkRepository;
        this.absenceRepository = absenceRepository;
        this.localBankHolidayRepository = localBankHolidayRepository;
        this.staffRepository = staffRepository;
        this.customerRepository = customerRepository;
        this.operationRepository = operationRepository;
        this.assignmentTypeRepository = assignmentTypeRepository;

    }

    @Override
    public List<SummarizedWeeklyReport> getAllSummarizedWeeklyReportByStaff(HashMap data) {
        String employeeId = (String) data.get("employeeId");
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
        List<SummarizedWeeklyReport> result = new ArrayList<>();
        Staff employee = staffRepository.findAllByStaffId(employeeId);
        SimpleDateFormat formatDate = new SimpleDateFormat("MMMM dd", Locale.ENGLISH);
        List<String> weeks = new ArrayList<>();
        if (employee != null) {
            List<WeeklyWork> list = weeklyWorkRepository.findAllByStaff(employee);
            //If the employee is the leader of the functional structure that his belong,
            // we must search all employees that belongs to the functional structure below his level
            for (WeeklyWork el : list) {
                int year = el.getYear();
                int week = el.getWeek();
                boolean proceed = false;
                Calendar calendar = Calendar.getInstance();
                switch (period) {
                    case "month": {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.WEEK_OF_YEAR, week);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int actualMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
                        proceed = month == actualMonth;
                        break;
                    }
                    case "year": {
                        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
                        proceed = year == actualYear;
                        break;
                    }
                    default: {
                        if (sDate == null || eDate == null) {
                            if (sDate != null) {
                                calendar.setTime(sDate);
                                int sYear = calendar.get(Calendar.YEAR);
                                int sWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                                proceed = year >= sYear && week >= sWeek;
                            } else {
                                calendar.setTime(eDate);
                                int eYear = calendar.get(Calendar.YEAR);
                                int eWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                                proceed = year <= eYear && week <= eWeek;
                            }
                        } else {
                            calendar.setTime(sDate);
                            int sYear = calendar.get(Calendar.YEAR);
                            int sWeek = calendar.get(Calendar.WEEK_OF_YEAR);

                            calendar = Calendar.getInstance();
                            calendar.setTime(eDate);
                            int eYear = calendar.get(Calendar.YEAR);
                            int eWeek = calendar.get(Calendar.WEEK_OF_YEAR);

                            proceed = (year >= sYear && year <= eYear) && (week >= sWeek && week <= eWeek);
                        }
                        break;
                    }
                }
                if (proceed) {
                    SummarizedWeeklyReport temp = new SummarizedWeeklyReport();
                    String weekOfYear = year + "/" + week;
                    HashMap dates = getStartAndEndOfWeek(year, week);
                    temp.setYear(year);
                    temp.setWeek(week);
                    temp.setWeekOfYear(weekOfYear + " (" + formatDate.format((Date) dates.get("start")) + " - " + formatDate.format((Date) dates.get("end")) + ")");
                    temp.setEmployeeId(el.getStaff().getStaffId());
                    temp.setEmployee(el.getStaff().getFullName());
                    temp.setCompanyEmail(el.getStaff().getCompanyEmail());
                    temp.setAssignedCost(el.getCustomer().getName());
                    temp.setConcept(el.getOperation().getName());
                    temp.setDeliverable(el.getDeliverable());
                    temp.setAssignmentType(el.getAssignmentType() != null ? el.getAssignmentType().getName() : "");
                    temp.setDays(new BigDecimal(el.getMonday() + el.getTuesday() + el.getWednesday() + el.getThursday() + el.getFriday()).setScale(1, RoundingMode.HALF_UP).doubleValue());
                    temp.setRegisterDate(el.getRegisterDate());
                    temp.setEditable(true);
                    if (!weeks.contains(weekOfYear)) {
                        weeks.add(weekOfYear);
                    }
                    result.add(temp);
                }
            }

            weeks.forEach(el -> {
                String[] split = el.split("/");
                if (split.length == 2) {
                    int year = Integer.parseInt(split[0]);
                    int week = Integer.parseInt(split[1]);
                    HashMap dates = getStartAndEndOfWeek(year, week);

                    Date startDate = (Date) dates.get("start");
                    Date endDate = (Date) dates.get("end");

                    List<AbsenceRequest> absences = absenceRepository.findAllByStaff(employee);
                    absences.forEach(abs -> {
                        double value = getTotalDays(abs, startDate, endDate);
                        if (value > 0) {
                            SummarizedWeeklyReport temp = new SummarizedWeeklyReport();
                            String concept = abs.getAbsenceType().getName();
                            String weekOfYear = year + "/" + week;
                            Optional<SummarizedWeeklyReport> opt = result.stream().filter(r -> r.getYear() == year && r.getWeek() == week && r.getConcept().equalsIgnoreCase(concept)).findFirst();
                            if (!opt.isPresent()) {
                                temp.setYear(year);
                                temp.setWeek(week);
                                temp.setWeekOfYear(weekOfYear + " (" + formatDate.format(startDate) + " - " + formatDate.format(endDate) + ")");
                                temp.setEmployee(employee.getFullName());
                                temp.setCompanyEmail(employee.getCompanyEmail());
                                temp.setAssignedCost(employee.getStaffContract().getCompany().getName());
                                temp.setConcept(concept);
                                temp.setDeliverable("");
                                temp.setAssignmentType("");
                                temp.setDays(value);
                                //temp.setRegisterDate(abs.getDate());
                                temp.setEditable(false);

                                result.add(temp);
                            } else {
                                temp = opt.get();
                                int index = result.indexOf(temp);
                                result.get(index).setDays(new BigDecimal(result.get(index).getDays() + value).setScale(1, RoundingMode.HALF_UP).doubleValue());
                                //result.get(index).setRegisterDate(abs.getDate());
                            }
                        }
                    });

                    //LOCAL BANK HOLIDAYS

                    List<LocalBankHoliday> localBankHolidays = localBankHolidayRepository.getAllByCompany(employee.getStaffContract().getCompany());
                    localBankHolidays.forEach(lbh -> {
                        double value = getTotalDays(lbh, startDate, endDate);
                        if (value > 0) {
                            SummarizedWeeklyReport temp = new SummarizedWeeklyReport();
                            String weekOfYear = year + "/" + week;
                            String concept = lbh.getName() + " (" + lbh.getCompany().getAddress().getCity().getStateCountry().getCountry().getCountryName() + ")";
                            Optional<SummarizedWeeklyReport> opt = result.stream().filter(r -> r.getYear() == year && r.getWeek() == week && r.getConcept().equalsIgnoreCase(concept)).findFirst();
                            if (!opt.isPresent()) {
                                temp.setYear(year);
                                temp.setWeek(week);
                                temp.setWeekOfYear(weekOfYear + " (" + formatDate.format(startDate) + " - " + formatDate.format(endDate) + ")");
                                temp.setEmployee(employee.getFullName());
                                temp.setCompanyEmail(employee.getCompanyEmail());
                                temp.setAssignedCost(employee.getStaffContract().getCompany().getName());
                                temp.setConcept(concept);
                                temp.setDeliverable("");
                                temp.setAssignmentType("");
                                temp.setDays(value);
                                //temp.setRegisterDate(lbh.getDate());
                                temp.setEditable(false);

                                result.add(temp);
                            } else {
                                temp = opt.get();
                                int index = result.indexOf(temp);
                                result.get(index).setDays(new BigDecimal(result.get(index).getDays() + value).setScale(1, RoundingMode.HALF_UP).doubleValue());
                                //result.get(index).setRegisterDate(lbh.getDate());
                            }
                        }
                    });
                }
            });

        }
        return result;
    }

    @Override
    public WeeklyReportDto getExtendedWeeklyReport(HashMap data) {
        WeeklyReportDto weeklyReportDto = new WeeklyReportDto();
        if (data.containsKey("employeeId") && data.containsKey("year") && data.containsKey("week")) {
            String employeeId = (String) data.get("employeeId");
            int year = Integer.parseInt(String.valueOf(data.get("year")));
            int week = Integer.parseInt(String.valueOf(data.get("week")));

            Staff employee = staffRepository.findAllByStaffId(employeeId);

            HashMap dates = getStartAndEndOfWeek(year, week);
            Date startDate = (Date) dates.get("start");
            Date endDate = (Date) dates.get("end");
            List<WeeklyWork> works = weeklyWorkRepository.findWeeklyWorkByStaffAndYearAndWeek(employee, year, week);
            List<AbsenceRequest> absences = filterAbsenceRequestsList(absenceRepository.findAllByStaff(employee), startDate, endDate);
            List<LocalBankHoliday> localBankHolidays = filterLocalBankHolidaysList(localBankHolidayRepository.getAllByCompany(employee.getStaffContract().getCompany()), startDate, endDate);

            SimpleDateFormat formatDate = new SimpleDateFormat("MMMM dd", Locale.ENGLISH);
            weeklyReportDto.setWeekOfYear((year + "/" + week) + " (" + formatDate.format(startDate) + " - " + formatDate.format(endDate) + ")");
            //Convert Staff to StaffDto
            StaffDto staffDto = staffMapper.modelToDto(employee);
            weeklyReportDto.setEmployeeId(staffDto.getStaffId());
            weeklyReportDto.setPersonalNumber(staffDto.getPersonalNumber());
            weeklyReportDto.setAvatar(staffDto.getPhoto());
            weeklyReportDto.setName(staffDto.getFirstName());
            weeklyReportDto.setFatherFamilyName(staffDto.getFatherFamilyName());
            weeklyReportDto.setMotherFamilyName(staffDto.getMotherFamilyName());
            weeklyReportDto.setCompanyEmail(staffDto.getCompanyEmail());
            weeklyReportDto.setCompany(staffDto.getCompanyName());

            //Convert each WeeklyWork to WeeklyWorkDto
            List<WeeklyWorkDto> worksDto = new ArrayList<>();
            works.forEach(work -> {
                WeeklyWorkDto weeklyWorkDto = weeklyWorkMapper.modelToDto(work);
                weeklyWorkDto.setCustomerId(work.getCustomer().get_id());
                weeklyWorkDto.setCustomerCode(work.getCustomer().getCode());
                weeklyWorkDto.setCustomerName(work.getCustomer().getName());
                weeklyWorkDto.setOperationId(work.getOperation().get_id());
                weeklyWorkDto.setOperationCode(work.getOperation().getCode());
                weeklyWorkDto.setOperationName(work.getOperation().getName());
                weeklyWorkDto.setAssignmentTypeId(work.getAssignmentType().get_id());
                weeklyWorkDto.setAssignmentTypeCode(work.getAssignmentType().getCode());
                weeklyWorkDto.setAssignmentTypeName(work.getAssignmentType().getName());

                worksDto.add(weeklyWorkDto);
            });
            weeklyReportDto.setWorks(worksDto);

            //Convert each Absence to AbsenceDto
            List<AbsenceRequestDto> absencesDto = new ArrayList<>();
            absences.forEach(absence -> {
                AbsenceRequestDto absenceDto = absenceMapper.modelToDto(absence);
                absencesDto.add(absenceDto);
            });
            weeklyReportDto.setAbsences(absencesDto);

            //Convert each LocalBankHoliday to LocalBankHolidayDto
            List<LocalBankHolidayDto> localBankHolidaysDto = new ArrayList<>();
            localBankHolidays.forEach(localBankHoliday -> {
                LocalBankHolidayDto localBankHolidayDto = localBankHolidayMapper.modelToDto(localBankHoliday);
                localBankHolidaysDto.add(localBankHolidayDto);
            });
            weeklyReportDto.setLocalBankHolidays(localBankHolidaysDto);
        }
        return weeklyReportDto;
    }

    @Override
    public void saveWeeklyReport(HashMap data) {
        int year = Integer.parseInt(String.valueOf(data.get("year")));
        int week = Integer.parseInt(String.valueOf(data.get("week")));
        String employeeId = String.valueOf(data.get("employeeId"));
        List<HashMap> works = (List<HashMap>) data.get("works");

        //VALIDATE EMPTY FIELDS
        AtomicInteger rowErrors = new AtomicInteger(0);
        works.forEach(w -> {
            boolean valid = validateRowData(w);
            if (!valid) {
                rowErrors.getAndIncrement();
            }
        });

        if (rowErrors.get() == 0) {
            Date registerDate = Calendar.getInstance().getTime();

            //VALIDATE NUMERICAL VALUES
            data.remove("works");
            WeeklyReportDto weeklyReport = getExtendedWeeklyReport(data);
            List<WeeklyWorkDto> oldWorks = weeklyReport.getWorks();
            List<AbsenceRequestDto> absences = weeklyReport.getAbsences();
            List<LocalBankHolidayDto> localBankHolidays = weeklyReport.getLocalBankHolidays();

            HashMap summary = new HashMap();
            List<String> days = Arrays.asList(new String[]{"monday", "tuesday", "wednesday", "thursday", "friday"});
            works.forEach(work -> {
                days.forEach(d -> {
                    summary.put(d, !summary.containsKey(d) ? Double.parseDouble(String.valueOf(work.get(d))) : ((double) summary.get(d) + Double.parseDouble(String.valueOf(work.get(d)))));
                });
            });

            boolean valid = validateNumericalValues(year, week, summary, absences, localBankHolidays);

            if (valid) {
                works.forEach(work -> {
                    WeeklyWorkDto temp = new WeeklyWorkDto();
                    if (work.containsKey("id")) {
                        Optional<WeeklyWorkDto> oldFound = oldWorks.stream().filter(o -> o.getId().equalsIgnoreCase((String) work.get("id"))).findFirst();
                        temp = oldFound.isPresent() ? oldFound.get() : temp;
                    } else {
                        temp.setRegisterDate(registerDate);
                    }
                    temp.setYear(year);
                    temp.setWeek(week);

                    temp.setDeliverable(String.valueOf(work.get("deliverable")));
                    temp.setMonday(Double.parseDouble(String.valueOf(work.get("monday"))));
                    temp.setTuesday(Double.parseDouble(String.valueOf(work.get("tuesday"))));
                    temp.setWednesday(Double.parseDouble(String.valueOf(work.get("wednesday"))));
                    temp.setThursday(Double.parseDouble(String.valueOf(work.get("thursday"))));
                    temp.setFriday(Double.parseDouble(String.valueOf(work.get("friday"))));

                    WeeklyWork weeklyWork = weeklyWorkMapper.dtoToModel(temp);
                    //set Staff object
                    Staff staff = staffRepository.findAllByStaffId(employeeId);
                    if (staff != null) {
                        weeklyWork.setStaff(staff);
                    }
                    //set Customer object
                    Client customer = customerRepository.findBy_id(String.valueOf(work.get("customerId")));
                    if (customer != null) {
                        weeklyWork.setCustomer(customer);
                    }
                    //set Operation object
                    CommercialOperation operation = operationRepository.findBy_id(String.valueOf(work.get("operationId")));
                    if (operation != null) {
                        weeklyWork.setOperation(operation);
                    }
                    //set AssignmentType object
                    AssignmentType assignmentType = assignmentTypeRepository.findAssignmentTypeBy_id(String.valueOf(work.get("assignmentTypeId")));
                    if (assignmentType != null) {
                        weeklyWork.setAssignmentType(assignmentType);
                    }
                    weeklyWorkRepository.save(weeklyWork);
                });

                oldWorks.forEach(old -> {
                    Optional<HashMap> workFound = works.stream().filter(o -> String.valueOf(o.get("id")).equalsIgnoreCase(old.getId())).findFirst();
                    if (!workFound.isPresent()) {
                        weeklyWorkRepository.deleteById(old.getId());
                    }
                });

            } else {
                throw exception(ERRORS_IN_JOURNAL_VALUES); //Sum of values for each day must be equal to 0 or 1
            }
        } else {
            throw exception(EMPTY_MANDATORY_FIELDS); //There are empty mandatory fields
        }
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.WeeklyReport, exceptionType, args);
    }


}
