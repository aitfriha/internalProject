package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.CurrencyMapper;
import org.techniu.isbackend.dto.model.CurrencyDto;
import org.techniu.isbackend.entity.Currency;
import org.techniu.isbackend.entity.TypeOfCurrency;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.CurrencyRepository;
import org.techniu.isbackend.repository.StateCountryRepository;
import org.techniu.isbackend.repository.TypeOfCurrencyRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;
import static org.techniu.isbackend.exception.ExceptionType.NOT_ASSOCIATED_DATA;

@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {
    private CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper = Mappers.getMapper(CurrencyMapper.class);
    private TypeOfCurrencyRepository typeOfCurrencyRepository;


    public CurrencyServiceImpl(CurrencyRepository currencyRepository, TypeOfCurrencyRepository typeOfCurrencyRepository) {
        this.currencyRepository = currencyRepository;
        this.typeOfCurrencyRepository = typeOfCurrencyRepository;
    }

    @Override
    public void saveCurrency(CurrencyDto currencyDto) {
        currencyRepository.save(currencyMapper.dtoToModel(currencyDto));
    }

    @Override
    public List<CurrencyDto> getAllCurrency() {
        // Get all actions
        List<Currency> contractStatus = currencyRepository.findAll();
        // Create a list of all actions dto
        ArrayList<CurrencyDto> currencyDtos = new ArrayList<>();

        for (Currency contractStatus1 : contractStatus) {
            CurrencyDto currencyDto = currencyMapper.modelToDto(contractStatus1);
            currencyDtos.add(currencyDto);
        }
        return currencyDtos;
    }

    @Override
    public Currency getById(String id) {
        return currencyRepository.findAllBy_id(id);
    }

    @Override
    public List<CurrencyDto> updateCurrency(CurrencyDto currencyDto, String id) {
        // save country if note existe
        Currency currency = getById(id);
        Optional<Currency> cs = Optional.ofNullable(currencyRepository.findAllBy_id(id));

        if (!cs.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        TypeOfCurrency typeOfCurrency = typeOfCurrencyRepository.findAllBy_id(currencyDto.getTypeOfCurrency().get_id());

        currency.setTypeOfCurrency(typeOfCurrency);
        currency.setChangeFactor(currencyDto.getChangeFactor());
        currency.setYear(currencyDto.getYear());
        currency.setMonth(currencyDto.getMonth());

        // System.out.println("new :" + currency);
        currencyRepository.save(currency);
        return getAllCurrency();
    }

    @Override
    public List<CurrencyDto> remove(String id) {
        Optional<Currency> action = Optional.ofNullable(currencyRepository.findAllBy_id(id));
        // If Currency doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        currencyRepository.deleteById(id);
        return getAllCurrency();
    }

    @Override
    public CurrencyDto getLastDataByCurrencyType(String currencyTypeId) {
        TypeOfCurrency currencyType = typeOfCurrencyRepository.findAllBy_id(currencyTypeId);
        if (currencyType != null) {
            List<Currency> list = currencyRepository.findAllByTypeOfCurrency(currencyType);
            if (!list.isEmpty()) {
                Collections.sort(list, Comparator.comparing(Currency::getMonth).reversed());
                Collections.sort(list, Comparator.comparing(Currency::getYear).reversed());
                CurrencyDto currencyDto = currencyMapper.modelToDto(list.get(0));
                return currencyDto;
            } else {
                throw exception(NOT_ASSOCIATED_DATA);
            }
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public List<CurrencyDto> getLastDataAssociatedWithCurrencyTypes() {
        List<CurrencyDto> listDto = new ArrayList<>();
        List<TypeOfCurrency> types = typeOfCurrencyRepository.findAll();
        types.forEach(currencyType -> {
            List<Currency> list = currencyRepository.findAllByTypeOfCurrency(currencyType);
            if (!list.isEmpty()) {
                Collections.sort(list, Comparator.comparing(Currency::getMonth).reversed());
                Collections.sort(list, Comparator.comparing(Currency::getYear).reversed());
                Currency temp = list.get(0);
                temp.setChangeFactor(temp.getChangeFactor());
                CurrencyDto currencyDto = currencyMapper.modelToDto(temp);
                listDto.add(currencyDto);
            }
        });
        return listDto;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Currency, exceptionType, args);
    }
}
