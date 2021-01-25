package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.TravelRequestEmailAddressDto;

import java.util.List;

public interface TravelRequestEmailAddressService {

    /**
     * Get All Email Addresses
     *
     * @return List<EmailAddressDto>
     */
    List<TravelRequestEmailAddressDto> getAllEmailAddresses();

    /**
     * Save email address
     *
     * @param  emailAddressDto
     *
     */
    void saveEmailAddress(TravelRequestEmailAddressDto emailAddressDto);

    /**
     * Update email Address
     *
     * @param  emailAddressDto
     *
     */
    void updateEmailAddress(TravelRequestEmailAddressDto emailAddressDto);


    /**
     * Delete email Address
     *
     * @param emailAddressId
     */
    void deleteEmailAddress(String emailAddressId);

}
