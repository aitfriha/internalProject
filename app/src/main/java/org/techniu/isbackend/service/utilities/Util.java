package org.techniu.isbackend.service.utilities;

import org.techniu.isbackend.dto.model.AbsenceRequestDto;
import org.techniu.isbackend.dto.model.LocalBankHolidayDto;
import org.techniu.isbackend.entity.AbsenceRequest;
import org.techniu.isbackend.entity.LocalBankHoliday;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Util {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static HashMap getStartAndEndOfWeek(int year, int weekOfYear) {
        HashMap result = new HashMap();

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear + 1);
        calendar.set(Calendar.YEAR, year);

        result.put("start", calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        result.put("end", calendar.getTime());

        return result;
    }

    public static double getTotalDays(Object obj, Date startDate, Date endDate) {
        double totalDays = 0;
        try {
            Date objectStartDate = format.parse(obj instanceof AbsenceRequest ? ((AbsenceRequest) obj).getStartDate() : ((LocalBankHoliday) obj).getStartDate());
            Date objectEndDate = format.parse(obj instanceof AbsenceRequest ? ((AbsenceRequest) obj).getEndDate() : ((LocalBankHoliday) obj).getEndDate());

            boolean validation = obj instanceof AbsenceRequest ? ((AbsenceRequest) obj).getStartDate().equalsIgnoreCase(((AbsenceRequest) obj).getEndDate()) : ((LocalBankHoliday) obj).getStartDate().equalsIgnoreCase(((LocalBankHoliday) obj).getEndDate());

            if (validation) {
                totalDays = Double.parseDouble(obj instanceof AbsenceRequest ? ((AbsenceRequest) obj).getHourRate() : ((LocalBankHoliday) obj).getTotalDays());
            } else {
                boolean startDateComparison = objectStartDate.compareTo(startDate) >= 0 && objectStartDate.compareTo(endDate) <= 0;
                boolean endDateComparison = objectEndDate.compareTo(startDate) >= 0 && objectEndDate.compareTo(endDate) <= 0;
                if (startDateComparison && endDateComparison) {
                    totalDays = Long.valueOf(ChronoUnit.DAYS.between(asLocalDate(objectStartDate), asLocalDate(objectEndDate))).doubleValue();
                } else if (startDateComparison) {
                    totalDays = Long.valueOf(ChronoUnit.DAYS.between(asLocalDate(objectStartDate), asLocalDate(endDate))).doubleValue();
                } else if (endDateComparison) {
                    totalDays = Long.valueOf(ChronoUnit.DAYS.between(asLocalDate(startDate), asLocalDate(objectEndDate))).doubleValue();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return totalDays;
    }

    public static double getAbsenceValueForSpecificDay(List<Object> list, Date date) {
        double value = 0;
        for (Object obj : list) {
            try {
                boolean validation = obj instanceof AbsenceRequest ? ((AbsenceRequest)obj).getStartDate().equalsIgnoreCase(((AbsenceRequest)obj).getEndDate()) : ((LocalBankHoliday)obj).getStartDate().equalsIgnoreCase(((LocalBankHoliday)obj).getEndDate());
                if (validation) {
                    if (format.parse(obj instanceof AbsenceRequest ? ((AbsenceRequest)obj).getStartDate() : ((LocalBankHoliday)obj).getStartDate()).compareTo(date) == 0) {
                        value = obj instanceof AbsenceRequest ? Double.parseDouble(((AbsenceRequest)obj).getHourRate()) : 1.0;
                        break;
                    }
                } else {
                    value = 1;
                    Date sDate = format.parse(obj instanceof AbsenceRequest ? ((AbsenceRequest)obj).getStartDate() : ((LocalBankHoliday)obj).getStartDate());
                    Date eDate = format.parse(obj instanceof AbsenceRequest ? ((AbsenceRequest)obj).getEndDate() : ((LocalBankHoliday)obj).getEndDate());
                    int numberOfDays = Long.valueOf(ChronoUnit.DAYS.between(asLocalDate(sDate), asLocalDate(eDate))).intValue();
                    if (numberOfDays == 2) {
                        if(sDate.compareTo(date) == 0 || eDate.compareTo(date) == 0){
                             break;
                        }
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(sDate);
                        for (int i = 1; i < numberOfDays - 1; i++) {
                              calendar.add(Calendar.DAY_OF_MONTH, 1);
                              if(calendar.getTime().compareTo(date) == 0){
                                  break;
                              }
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static List<AbsenceRequest> filterAbsenceRequestsList(List<AbsenceRequest> list, Date startDate, Date endDate) {
        List<AbsenceRequest> result = new ArrayList<>();
        list.forEach(absenceRequest -> {
            try {
                Date absenceStartDate = format.parse(absenceRequest.getStartDate());
                Date absenceEndDate = format.parse(absenceRequest.getEndDate());
                boolean startDateComparison = absenceStartDate.compareTo(startDate) >= 0 && absenceStartDate.compareTo(endDate) <= 0;
                boolean endDateComparison = absenceEndDate.compareTo(startDate) >= 0 && absenceEndDate.compareTo(endDate) <= 0;
                if (startDateComparison || endDateComparison) {
                    result.add(absenceRequest);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return result;
    }


    public static List<LocalBankHoliday> filterLocalBankHolidaysList(List<LocalBankHoliday> list, Date startDate, Date endDate) {
        List<LocalBankHoliday> result = new ArrayList<>();
        list.forEach(localBankHoliday -> {
            try {
                Date lbhStartDate = format.parse(localBankHoliday.getStartDate());
                Date lbhEndDate = format.parse(localBankHoliday.getEndDate());
                boolean startDateComparison = lbhStartDate.compareTo(startDate) >= 0 && lbhStartDate.compareTo(endDate) <= 0;
                boolean endDateComparison = lbhEndDate.compareTo(startDate) >= 0 && lbhEndDate.compareTo(endDate) <= 0;
                if (startDateComparison || endDateComparison) {
                    result.add(localBankHoliday);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return result;
    }


    public static Date getDate(int year, int weekOfYear, String dayOfWeek) {
        Date result = null;
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        calendar.set(Calendar.YEAR, year);

        switch (dayOfWeek) {
            case "monday": {
                calendar.set(Calendar.DAY_OF_WEEK, 2);
                break;
            }
            case "tuesday": {
                calendar.set(Calendar.DAY_OF_WEEK, 3);
                break;
            }
            case "wednesday": {
                calendar.set(Calendar.DAY_OF_WEEK, 4);
                break;
            }
            case "thursday": {
                calendar.set(Calendar.DAY_OF_WEEK, 5);
                break;
            }
            case "friday": {
                calendar.set(Calendar.DAY_OF_WEEK, 6);
                break;
            }
            default: {
                break;
            }
        }
        result = calendar.getTime();
        return result;
    }


    public static boolean validateNumericalValues(int year, int week, HashMap summary, List<AbsenceRequestDto> absences, List<LocalBankHolidayDto> localBankHolidays) {
        List<Object> absencesObjects = new ArrayList<>();
        absences.forEach(absenceRequestDto -> {
            absencesObjects.add(absenceRequestDto);
        });
        List<Object> lbhObjects = new ArrayList<>();
        localBankHolidays.forEach(localBankHolidayDto -> {
            lbhObjects.add(localBankHolidayDto);
        });
        AtomicBoolean valid = new AtomicBoolean(true);
        summary.forEach((k, v) -> {
            double sum = Double.parseDouble(String.valueOf(v));
            Date kDate = getDate(year, week, String.valueOf(k));


            double absenceValue = getAbsenceValueForSpecificDay(absencesObjects, kDate);
            sum += absenceValue;

            double lbhValue = getAbsenceValueForSpecificDay(lbhObjects, kDate);
            sum += lbhValue;

            if (sum >= 0) {
                if (sum > 0 && sum != 1) {
                    valid.set(false);
                }
            } else {
                valid.set(false);
            }
        });
        return valid.get();
    }

    public static boolean validateRowData(HashMap data) {
        boolean valid = true;
        if ((!data.containsKey("customerId") || String.valueOf(data.get("customerId")).trim().isEmpty())
                || (!data.containsKey("operationId") || String.valueOf(data.get("operationId")).trim().isEmpty())
                || (!data.containsKey("assignmentTypeId") || String.valueOf(data.get("assignmentTypeId")).trim().isEmpty())
                || (!data.containsKey("deliverable") || String.valueOf(data.get("deliverable")).trim().isEmpty())
                || String.valueOf(data.get("monday")).trim().isEmpty()
                || String.valueOf(data.get("tuesday")).trim().isEmpty()
                || String.valueOf(data.get("wednesday")).trim().isEmpty()
                || String.valueOf(data.get("thursday")).trim().isEmpty()
                || String.valueOf(data.get("friday")).trim().isEmpty()) {
            valid = false;
        }
        return valid;
    }

    public static LocalDate asLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getMonthInLetter(int month) {
        String[] monthNames = new String[]{"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    public static void deleteTemporalFilesOnServer() {
        final String temporalDocumentsDirectory = "TEMPORAL DOCUMENTS";
        Stream.of(new File(temporalDocumentsDirectory).listFiles()).forEach(file -> {
            file.delete();
        });
    }


    public static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
