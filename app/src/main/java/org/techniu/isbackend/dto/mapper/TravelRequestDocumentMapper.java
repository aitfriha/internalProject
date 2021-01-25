package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.dto.model.TravelRequestDocumentDto;
import org.techniu.isbackend.entity.TravelRequestDocument;

@Mapper(componentModel = "spring")
public interface TravelRequestDocumentMapper {
    /**
     * Map dto to model
     *
     * @param docDto
     * @return TravelRequestDocument
     */
    @Mapping(target = "localCurrencyType", ignore=true)
    @Mapping(target = "euroCurrencyType", ignore=true)
    @Mapping(target = "data", ignore=true)
    TravelRequestDocument dtoToModel(TravelRequestDocumentDto docDto);

    /**
     * Map model to dto
     *
     * @param doc
     * @return TravelRequestDocumentDto
     */
    @Mapping(source = "localCurrencyType._id", target="localCurrencyTypeId")
    @Mapping(source = "localCurrencyType.currencyCode", target="localCurrencyTypeCode")
    @Mapping(source = "localCurrencyType.currencyName", target="localCurrencyTypeName")
    @Mapping(source = "euroCurrencyType._id", target="euroCurrencyTypeId")
    @Mapping(source = "euroCurrencyType.currencyCode", target="euroCurrencyTypeCode")
    @Mapping(source = "euroCurrencyType.currencyName", target="euroCurrencyTypeName")
    TravelRequestDocumentDto modelToDto(TravelRequestDocument doc);
}
