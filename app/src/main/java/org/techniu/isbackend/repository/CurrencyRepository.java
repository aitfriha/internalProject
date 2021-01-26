package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.Currency;
import org.techniu.isbackend.entity.TypeOfCurrency;

import java.util.List;


public interface CurrencyRepository extends MongoRepository<Currency, String> {

    Currency findAllBy_id(String id);
    List<Currency> findAllByTypeOfCurrency(TypeOfCurrency typeOfCurrency);

}
