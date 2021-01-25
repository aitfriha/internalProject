package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.TravelRequestEmailAddressMapper;
import org.techniu.isbackend.dto.model.TravelRequestEmailAddressDto;
import org.techniu.isbackend.entity.TravelRequestEmailAddress;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.TravelRequestEmailAddressRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;
import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;


@Service
public class TravelRequestEmailAddressServiceImpl implements TravelRequestEmailAddressService {

    private TravelRequestEmailAddressRepository travelRequestEmailAddressRepository;
    private final TravelRequestEmailAddressMapper travelRequestEmailAddressMapper = Mappers.getMapper(TravelRequestEmailAddressMapper.class);

    public TravelRequestEmailAddressServiceImpl(TravelRequestEmailAddressRepository travelRequestEmailAddressRepository) {
        this.travelRequestEmailAddressRepository = travelRequestEmailAddressRepository;
    }

    @Override
    public List<TravelRequestEmailAddressDto> getAllEmailAddresses() {
        List<TravelRequestEmailAddressDto> result = new ArrayList<>();
        List<TravelRequestEmailAddress> list = travelRequestEmailAddressRepository.findAll();
        list.forEach(emailAddress -> {
            TravelRequestEmailAddressDto emailAddressDto = travelRequestEmailAddressMapper.modelToDto(emailAddress);
            result.add(emailAddressDto);
        });
        return result;
    }

    @Override
    public void saveEmailAddress(TravelRequestEmailAddressDto emailAddressDto) {
        TravelRequestEmailAddress obj = travelRequestEmailAddressRepository.findEmailAddressByEmail(emailAddressDto.getEmail());
        if (obj != null) {
            throw exception(DUPLICATE_ENTITY);
        } else {
            travelRequestEmailAddressRepository.save(travelRequestEmailAddressMapper.dtoToModel(emailAddressDto));
        }
    }


    @Override
    public void updateEmailAddress(TravelRequestEmailAddressDto emailAddressDto) {
        TravelRequestEmailAddress obj = null;
        // Find email address by id
        Optional<TravelRequestEmailAddress> emailAddress = travelRequestEmailAddressRepository.findById(emailAddressDto.getId());
        if (emailAddress.isPresent()) {
            obj = travelRequestEmailAddressRepository.findEmailAddressByEmail(emailAddressDto.getEmail());
            if (obj != null && !obj.get_id().equalsIgnoreCase(emailAddressDto.getId())) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                // Get email address model
                TravelRequestEmailAddress emailAddressModel = emailAddress.get();
                emailAddressModel.setEmail(emailAddressDto.getEmail());
                // Update email address data
                travelRequestEmailAddressRepository.save(emailAddressModel);
            }
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void deleteEmailAddress(String id) {
        Optional<TravelRequestEmailAddress> emailAddress = travelRequestEmailAddressRepository.findById(id);
        if (emailAddress.isPresent()) {
            TravelRequestEmailAddress object = emailAddress.get();
            travelRequestEmailAddressRepository.delete(object);
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
