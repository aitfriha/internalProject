package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.CurrencyDto;
import org.techniu.isbackend.entity.Currency;

import java.util.List;

public interface CurrencyService {
    void saveCurrency(CurrencyDto currencyDto);

    List<CurrencyDto> getAllCurrency();

    List<CurrencyDto> getFilteredCurrency();

    Currency getById(String id);

    List<CurrencyDto> updateCurrency(CurrencyDto currencyDto, String id);

    List<CurrencyDto> remove(String id);
}
