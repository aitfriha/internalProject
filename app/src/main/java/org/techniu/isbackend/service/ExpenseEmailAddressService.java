package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.ExpenseEmailAddressDto;

import java.util.List;

public interface ExpenseEmailAddressService {

    /**
     * Get All Email Addresses
     *
     * @return List<ExpenseEmailAddressDto>
     */
    List<ExpenseEmailAddressDto> getAllEmailAddresses();

    /**
     * Save expense Email address
     *
     * @param  expenseEmailAddressDto
     *
     */
    void saveEmailAddress(ExpenseEmailAddressDto expenseEmailAddressDto);

    /**
     * Update expense Email Address
     *
     * @param  expenseEmailAddressDto
     *
     */
    void updateEmailAddress(ExpenseEmailAddressDto expenseEmailAddressDto);


    /**
     * Delete expense Email Address
     *
     * @param emailAddressId
     */
    void deleteEmailAddress(String emailAddressId);

}
