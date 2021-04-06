package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.CityMapper;
import org.techniu.isbackend.dto.model.CityDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.CityRepository;
import org.techniu.isbackend.repository.CountryRepository;
import org.techniu.isbackend.repository.StateCountryRepository;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
public class CityServiceImpl implements CityService{
    private CityRepository cityRepository;
    private CountryRepository countryRepository;
    private StateCountryRepository stateCountryRepository;
    private LogService logService;
    private final CityMapper cityMapper = Mappers.getMapper(CityMapper.class);
    CityServiceImpl(CityRepository cityRepository, StateCountryRepository stateCountryRepository, CountryRepository countryRepository, LogService logService) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.stateCountryRepository = stateCountryRepository;
        this.logService = logService;
    }
    @Override
    public City saveCity(Country country, StateCountry stateCountry, City city) {
        // save country if note existe
        Country country1;
        StateCountry stateCountry1;
        StateCountry city1;
        Optional<Country> country2 = Optional.ofNullable(countryRepository.getByCountryName(country.getCountryName()));
        if (country2.isPresent()) {
             country1= country2.get();
        }
        else {
             country1=countryRepository.save(country);
            logService.addLog(LogType.CREATE, ClassType.COUNTRY,"create country "+country.getCountryName());
        }
        // save state if note existe
        Optional<StateCountry> stateCountry2 = Optional.ofNullable(stateCountryRepository.getByStateName(stateCountry.getStateName()));
        if (stateCountry2.isPresent()) {
            stateCountry1= stateCountry2.get();
        }
        else {
            // save country if note existe
            stateCountry1=stateCountryRepository.save(stateCountry);
          //  logService.addLog(LogType.CREATE, ClassType.STATE,"create STATE "+stateCountry.getStateName());
        }
        StateCountry stateCountry3=stateCountryRepository.save(stateCountry1.setCountry(country1));
        logService.addLog(LogType.CREATE, ClassType.STATE,"create STATE "+stateCountry3.getStateName()+" for country "+stateCountry3.getCountry().getCountryName());
        // save state if note existe
        Optional<City> city2 = Optional.ofNullable(cityRepository.findCityByCityName(city.getCityName()));
        if (city2.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        logService.addLog(LogType.CREATE, ClassType.CITY,"create city "+city.getCityName()+" for state "+stateCountry3.getStateName());
        return cityRepository.save(city.setStateCountry(stateCountry3));

    }

    @Override
    public City updateCity(String cityId, City city) {
        return cityRepository.findById(cityId).map(city1 -> {
            city.set_id(city1.get_id());
            return cityRepository.save(city);
        }).orElseThrow(() -> new ExceptionMessage("Cannot update this city"));
    }

    @Override
    public ResponseEntity<?> deleteCity(String cityId) {
        return null;
    }

    @Override
    public List<CityDto> getAllCity() {
         List<City> cities = cityRepository.findAll();
        // Create a list of all staff dto
        ArrayList<CityDto> cityDtos = new ArrayList<>();
        for (City city : cities) {
            CityDto cityDto=new CityDto()
                    .setCountryName(city.getStateCountry().getCountry().getCountryName())
                    .setCountryCode(city.getStateCountry().getCountry().getCountryCode())
                    .setPhonePrefix(city.getStateCountry().getCountry().getPhonePrefix())
                    .setStateName(city.getStateCountry().getStateName())
                    .setCityName(city.getCityName());
            cityDtos.add(cityDto);
        }
        return cityDtos;
    }

    @Override
    public List<CityDto> getAllCitiesByState(String id) {
        // Get all actions
        List<City> citiesStates = cityRepository.findAllByStateCountry__id(id);
        // Create a list of all actions dto
        ArrayList<CityDto> citiesStateDtos = new ArrayList<>();

        for (City city : citiesStates) {
            CityDto cityDto=cityMapper.modelToDto(city);
            citiesStateDtos.add(cityDto);
        }
        return citiesStateDtos;
    }
    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.City, exceptionType, args);
    }
}
