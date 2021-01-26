package com.internal.translation.repository;

import com.internal.translation.model.DefaultSentence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefaultSentenceRepository extends MongoRepository<DefaultSentence, String> {
    List<DefaultSentence> findAll();
    DefaultSentence findDefaultSentenceBy_id(String s);
    DefaultSentence findByCode(String s);
}
