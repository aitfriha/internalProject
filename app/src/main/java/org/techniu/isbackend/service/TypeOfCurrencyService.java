package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.TypeOfCurrencyDto;
import org.techniu.isbackend.entity.TypeOfCurrency;

import java.util.List;

public interface TypeOfCurrencyService {
    void saveTypeOfCurrency(TypeOfCurrencyDto typeOfCurrencyDto);

    List<TypeOfCurrencyDto> getAllTypeOfCurrency();

    TypeOfCurrency getById(String id);

    List<TypeOfCurrencyDto> updateTypeOfCurrency(TypeOfCurrencyDto typeOfCurrencyDto, String id);

    List<TypeOfCurrencyDto> remove(String id);
}
