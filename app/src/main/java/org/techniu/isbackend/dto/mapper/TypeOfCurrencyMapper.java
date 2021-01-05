package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.TypeOfCurrencyAddrequest;
import org.techniu.isbackend.controller.request.TypeOfCurrencyUpdaterequest;
import org.techniu.isbackend.dto.model.TypeOfCurrencyDto;
import org.techniu.isbackend.entity.TypeOfCurrency;

@Mapper(componentModel = "spring")
public interface TypeOfCurrencyMapper {
    /**
     * Map dto to model
     *
     * @param typeOfCurrencyDto typeOfCurrencyDto
     * @return TypeOfCurrency
     */
    @Mapping(source = "typeOfCurrencyId", target="_id")
    TypeOfCurrency dtoToModel(TypeOfCurrencyDto typeOfCurrencyDto);

    /**
     * Map TypeOfCurrency to TypeOfCurrencyDo
     *
     * @param typeOfCurrencyAddrequest typeOfCurrencyAddrequest
     * @return TypeOfCurrencyDto
     */
    TypeOfCurrencyDto addRequestToDto(TypeOfCurrencyAddrequest typeOfCurrencyAddrequest);

    /**
     * Map TypeOfCurrency to TypeOfCurrencyDo
     *
     * @param typeOfCurrencyUpdaterequest typeOfCurrencyUpdaterequest
     * @return TypeOfCurrencyDto
     */
    TypeOfCurrencyDto updateRequestToDto(TypeOfCurrencyUpdaterequest typeOfCurrencyUpdaterequest);

    /**
     * Map typeOfCurrency to typeOfCurrencyDo
     *
     * @param typeOfCurrency typeOfCurrency
     * @return TypeOfCurrencyDto
     */
    @Mapping(source = "_id", target="typeOfCurrencyId")
    TypeOfCurrencyDto modelToDto(TypeOfCurrency typeOfCurrency);
}
