package org.techniu.isbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.entity.ClassType;
import org.techniu.isbackend.entity.Country;
import org.techniu.isbackend.entity.Log;
import org.techniu.isbackend.entity.LogType;
import org.techniu.isbackend.repository.CountryRepository;
import org.techniu.isbackend.service.utilities.StringUtility;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CountryServiceImpl implements CountryService {
    private CountryRepository countryRepository;
    private LogService logService;
    CountryServiceImpl(CountryRepository countryRepository, LogService logService){
        this.countryRepository = countryRepository;
        this.logService = logService;
    }
    @Override
    public Country saveCountry(Country country) {
        Log log = new Log();
        log.setUserName("John Doe");
       // log.setActionDate(new Date());
        log.setClassType(ClassType.COUNTRY);
        log.setLogType(LogType.CREATE);
        String[] countryNames = country.getCountryName().split(" ");
        StringBuilder countryName = new StringBuilder();
        for (String name:countryNames){
            String captName = StringUtility.capitalize(name);
            countryName.append(" ").append(captName);

        };
        country.setCountryName(countryName.toString().trim());
        Country country1 = countryRepository.save(country);
        Log l = logService.saveLog(log);
        System.out.println(l.toString());
        return country1;
    }

    @Override
    public Country updateCountry(String countryId, Country country) {
        return countryRepository.findById(countryId).map(country1 -> {
            Log log = new Log();
            log.setUserName("John Doe");
            //log.setActionDate(new Date());
            log.setClassType(ClassType.COUNTRY);
            log.setLogType(LogType.UPDATE);
            country.setCountryId(country1.getCountryId());
            country.setCountryName(StringUtility.capitalize(country.getCountryName()));
            Country country11 = countryRepository.save(country);;
            logService.saveLog(log);
            return country11;
        }).orElseThrow(() -> new ExceptionMessage("Cannot update the country!!"));
    }

    @Override
    public Boolean getCountryByName(String countryName){
        String[] country = countryName.split("-");
        String name = String.join(" ", country);
        Country country1 = countryRepository.getByCountryName(name);
        return country1 != null;
    }

    @Override
    public ResponseEntity<?> deleteCountry(String countryId) {
        return countryRepository.findById(countryId).map(country -> {
            countryRepository.delete(country);
            Log log = new Log();
            log.setUserName("John Doe");
            //log.setActionDate(new Date());
            log.setClassType(ClassType.COUNTRY);
            log.setLogType(LogType.DELETE);
            logService.saveLog(log);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ExceptionMessage("Connot delete the country"));
    }

    @Override
    public List<Country> getAllCountry() {
        return countryRepository.findAll();
    }
}
