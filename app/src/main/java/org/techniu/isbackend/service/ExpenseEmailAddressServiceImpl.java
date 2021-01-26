package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.ExpenseEmailAddressMapper;
import org.techniu.isbackend.dto.model.ExpenseEmailAddressDto;
import org.techniu.isbackend.entity.ExpenseEmailAddress;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.ExpenseEmailAddressRepository;

import java.util.*;

import static org.techniu.isbackend.exception.ExceptionType.*;


@Service
public class ExpenseEmailAddressServiceImpl implements ExpenseEmailAddressService {

    private ExpenseEmailAddressRepository expenseEmailAddressRepository;
    private final ExpenseEmailAddressMapper expenseEmailAddressMapper = Mappers.getMapper(ExpenseEmailAddressMapper.class);

    public ExpenseEmailAddressServiceImpl(ExpenseEmailAddressRepository expenseEmailAddressRepository) {
        this.expenseEmailAddressRepository = expenseEmailAddressRepository;
    }

    @Override
    public List<ExpenseEmailAddressDto> getAllEmailAddresses() {
        List<ExpenseEmailAddressDto> result = new ArrayList<>();
        List<ExpenseEmailAddress> list = expenseEmailAddressRepository.findAll();
        Collections.sort(list, Comparator.comparing(ExpenseEmailAddress::getAction));
        list.forEach(emailAddress -> {
            ExpenseEmailAddressDto emailAddressDto = expenseEmailAddressMapper.modelToDto(emailAddress);
            result.add(emailAddressDto);
        });
        return result;
    }

    @Override
    public void saveEmailAddress(ExpenseEmailAddressDto emailAddressDto) {
        ExpenseEmailAddress obj = null;
        obj = expenseEmailAddressRepository.findEmailAddressByAction(emailAddressDto.getAction());
        if (obj != null) {
            throw exception(DUPLICATE_ACTION);
        } else {
            obj = expenseEmailAddressRepository.findEmailAddressByActionAndEmail(emailAddressDto.getAction(), emailAddressDto.getEmail());
            if (obj != null) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                expenseEmailAddressRepository.save(expenseEmailAddressMapper.dtoToModel(emailAddressDto));
            }
        }
    }


    @Override
    public void updateEmailAddress(ExpenseEmailAddressDto emailAddressDto) {
        ExpenseEmailAddress obj = null;
        // Find email address by id
        Optional<ExpenseEmailAddress> emailAddress = expenseEmailAddressRepository.findById(emailAddressDto.getId());
        if (emailAddress.isPresent()) {
            obj = expenseEmailAddressRepository.findEmailAddressByAction(emailAddressDto.getAction());
            if (obj != null && !obj.get_id().equalsIgnoreCase(emailAddressDto.getId())) {
                throw exception(DUPLICATE_ACTION);
            } else {
                obj = expenseEmailAddressRepository.findEmailAddressByActionAndEmail(emailAddressDto.getAction(), emailAddressDto.getEmail());
                if (obj != null && !obj.get_id().equalsIgnoreCase(emailAddressDto.getId())) {
                    throw exception(DUPLICATE_ENTITY);
                } else {
                    // Get email address model
                    ExpenseEmailAddress emailAddressModel = emailAddress.get();
                    emailAddressModel.setAction(emailAddressDto.getAction());
                    emailAddressModel.setEmail(emailAddressDto.getEmail());
                    // Update email address data
                    expenseEmailAddressRepository.save(emailAddressModel);
                }
            }
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void deleteEmailAddress(String id) {
        Optional<ExpenseEmailAddress> emailAddress = expenseEmailAddressRepository.findById(id);
        if (emailAddress.isPresent()) {
            ExpenseEmailAddress object = emailAddress.get();
            expenseEmailAddressRepository.delete(object);
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.EmailAddress, exceptionType, args);
    }

}
