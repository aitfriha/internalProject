package com.internal.translation.repository;

import com.internal.translation.model.DefaultSentence;
import com.internal.translation.model.TranslateSentence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslateSentenceRepository extends MongoRepository<TranslateSentence, String> {
    List<TranslateSentence> findAll();

    TranslateSentence findTranslateSentenceBy_id(String s);

    List<TranslateSentence> findAllByCountryLanguageCode(String s);

    TranslateSentence findTranslateSentenceByDefaultSentenceAndCountryLanguageCode(DefaultSentence defaultSentence, String countryLanguageCode);


}
