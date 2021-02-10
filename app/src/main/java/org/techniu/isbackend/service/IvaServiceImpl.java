package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.IvaMapper;
import org.techniu.isbackend.dto.model.ContractStatusDto;
import org.techniu.isbackend.dto.model.CurrencyDto;
import org.techniu.isbackend.dto.model.IvaDto;
import org.techniu.isbackend.entity.ContractStatus;
import org.techniu.isbackend.entity.Iva;
import org.techniu.isbackend.entity.StateCountry;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.IvaRepository;
import org.techniu.isbackend.repository.StateCountryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class IvaServiceImpl implements IvaService {
    private IvaRepository ivaRepository;
    private final IvaMapper ivaMapper = Mappers.getMapper(IvaMapper.class);
    private StateCountryRepository stateCountryRepository;

    public IvaServiceImpl(IvaRepository ivaRepository, StateCountryRepository stateCountryRepository) {
        this.ivaRepository = ivaRepository;
        this.stateCountryRepository = stateCountryRepository;
    }

    @Override
    public void saveIva(IvaDto ivaDto) {
        System.out.println("Implement part :" + ivaDto);
        ivaRepository.save(ivaMapper.dtoToModel(ivaDto));
    }

    @Override
    public List<IvaDto> getAllIva() {
        // Get all actions
        List<Iva> iva = ivaRepository.findAll();
        // Create a list of all actions dto
        ArrayList<IvaDto> ivaDtos = new ArrayList<>();

        for (Iva iva1 : iva) {
            IvaDto ivaDto = ivaMapper.modelToDto(iva1);
            ivaDtos.add(ivaDto);
        }
        return ivaDtos;
    }

    @Override
    public List<String> getIvaCountries() {
        // Get all actions
        List<Iva> iva = ivaRepository.findAll();
        // Create a list of all actions dto
        ArrayList<IvaDto> ivaDtos = new ArrayList<>();

        for (Iva iva1 : iva) {
            IvaDto ivaDto = ivaMapper.modelToDto(iva1);
            ivaDtos.add(ivaDto);
        }
        ArrayList<String> NewT = new ArrayList<>();
        for (IvaDto ivaDto : ivaDtos) {
            if (!NewT.contains(ivaDto.getStateCountry().getCountry().getCountryName())) NewT.add(ivaDto.getStateCountry().getCountry().getCountryName());
        }
        return NewT;
    }

    @Override
    public List<IvaDto> getIvaStates(String CountryName) {
        // Get all actions
        List<Iva> iva = ivaRepository.findAll();
        // Create a list of all actions dto
        ArrayList<IvaDto> ivaDtos = new ArrayList<>();

        for (Iva iva1 : iva) {
            IvaDto ivaDto = ivaMapper.modelToDto(iva1);
            ivaDtos.add(ivaDto);
        }
        System.out.println("Country :: " + CountryName);
        ArrayList<IvaDto> NewT = new ArrayList<>();
        for (IvaDto ivaDto : ivaDtos) {
            IvaDto newIvaDto = new IvaDto();
            if (ivaDto.getStateCountry().getCountry().getCountryName().equals(CountryName)) {
                for (IvaDto ivaDto1 : ivaDtos) {
                    if (ivaDto1.getStateCountry().getStateName().equals(ivaDto.getStateCountry().getStateName())
                        && (ivaDto1.getStartingDate().after(ivaDto.getStartingDate())))
                        newIvaDto = ivaDto1;
                }
            }

            if (newIvaDto.getIvaId() != null && !NewT.contains(newIvaDto)) NewT.add(newIvaDto);
        }

        return NewT;
    }

    @Override
    public Iva getById(String id) {
        return ivaRepository.findAllBy_id(id);
    }

    @Override
    public List<IvaDto> updateIva(IvaDto ivaDto, String id) {
        // save country if note existe
        Iva iva = getById(id);
        Optional<Iva> iva1 = Optional.ofNullable(ivaRepository.findAllBy_id(id));

        if (!iva1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        System.out.println(iva);

        StateCountry stateCountry = stateCountryRepository.findStateCountryBy_id(ivaDto.getStateCountry().get_id());

        System.out.println(stateCountry);

        iva.setStateCountry(stateCountry);
        iva.setIvaCode(ivaDto.getIvaCode());
        iva.setValue(ivaDto.getValue());
        iva.setElectronicInvoice(ivaDto.isElectronicInvoice());
        iva.setStartingDate(ivaDto.getStartingDate());
        iva.setEndingDate(ivaDto.getEndingDate());

        System.out.println(iva);

        ivaRepository.save(iva);
        return getAllIva();
    }

    @Override
    public List<IvaDto> remove(String id) {
        Optional<Iva> action = Optional.ofNullable(ivaRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        ivaRepository.deleteById(id);
        return getAllIva();
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
