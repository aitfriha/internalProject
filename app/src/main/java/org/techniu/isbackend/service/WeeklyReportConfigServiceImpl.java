package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.WeeklyReportConfigMapper;
import org.techniu.isbackend.dto.model.WeeklyReportConfigDto;
import org.techniu.isbackend.entity.WeeklyReportConfig;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.WeeklyReportConfigRepository;

import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
public class WeeklyReportConfigServiceImpl implements WeeklyReportConfigService {

    @Autowired
    private WeeklyReportConfigRepository weeklyReportConfigRepository;

    private final WeeklyReportConfigMapper weeklyReportConfigMapper = Mappers.getMapper(WeeklyReportConfigMapper.class);

    @Override
    public WeeklyReportConfigDto getConfiguration() {
        WeeklyReportConfig configuration = weeklyReportConfigRepository.findConfigurationByRemovable(false);
        return weeklyReportConfigMapper.modelToDto(configuration);
    }

    @Override
    public WeeklyReportConfigDto getConfigurationById(String id) {
        Optional<WeeklyReportConfig> configuration = weeklyReportConfigRepository.findById(id);
        if (configuration.isPresent()) {
            return weeklyReportConfigMapper.modelToDto(configuration.get());
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void updateConfiguration(WeeklyReportConfigDto configurationDto) {
        WeeklyReportConfig configuration = null;
        Optional<WeeklyReportConfig> result = weeklyReportConfigRepository.findById(configurationDto.getId());
        if (result.isPresent()) {
              configuration = result.get();
              configuration.setNumberOfDays(configurationDto.getNumberOfDays());
              configuration.setEmployees(configurationDto.getEmployees());
        } else {
            configuration = new WeeklyReportConfig();
            configuration.setNumberOfDays(configurationDto.getNumberOfDays());
            configuration.setEmployees(configurationDto.getEmployees());
            configuration.setRemovable(false);
        }
        weeklyReportConfigRepository.save(configuration);
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.WeeklyReportConfig, exceptionType, args);
    }

}
