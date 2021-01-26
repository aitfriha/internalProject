package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.RequestStatusMapper;
import org.techniu.isbackend.dto.model.RequestStatusDto;
import org.techniu.isbackend.entity.RequestStatus;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.RequestStatusRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;
import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;


@Service
public class RequestStatusServiceImpl implements RequestStatusService {

    private RequestStatusRepository requestStatusRepository;
    private final RequestStatusMapper requestStatusMapper = Mappers.getMapper(RequestStatusMapper.class);

    public RequestStatusServiceImpl(RequestStatusRepository requestStatusRepository) {
        this.requestStatusRepository = requestStatusRepository;
    }

    @Override
    public List<RequestStatusDto> getAllRequestStatus() {
        List<RequestStatusDto> result = new ArrayList<>();
        List<RequestStatus> list = requestStatusRepository.findAll();
        list.forEach(requestStatus -> {
            RequestStatusDto requestStatusDto = requestStatusMapper.modelToDto(requestStatus);
            result.add(requestStatusDto);
        });
        return result;
    }

    @Override
    public void saveRequestStatus(RequestStatusDto requestStatusDto) {
        RequestStatus obj = null;
        obj = requestStatusRepository.findRequestStatusByCode(requestStatusDto.getCode());
        if (obj != null) {
            throw exception(DUPLICATE_ENTITY);
        } else {
            obj = requestStatusRepository.findRequestStatusByName(requestStatusDto.getName());
            if (obj != null) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                requestStatusDto.setMasterValue(requestStatusDto.getName().toUpperCase());
                requestStatusRepository.save(requestStatusMapper.dtoToModel(requestStatusDto));
            }
        }
    }

    @Override
    public void updateRequestStatus(RequestStatusDto requestStatusDto) {
        RequestStatus obj = null;
        // Find person type by code
        Optional<RequestStatus> personType = requestStatusRepository.findById(requestStatusDto.getId());
        if (personType.isPresent()) {
            obj = requestStatusRepository.findRequestStatusByCode(requestStatusDto.getCode());
            if (obj != null && !obj.get_id().equalsIgnoreCase(requestStatusDto.getId())) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                obj = requestStatusRepository.findRequestStatusByName(requestStatusDto.getName());
                if (obj != null && !obj.get_id().equalsIgnoreCase(requestStatusDto.getId())) {
                    throw exception(DUPLICATE_ENTITY);
                } else {
                    // Get person type model
                    RequestStatus requestStatusModel = personType.get();
                    requestStatusModel.setCode(requestStatusDto.getCode())
                            .setName(requestStatusDto.getName())
                            .setDescription(requestStatusDto.getDescription())
                            .setMasterValue(requestStatusDto.getMasterValue())
                            .setRemovable(requestStatusDto.isRemovable());

                    // Update person type data
                    requestStatusRepository.save(requestStatusModel);
                }
            }
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void deleteRequestStatus(String id) {
        Optional<RequestStatus> requestStatus = requestStatusRepository.findById(id);
        if (requestStatus.isPresent()) {
            RequestStatus object = requestStatus.get();
            requestStatusRepository.delete(object);
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
        return MainException.throwException(EntityType.RequestStatus, exceptionType, args);
    }

}
