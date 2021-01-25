package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.RequestStatus;
import org.techniu.isbackend.entity.Staff;
import org.techniu.isbackend.entity.TravelRequest;

import java.util.Date;
import java.util.List;

@Repository
public interface TravelRequestRepository extends MongoRepository<TravelRequest, String> {
    List<TravelRequest> findAllByRequesterAndRequestDateIsBetween(Staff staff, Date start, Date end);
    List<TravelRequest> findAllByRequestDateIsBetween(Date start, Date end);
    List<TravelRequest> findAllByRequester(Staff staff);
    TravelRequest findTravelRequestBy_id(String id);
    TravelRequest findTravelRequestByCode(String code);
    List<TravelRequest> findAllByStatus(RequestStatus status);
}
