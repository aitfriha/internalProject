package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.PersonTypeDto;

import java.util.List;

public interface PersonTypeService {

    /**
     * Get All Person Types
     *
     * @return List<PersonTypeDto>
     */
    List<PersonTypeDto> getAllPersonTypes();


    /**
     * Save person Type
     *
     * @param  personTypeDto
     *
     */
    void savePersonType(PersonTypeDto personTypeDto);

    /**
     * Update person Type
     *
     * @param  personTypeDto
     *
     */
    void updatePersonType(PersonTypeDto personTypeDto);

    /**
     * Delete personType
     *
     * @param personTypeId
     */
    void deletePersonType(String personTypeId);

}
