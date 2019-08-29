package com.productcamp.demo.repository;

import com.productcamp.demo.model.MovieCarousel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MovieCarouselRepository
 *
 * @version 1.0
 */
public interface MovieCarouselRepository extends JpaRepository<MovieCarousel, Long> {

}
