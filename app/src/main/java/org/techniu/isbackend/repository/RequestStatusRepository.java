package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.RequestStatus;

@Repository
public interface RequestStatusRepository extends MongoRepository<RequestStatus, String> {
    RequestStatus findRequestStatusByCode(String code);
    RequestStatus findRequestStatusBy_id(String id);
    RequestStatus findRequestStatusByName(String name);
    RequestStatus findRequestStatusByMasterValue(String masterValue);
}
