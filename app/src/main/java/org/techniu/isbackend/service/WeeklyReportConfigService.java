package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.WeeklyReportConfigDto;

public interface WeeklyReportConfigService {

    /**
     * Get Configuration
     *
     *
     * @return Configuration
     */
    WeeklyReportConfigDto getConfiguration();

    /**
     * Get Configuration By Id
     *
     * @param configurationId
     * @return Configuration
     */
    WeeklyReportConfigDto getConfigurationById(String configurationId);

    /**
     * Update configuration
     *
     * @param configurationDto
     */
    void updateConfiguration(WeeklyReportConfigDto configurationDto);

}
