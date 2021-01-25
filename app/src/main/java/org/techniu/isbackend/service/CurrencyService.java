package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.CurrencyDto;
import org.techniu.isbackend.entity.Currency;

import java.util.List;

public interface CurrencyService {
    void saveCurrency(CurrencyDto currencyDto);

    List<CurrencyDto> getAllCurrency();

    Currency getById(String id);

    List<CurrencyDto> updateCurrency(CurrencyDto currencyDto, String id);

    List<CurrencyDto> remove(String id);

    /**
     * Get currency data by currencyTypeId
     *
     * @return CurrencyDto
     */
    CurrencyDto getLastDataByCurrencyType(String currencyTypeId);

    /**
     * getDataAssociatedWithCurrencyTypes
     *
     * @return List<CurrencyDto>
     */
    List<CurrencyDto> getLastDataAssociatedWithCurrencyTypes();
}
