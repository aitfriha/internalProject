package com.wproducts.administration.repository;

import com.wproducts.administration.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {

    Department findByDepartmentCode(String s);
    Department findDepartmentBy_id(String s);

}
