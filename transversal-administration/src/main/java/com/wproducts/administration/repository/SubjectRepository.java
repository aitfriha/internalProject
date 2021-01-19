package com.wproducts.administration.repository;

import com.wproducts.administration.model.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    Subject findBySubjectCode(String s);
    Subject findSubjectBy_id(String s);
}
