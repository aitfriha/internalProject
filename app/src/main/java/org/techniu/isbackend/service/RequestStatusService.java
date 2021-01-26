package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.PersonTypeDto;
import org.techniu.isbackend.dto.model.RequestStatusDto;

import java.util.List;

public interface RequestStatusService {

    /**
     * Get All Request Status
     *
     * @return List<RequestStatusDto>
     */
    List<RequestStatusDto> getAllRequestStatus();


    /**
     * Save request Status
     *
     * @param  requestStatusDto
     *
     */
    void saveRequestStatus(RequestStatusDto requestStatusDto);

    /**
     * Update person Type
     *
     * @param  requestStatusDto
     *
     */
    void updateRequestStatus(RequestStatusDto requestStatusDto);

    /**
     * Delete requestStatus
     *
     * @param requestStatusId
     */
    void deleteRequestStatus(String requestStatusId);

}
