package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.TypeOfCurrencyMapper;
import org.techniu.isbackend.dto.model.TypeOfCurrencyDto;
import org.techniu.isbackend.entity.TypeOfCurrency;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.TypeOfCurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class TypeOfCurrencyServiceImpl implements TypeOfCurrencyService {
    private TypeOfCurrencyRepository typeOfCurrencyRepository;
    private final TypeOfCurrencyMapper typeOfCurrencyMapper = Mappers.getMapper(TypeOfCurrencyMapper.class);

    public TypeOfCurrencyServiceImpl(TypeOfCurrencyRepository typeOfCurrencyRepository) {
        this.typeOfCurrencyRepository = typeOfCurrencyRepository;
    }

    @Override
    public void saveTypeOfCurrency(TypeOfCurrencyDto typeOfCurrencyDto) {
        System.out.println("Implement part :" + typeOfCurrencyDto);
        typeOfCurrencyRepository.save(typeOfCurrencyMapper.dtoToModel(typeOfCurrencyDto));
    }

    @Override
    public List<TypeOfCurrencyDto> getAllTypeOfCurrency() {
        // Get all actions
        List<TypeOfCurrency> typeOfCurrency = typeOfCurrencyRepository.findAll();
        // Create a list of all actions dto
        ArrayList<TypeOfCurrencyDto> typeOfCurrencyDtos = new ArrayList<>();

        for (TypeOfCurrency typeOfCurrency1 : typeOfCurrency) {
            TypeOfCurrencyDto typeOfCurrencyDto = typeOfCurrencyMapper.modelToDto(typeOfCurrency1);
            typeOfCurrencyDtos.add(typeOfCurrencyDto);
        }
        return typeOfCurrencyDtos;
    }

    @Override
    public TypeOfCurrency getById(String id) {
        return typeOfCurrencyRepository.findAllBy_id(id);
    }

    @Override
    public List<TypeOfCurrencyDto> updateTypeOfCurrency(TypeOfCurrencyDto typeOfCurrencyDto, String id) {
        // save country if note existe
        TypeOfCurrency typeOfCurrency = getById(id);
        Optional<TypeOfCurrency> typeOfCurrency1 = Optional.ofNullable(typeOfCurrencyRepository.findAllBy_id(id));

        if (!typeOfCurrency1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        System.out.println(typeOfCurrency);

        typeOfCurrency.setCurrencyCode(typeOfCurrencyDto.getCurrencyCode());
        typeOfCurrency.setCurrencyName(typeOfCurrencyDto.getCurrencyName());

        System.out.println(typeOfCurrency);

        typeOfCurrencyRepository.save(typeOfCurrency);
        return getAllTypeOfCurrency();
    }

    @Override
    public List<TypeOfCurrencyDto> remove(String id) {
        Optional<TypeOfCurrency> action = Optional.ofNullable(typeOfCurrencyRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        typeOfCurrencyRepository.deleteById(id);
        return getAllTypeOfCurrency();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.ContractStatus, exceptionType, args);
    }
}
