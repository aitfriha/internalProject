package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.dto.model.JourneyDto;
import org.techniu.isbackend.entity.Journey;

@Mapper(componentModel = "spring")
public interface JourneyMapper {
    /**
     * Map dto to model
     *
     * @param journeyDto
     * @return Journey
     */
    @Mapping(source = "fromCountryId", target = "fromCountry.countryId")
    @Mapping(source = "fromCountryCode", target = "fromCountry.countryCode")
    @Mapping(source = "fromCountryName", target = "fromCountry.countryName")
    @Mapping(source = "fromStateId", target = "fromState._id")
    @Mapping(source = "fromStateName", target = "fromState.stateName")
    @Mapping(source = "fromCityId", target = "fromCity._id")
    @Mapping(source = "fromCityName", target = "fromCity.cityName")

    @Mapping(source = "toCountryId", target = "toCountry.countryId")
    @Mapping(source = "toCountryCode", target = "toCountry.countryCode")
    @Mapping(source = "toCountryName", target = "toCountry.countryName")
    @Mapping(source = "toStateId", target = "toState._id")
    @Mapping(source = "toStateName", target = "toState.stateName")
    @Mapping(source = "toCityId", target = "toCity._id")
    @Mapping(source = "toCityName", target = "toCity.cityName")
    Journey dtoToModel(JourneyDto journeyDto);

    /**
     * Map model to dto
     *
     * @param journey
     * @return JourneyDto
     */
    @Mapping(source = "fromCountry.countryId", target = "fromCountryId")
    @Mapping(source = "fromCountry.countryCode", target = "fromCountryCode")
    @Mapping(source = "fromCountry.countryName", target = "fromCountryName")
    @Mapping(source = "fromState._id", target = "fromStateId")
    @Mapping(source = "fromState.stateName", target = "fromStateName")
    @Mapping(source = "fromCity._id", target = "fromCityId")
    @Mapping(source = "fromCity.cityName", target = "fromCityName")

    @Mapping(source = "toCountry.countryId", target = "toCountryId")
    @Mapping(source = "toCountry.countryCode", target = "toCountryCode")
    @Mapping(source = "toCountry.countryName", target = "toCountryName")
    @Mapping(source = "toState._id", target = "toStateId")
    @Mapping(source = "toState.stateName", target = "toStateName")
    @Mapping(source = "toCity._id", target = "toCityId")
    @Mapping(source = "toCity.cityName", target = "toCityName")
    JourneyDto modelToDto(Journey journey);
}
