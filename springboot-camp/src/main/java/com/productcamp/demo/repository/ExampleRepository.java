package com.productcamp.demo.repository;

import com.productcamp.demo.model.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * ExampleRepository
 *
 * @version 1.0
 */
public interface ExampleRepository extends JpaRepository<Example, Long>, JpaSpecificationExecutor {
    List<Example> findByProductId(Long prodId);
}
