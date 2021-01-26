package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.VoucherTypeMapper;
import org.techniu.isbackend.dto.model.VoucherTypeDto;
import org.techniu.isbackend.entity.VoucherType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.VoucherTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;
import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;


@Service
public class VoucherTypeServiceImpl implements VoucherTypeService {

    private VoucherTypeRepository voucherTypeRepository;
    private final VoucherTypeMapper voucherTypeMapper = Mappers.getMapper(VoucherTypeMapper.class);

    public VoucherTypeServiceImpl(VoucherTypeRepository voucherTypeRepository) {
        this.voucherTypeRepository = voucherTypeRepository;
    }

    @Override
    public List<VoucherTypeDto> getAllVoucherTypes() {
        List<VoucherTypeDto> result = new ArrayList<>();
        List<VoucherType> list = voucherTypeRepository.findAll();
        list.forEach(voucherType -> {
            VoucherTypeDto voucherTypeDto = voucherTypeMapper.modelToDto(voucherType);
            result.add(voucherTypeDto);
        });
        return result;
    }

    @Override
    public void saveVoucherType(VoucherTypeDto voucherTypeDto) {
        VoucherType obj = null;
        obj = voucherTypeRepository.findVoucherTypeByCode(voucherTypeDto.getCode());
        if (obj != null) {
            throw exception(DUPLICATE_ENTITY);
        } else {
            obj = voucherTypeRepository.findVoucherTypeByName(voucherTypeDto.getName());
            if (obj != null) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                voucherTypeRepository.save(voucherTypeMapper.dtoToModel(voucherTypeDto));
            }
        }
    }

    @Override
    public void updateVoucherType(VoucherTypeDto voucherTypeDto) {
        VoucherType obj = null;
        // Find voucher type by code
        Optional<VoucherType> voucherType = voucherTypeRepository.findById(voucherTypeDto.getId());
        if (voucherType.isPresent()) {
            obj = voucherTypeRepository.findVoucherTypeByCode(voucherTypeDto.getCode());
            if (obj != null && !obj.get_id().equalsIgnoreCase(voucherTypeDto.getId())) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                obj = voucherTypeRepository.findVoucherTypeByName(voucherTypeDto.getName());
                if (obj != null && !obj.get_id().equalsIgnoreCase(voucherTypeDto.getId())) {
                    throw exception(DUPLICATE_ENTITY);
                } else {
                    // Get voucher type model
                    VoucherType voucherTypeModel = voucherType.get();
                    voucherTypeModel.setCode(voucherTypeDto.getCode())
                            .setName(voucherTypeDto.getName())
                            .setDescription(voucherTypeDto.getDescription())
                            .setRemovable(voucherTypeDto.isRemovable())
                            .setMasterValue(voucherTypeDto.getMasterValue());

                    // Update voucher type data
                    voucherTypeRepository.save(voucherTypeModel);
                }
            }
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void deleteVoucherType(String id) {
        Optional<VoucherType> voucherType = voucherTypeRepository.findById(id);
        if (voucherType.isPresent()) {
            VoucherType object = voucherType.get();
            voucherTypeRepository.delete(object);
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
        return MainException.throwException(EntityType.VoucherType, exceptionType, args);
    }

}
