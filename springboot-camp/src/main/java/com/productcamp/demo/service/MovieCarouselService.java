package com.productcamp.demo.service;

import com.productcamp.demo.model.MovieCarousel;
import com.productcamp.demo.repository.MovieCarouselRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MovieCarouselService
 *
 * @version 1.0
 */
@Service
public class MovieCarouselService {
    private MovieCarouselRepository movieCarouselRepository;

    @Autowired
    public void setMovieCarouselRepository(MovieCarouselRepository movieCarouselRepository) {
        this.movieCarouselRepository = movieCarouselRepository;
    }

    public List<MovieCarousel> getMovieCarousel() {
        return movieCarouselRepository.findAll();
    }

    public Long addNewCarousel(MovieCarousel movieCarousel) {
        MovieCarousel newCarousel = movieCarouselRepository.save(movieCarousel);
        return newCarousel.getId();
    }
}
