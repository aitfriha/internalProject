package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.WeeklyReportDto;
import org.techniu.isbackend.entity.SummarizedWeeklyReport;

import java.util.HashMap;
import java.util.List;

public interface WeeklyReportService {

    /**
     * Get all Summarized Weekly Report By staffId
     *
     * @param data
     * @return List<SummarizedWeeklyReport>
     */
    List<SummarizedWeeklyReport> getAllSummarizedWeeklyReportByStaff(HashMap data);

    /**
     * Get weekly report dto
     *
     * @param data
     * @return WeeklyReportDto
     */
    WeeklyReportDto getExtendedWeeklyReport(HashMap data);


    /**
     * Save weeklyReport
     *
     * @param data
     */
    void saveWeeklyReport(HashMap data);

}
