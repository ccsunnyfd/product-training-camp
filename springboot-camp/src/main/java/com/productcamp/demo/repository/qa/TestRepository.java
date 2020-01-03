package com.productcamp.demo.repository.qa;

import com.productcamp.demo.model.qa.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * TestRepository
 *
 * @version 1.0
 */
public interface TestRepository extends MongoRepository<Test, String> {
}
