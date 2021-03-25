package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.CountryConfigMapper;
import org.techniu.isbackend.dto.model.CountryConfigDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.repository.CountryConfigRepository;
import org.techniu.isbackend.repository.CountryRepository;
import org.techniu.isbackend.repository.StaffRepository;

import java.util.Date;
import java.util.List;
@Service
@Transactional
public class CountryConfigServiceImpl implements CountryConfigService {
    private CountryConfigRepository countryConfigRepository;
    private CountryRepository countryRepository;
    private StaffRepository staffRepository;
    private final CountryConfigMapper countryConfigMapper = Mappers.getMapper(CountryConfigMapper.class);
    private LogService logService;
    CountryConfigServiceImpl(
            CountryConfigRepository countryConfigRepository,
            StaffRepository staffRepository,
            CountryRepository countryRepository, LogService logService){
        this.countryConfigRepository = countryConfigRepository;
        this.countryRepository = countryRepository;
        this.staffRepository = staffRepository;
        this.logService = logService;
    }
    @Override
    public void saveCountryConfig(CountryConfigDto countryConfigDto) {
        Country country = countryRepository.getByCountryId(countryConfigDto.getCountryId());
        Staff leader = staffRepository.findById(countryConfigDto.getLeader()).get();
        Log log = new Log();
        log.setUserName("John Doe");
      //  log.setActionDate(new Date());
        log.setClassType(ClassType.COUNTRYCONFIG);
        log.setLogType(LogType.CREATE);
        CountryConfig countryConfig = countryConfigMapper.dtoToModel(countryConfigDto);
        countryConfig.setCountry(country);
        countryConfig.setLeader(leader);
         countryConfigRepository.save(countryConfig);
    }

    @Override
    public CountryConfig updateCountryConfig(CountryConfig countryConfig) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteCountryConfig(String countryConfigId) {
        return null;
    }

    @Override
    public List<CountryConfig> getAllCountryConfig() {
        return countryConfigRepository.findAll();
    }

    @Override
    public CountryConfig getCountryConfigByCountry(String countryId) {
        return countryRepository.findById(countryId).map(country -> countryConfigRepository.getByCountry(country)).orElseThrow(() -> new ExceptionMessage("Cannot find country configuration"));

    }
}
