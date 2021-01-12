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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {
    private CurrencyRepository contractStatusRepository;
    private final CurrencyMapper currencyMapper = Mappers.getMapper(CurrencyMapper.class);
    private TypeOfCurrencyRepository typeOfCurrencyRepository;


    public CurrencyServiceImpl(CurrencyRepository contractStatusRepository, TypeOfCurrencyRepository typeOfCurrencyRepository) {
        this.contractStatusRepository = contractStatusRepository;
        this.typeOfCurrencyRepository = typeOfCurrencyRepository;
    }

    @Override
    public void saveCurrency(CurrencyDto currencyDto) {
        contractStatusRepository.save(currencyMapper.dtoToModel(currencyDto));
    }

    @Override
    public List<CurrencyDto> getAllCurrency() {
        // Get all actions
        List<Currency> contractStatus = contractStatusRepository.findAll();
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
        return contractStatusRepository.findAllBy_id(id);
    }

    @Override
    public List<CurrencyDto> updateCurrency(CurrencyDto currencyDto, String id) {
        // save country if note existe
        Currency currency = getById(id);
        Optional<Currency> cs = Optional.ofNullable(contractStatusRepository.findAllBy_id(id));

        if (!cs.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        TypeOfCurrency typeOfCurrency = typeOfCurrencyRepository.findAllBy_id(currencyDto.getTypeOfCurrency().get_id());

        currency.setTypeOfCurrency(typeOfCurrency);
        currency.setChangeFactor(currencyDto.getChangeFactor());
        currency.setYear(currencyDto.getYear());
        currency.setMonth(currencyDto.getMonth());

        // System.out.println("new :" + currency);
        contractStatusRepository.save(currency);
        return getAllCurrency();
    }

    @Override
    public List<CurrencyDto> remove(String id) {
        Optional<Currency> action = Optional.ofNullable(contractStatusRepository.findAllBy_id(id));
        // If Currency doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        contractStatusRepository.deleteById(id);
        return getAllCurrency();
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
