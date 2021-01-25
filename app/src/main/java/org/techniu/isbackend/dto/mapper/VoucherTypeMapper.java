package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.VoucherTypeAddRequest;
import org.techniu.isbackend.controller.request.VoucherTypeUpdateRequest;
import org.techniu.isbackend.dto.model.VoucherTypeDto;
import org.techniu.isbackend.entity.VoucherType;

@Mapper(componentModel = "spring")
public interface VoucherTypeMapper {
    /**
     * Map dto to model
     *
     * @param voucherTypeDto
     * @return VoucherType
     */
    @Mapping(source = "id", target="_id")
    VoucherType dtoToModel(VoucherTypeDto voucherTypeDto);

    /**
     * Map VoucherTypeAddRequest to VoucherTypeDto
     *
     * @param voucherTypeAddRequest
     * @return VoucherTypeDto
     */
    VoucherTypeDto addRequestToDto(VoucherTypeAddRequest voucherTypeAddRequest);

    /**
     * Map VoucherTypeUpdateRequest to VoucherTypeDto
     *
     * @param voucherTypeUpdateRequest
     * @return VoucherTypeDto
     */
    VoucherTypeDto updateRequestToDto(VoucherTypeUpdateRequest voucherTypeUpdateRequest);



    /**
     * Map model to dto
     *
     * @param voucherType
     * @return VoucherTypeDto
     */
    @Mapping(source = "_id", target="id")
    VoucherTypeDto modelToDto(VoucherType voucherType);
}
