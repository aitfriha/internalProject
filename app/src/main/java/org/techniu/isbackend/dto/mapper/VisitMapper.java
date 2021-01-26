package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.dto.model.VisitDto;
import org.techniu.isbackend.entity.Visit;

@Mapper(componentModel = "spring")
public interface VisitMapper {
    /**
     * Map dto to model
     *
     * @param visitDto
     * @return Visit
     */
    @Mapping(source = "customerId", target = "customer._id")
    @Mapping(source = "customerCode", target = "customer.code")
    @Mapping(source = "customerName", target = "customer.name")
    @Mapping(source = "operationId", target = "operation._id")
    @Mapping(source = "operationCode", target = "operation.code")
    @Mapping(source = "operationName", target = "operation.name")
    Visit dtoToModel(VisitDto visitDto);

    /**
     * Map model to dto
     *
     * @param visit
     * @return VisitDto
     */
    @Mapping(source = "customer._id", target = "customerId")
    @Mapping(source = "customer.code", target = "customerCode")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "operation._id", target = "operationId")
    @Mapping(source = "operation.code", target = "operationCode")
    @Mapping(source = "operation.name", target = "operationName")
    VisitDto modelToDto(Visit visit);
}
