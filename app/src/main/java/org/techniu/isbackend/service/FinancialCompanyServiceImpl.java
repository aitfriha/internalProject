package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.FinancialCompanyMapper;
import org.techniu.isbackend.dto.model.FinancialCompanyDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.AddressRepository;
import org.techniu.isbackend.repository.CityRepository;
import org.techniu.isbackend.repository.FinancialCompanyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class FinancialCompanyServiceImpl implements FinancialCompanyService {
    private FinancialCompanyRepository financialCompanyRepository;
    private CityRepository cityRepository;
    private AddressRepository addressRepository;
    private AddressService addressService;
    private LogService logService;
    private final FinancialCompanyMapper financialCompanyMapper = Mappers.getMapper(FinancialCompanyMapper.class);

    public FinancialCompanyServiceImpl(FinancialCompanyRepository financialCompanyRepository, AddressService addressService,
                                       AddressRepository addressRepository, CityRepository cityRepository, LogService logService) {
        this.financialCompanyRepository = financialCompanyRepository;
        this.cityRepository = cityRepository ;
        this.addressRepository = addressRepository;
        this.addressService = addressService;
        this.logService = logService;
    }

    @Override
    public void saveFinancialCompany(FinancialCompanyDto financialCompanyDto) {
        financialCompanyRepository.save(financialCompanyMapper.dtoToModel(financialCompanyDto));
        logService.addLog(LogType.CREATE, ClassType.COMPANY,"create company "+financialCompanyDto.getName());
    }

    @Override
    public List<FinancialCompanyDto> getAllFinancialCompany() {
        // Get all actions
        List<FinancialCompany> financialCompany = financialCompanyRepository.findAll();
        // Create a list of all actions dto
        ArrayList<FinancialCompanyDto> financialCompanyDtos = new ArrayList<>();

        for (FinancialCompany financialCompany1 : financialCompany) {
            FinancialCompanyDto financialCompanyDto = financialCompanyMapper.modelToDto(financialCompany1);
            financialCompanyDtos.add(financialCompanyDto);
        }
        return financialCompanyDtos;
    }

    @Override
    public FinancialCompany getById(String id) {
        return financialCompanyRepository.findAllBy_id(id);
    }

    @Override
    public List<FinancialCompanyDto> updateFinancialCompany(FinancialCompanyDto financialCompanyDto, String id) {

        FinancialCompany financialCompany = getById(id);
        Optional<FinancialCompany> financialCompany1 = Optional.ofNullable(financialCompanyRepository.findAllBy_id(id));

        if (!financialCompany1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        City city = cityRepository.findCityBy_id(financialCompanyDto.getAddress().getCity().get_id());
        Address address = addressRepository.findAddressByAddressId(financialCompanyDto.getAddress().getAddressId());


        address.setFullAddress(financialCompanyDto.getAddress().getFullAddress());
        address.setPostCode(financialCompanyDto.getAddress().getPostCode());
        address.setCity(city);
        addressService.updateAddress(financialCompanyDto.getAddress().getAddressId(), address);

        financialCompany.setAddress(address);
        financialCompany.setName(financialCompanyDto.getName());
        financialCompany.setCode(financialCompanyDto.getCode());
        financialCompany.setTaxNumber(financialCompanyDto.getTaxNumber());
        financialCompany.setPhone2(financialCompanyDto.getPhone2());
        financialCompany.setPhone1(financialCompanyDto.getPhone1());
        financialCompany.setEmail(financialCompanyDto.getEmail());
        financialCompany.setLogo(financialCompanyDto.getLogo());

        financialCompanyRepository.save(financialCompany);
        logService.addLog(LogType.UPDATE, ClassType.COMPANY,"update company "+financialCompany.getName());
        return getAllFinancialCompany();
    }

    @Override
    public List<FinancialCompanyDto> remove(String id) {
        Optional<FinancialCompany> action = Optional.ofNullable(financialCompanyRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        financialCompanyRepository.deleteById(id);
        logService.addLog(LogType.DELETE, ClassType.COMPANY,"delete company "+action.get().getName());
        return getAllFinancialCompany();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.FinancialCompany, exceptionType, args);
    }
}
