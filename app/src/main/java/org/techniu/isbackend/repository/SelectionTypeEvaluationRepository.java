package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.techniu.isbackend.entity.SelectionTypeEvaluation;
import org.techniu.isbackend.entity.SelectionTypeEvaluation;

import java.util.List;

public interface SelectionTypeEvaluationRepository extends MongoRepository<SelectionTypeEvaluation, String> {
    SelectionTypeEvaluation findByName(String name);

    SelectionTypeEvaluation findBy_id(String id);

    List<SelectionTypeEvaluation> findByType(String type);

    SelectionTypeEvaluation findByChildsContaining(SelectionTypeEvaluation type);
}
