package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.VoucherTypeDto;

import java.util.List;

public interface VoucherTypeService {

    /**
     * Get All Voucher Types
     *
     * @return List<VoucherTypeDto>
     */
    List<VoucherTypeDto> getAllVoucherTypes();

    /**
     * Save voucher Type
     *
     * @param  voucherTypeDto
     *
     */
    void saveVoucherType(VoucherTypeDto voucherTypeDto);

    /**
     * Update voucher Type
     *
     * @param  voucherTypeDto
     *
     */
    void updateVoucherType(VoucherTypeDto voucherTypeDto);

    /**
     * Delete voucherType
     *
     * @param voucherTypeId
     */
    void deleteVoucherType(String voucherTypeId);

}
