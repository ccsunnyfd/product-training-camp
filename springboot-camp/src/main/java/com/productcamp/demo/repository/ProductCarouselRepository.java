package com.productcamp.demo.repository;

import com.productcamp.demo.model.ProductCarousel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductCarouselRepository
 *
 * @version 1.0
 */
public interface ProductCarouselRepository extends JpaRepository<ProductCarousel, Long> {

}
