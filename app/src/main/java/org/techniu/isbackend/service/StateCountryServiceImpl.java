package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.CommercialOperationStatusMapper;
import org.techniu.isbackend.dto.mapper.StateCountryMapper;
import org.techniu.isbackend.dto.model.CityDto;
import org.techniu.isbackend.dto.model.CommercialOperationStatusDto;
import org.techniu.isbackend.dto.model.StateCountryDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.repository.StateCountryRepository;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class StateCountryServiceImpl implements StateCountryService {
    private StateCountryRepository stateCountryRepository;
    private CityService cityService;
    private final StateCountryMapper stateCountryMapper = Mappers.getMapper(StateCountryMapper.class);
    private LogService logService;
    StateCountryServiceImpl(StateCountryRepository stateCountryRepository, CityService cityService, LogService logService){
        this.stateCountryRepository = stateCountryRepository;
        this.cityService = cityService;
        this.logService = logService;
    }

    @Override
    public List<StateCountryDto> getAllState(String id) {
        // Get all actions
        List<StateCountry> stateCountries = stateCountryRepository.findAllByCountry_CountryId(id);
        // Create a list of all actions dto
        ArrayList<StateCountryDto> stateCountryDtos = new ArrayList<>();

        for (StateCountry stateCountry : stateCountries) {
            List<CityDto> cityList = cityService.getAllCitiesByState(stateCountry.get_id());
            StateCountryDto stateCountryDto=stateCountryMapper.modelToDto(stateCountry);
            stateCountryDto.setCities(cityList);
            stateCountryDtos.add(stateCountryDto);
        }
        return stateCountryDtos;
    }
}
