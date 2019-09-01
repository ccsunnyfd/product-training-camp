package com.productcamp.demo.repository;

import com.productcamp.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * ProductRepository
 *
 * @version 1.0
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor {

}
