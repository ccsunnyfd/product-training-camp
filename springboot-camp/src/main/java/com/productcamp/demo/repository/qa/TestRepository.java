package com.productcamp.demo.repository.qa;

import com.productcamp.demo.model.qa.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * TestRepository
 *
 * @version 1.0
 */
public interface TestRepository extends MongoRepository<Test, String> {
}
