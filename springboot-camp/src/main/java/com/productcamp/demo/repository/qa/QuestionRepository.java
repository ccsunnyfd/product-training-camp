package com.productcamp.demo.repository.qa;

import com.productcamp.demo.model.qa.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * QuestionRepository
 *
 * @version 1.0
 */
public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findByQtype(Integer qtype);
}
