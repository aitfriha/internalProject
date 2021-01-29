package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.ExternalSupplierMapper;
import org.techniu.isbackend.dto.model.ExternalSupplierDto;
import org.techniu.isbackend.entity.Address;
import org.techniu.isbackend.entity.City;
import org.techniu.isbackend.entity.ExternalSupplier;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.AddressRepository;
import org.techniu.isbackend.repository.CityRepository;
import org.techniu.isbackend.repository.ExternalSupplierRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class ExternalSupplierServiceImpl implements ExternalSupplierService {
    private ExternalSupplierRepository externalSupplierRepository;
    private CityRepository cityRepository;
    private AddressRepository addressRepository;
    private AddressService addressService;
    private final ExternalSupplierMapper externalSupplierMapper = Mappers.getMapper(ExternalSupplierMapper.class);

    public ExternalSupplierServiceImpl(ExternalSupplierRepository externalSupplierRepository, AddressService addressService,
                                       AddressRepository addressRepository, CityRepository cityRepository) {
        this.externalSupplierRepository = externalSupplierRepository;
        this.cityRepository = cityRepository ;
        this.addressRepository = addressRepository;
        this.addressService = addressService;
    }

    @Override
    public void saveExternalSupplier(ExternalSupplierDto externalSupplierDto) {
        externalSupplierRepository.save(externalSupplierMapper.dtoToModel(externalSupplierDto));
    }

    @Override
    public List<ExternalSupplierDto> getAllExternalSupplier() {
        // Get all actions
        List<ExternalSupplier> externalSupplier = externalSupplierRepository.findAll();
        // Create a list of all actions dto
        ArrayList<ExternalSupplierDto> externalSupplierDtos = new ArrayList<>();

        for (ExternalSupplier externalSupplier1 : externalSupplier) {
            ExternalSupplierDto externalSupplierDto = externalSupplierMapper.modelToDto(externalSupplier1);
            externalSupplierDtos.add(externalSupplierDto);
        }
        return externalSupplierDtos;
    }

    @Override
    public ExternalSupplier getById(String id) {
        return externalSupplierRepository.findAllBy_id(id);
    }

    @Override
    public List<ExternalSupplierDto> updateExternalSupplier(ExternalSupplierDto externalSupplierDto, String id) {

        ExternalSupplier externalSupplier = getById(id);
        Optional<ExternalSupplier> externalSupplier1 = Optional.ofNullable(externalSupplierRepository.findAllBy_id(id));

        if (!externalSupplier1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        City city = cityRepository.findCityBy_id(externalSupplierDto.getAddress().getCity().get_id());
        Address address = addressRepository.findAddressByAddressId(externalSupplierDto.getAddress().getAddressId());


        address.setFullAddress(externalSupplierDto.getAddress().getFullAddress());
        address.setPostCode(externalSupplierDto.getAddress().getPostCode());
        address.setCity(city);
        addressService.updateAddress(externalSupplierDto.getAddress().getAddressId(), address);

        externalSupplier.setAddress(address);
        externalSupplier.setCompanyName(externalSupplierDto.getCompanyName());
        externalSupplier.setCode(externalSupplierDto.getCode());
        externalSupplier.setTaxNumber(externalSupplierDto.getTaxNumber());
        externalSupplier.setFirstName(externalSupplierDto.getFirstName());
        externalSupplier.setFatherFamilyName(externalSupplierDto.getFatherFamilyName());
        externalSupplier.setMotherFamilyName(externalSupplierDto.getMotherFamilyName());
        externalSupplier.setEmail(externalSupplierDto.getEmail());
        externalSupplier.setUrl(externalSupplierDto.getUrl());

        externalSupplierRepository.save(externalSupplier);
        return getAllExternalSupplier();
    }

    @Override
    public List<ExternalSupplierDto> remove(String id) {
        Optional<ExternalSupplier> action = Optional.ofNullable(externalSupplierRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        externalSupplierRepository.deleteById(id);
        return getAllExternalSupplier();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.ExternalSupplier, exceptionType, args);
    }
}
