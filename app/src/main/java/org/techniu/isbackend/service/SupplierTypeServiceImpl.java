package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.SupplierTypeMapper;
import org.techniu.isbackend.dto.model.SupplierTypeDto;
import org.techniu.isbackend.entity.SupplierType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.SupplierTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class SupplierTypeServiceImpl implements SupplierTypeService {
    private SupplierTypeRepository supplierTypeRepository;
    private final SupplierTypeMapper supplierTypeMapper = Mappers.getMapper(SupplierTypeMapper.class);

    public SupplierTypeServiceImpl(SupplierTypeRepository supplierTypeRepository) {
        this.supplierTypeRepository = supplierTypeRepository;
    }

    @Override
    public void saveSupplierType(SupplierTypeDto supplierTypeDto) {
        System.out.println("Implement part :" + supplierTypeDto);
        supplierTypeRepository.save(supplierTypeMapper.dtoToModel(supplierTypeDto));
    }

    @Override
    public List<SupplierTypeDto> getAllSupplierType() {
        // Get all actions
        List<SupplierType> supplierType = supplierTypeRepository.findAll();
        // Create a list of all actions dto
        ArrayList<SupplierTypeDto> supplierTypeDtos = new ArrayList<>();

        for (SupplierType supplierType1 : supplierType) {
            SupplierTypeDto supplierTypeDto = supplierTypeMapper.modelToDto(supplierType1);
            supplierTypeDtos.add(supplierTypeDto);
        }
        return supplierTypeDtos;
    }

    @Override
    public SupplierType getById(String id) {
        return supplierTypeRepository.findAllBy_id(id);
    }

    @Override
    public List<SupplierTypeDto> updateSupplierType(SupplierTypeDto supplierTypeDto, String id) {
        // save country if note existe
        SupplierType supplierType = getById(id);
        Optional<SupplierType> supplierType1 = Optional.ofNullable(supplierTypeRepository.findAllBy_id(id));

        if (!supplierType1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        System.out.println(supplierType);

        supplierType.setName(supplierTypeDto.getName());
        supplierType.setDescription(supplierTypeDto.getDescription());
        supplierType.setInternalOrder(supplierTypeDto.isInternalOrder());
        supplierType.setOperationAssociated(supplierTypeDto.isOperationAssociated());

        System.out.println(supplierType);

        supplierTypeRepository.save(supplierType);
        return getAllSupplierType();
    }

    @Override
    public List<SupplierTypeDto> remove(String id) {
        Optional<SupplierType> action = Optional.ofNullable(supplierTypeRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        supplierTypeRepository.deleteById(id);
        return getAllSupplierType();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.SupplierType, exceptionType, args);
    }
}
