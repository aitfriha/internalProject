package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.TypeOfCurrency;

public interface TypeOfCurrencyRepository extends MongoRepository<TypeOfCurrency, String> {

    TypeOfCurrency findAllBy_id(String id);
}
