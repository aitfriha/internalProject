package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.TravelRequestDto;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface TravelRequestService {


    /**
     * Get all travel requests
     *
     * @param data
     * @return List<TravelRequestDto>
     */
    List<TravelRequestDto> getAllTravelRequests(HashMap data);

    /**
     * Save travel request
     *
     * @param travelRequestDto
     */
    void saveTravelRequest(TravelRequestDto travelRequestDto);

    /**
     * Update travel request
     *
     * @param travelRequestDto
     */
    void updateTravelRequest(TravelRequestDto travelRequestDto);

    /**
     * ChangeStatus travelRequest
     *
     * @param data
     */
    void changeStatusTravelRequest(HashMap data);

    /**
     * exportTravelRequests
     *
     * @param data
     */
    File exportTravelRequests(HashMap data);

    /**
     * downloadAttachedDocumentsOfTravelRequest
     *
     * @param travelRequestId
     */
    byte[] downloadDocumentsOfTravelRequest(String travelRequestId);

    /**
     * approveTravelRequest
     *
     * @param data
     */
    void approveTravelRequest(HashMap data);

    /**
     * existsTravelRequestsWithStatus
     *
     * @param statusId
     * @return boolean
     */
    boolean existsTravelRequestsWithStatus(String statusId);

    /**
     * existsTravelRequestsWithExpense
     *
     * @param data
     * @return boolean
     */
    boolean existsTravelRequestsWithBusinessExpenseSubtype(HashMap data);

}
