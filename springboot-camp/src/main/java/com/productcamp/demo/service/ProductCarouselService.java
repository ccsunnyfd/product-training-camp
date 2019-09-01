package com.productcamp.demo.service;

import com.productcamp.demo.model.ProductCarousel;
import com.productcamp.demo.repository.ProductCarouselRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ProductCarouselService
 *
 * @version 1.0
 */
@Service
public class ProductCarouselService {
    private ProductCarouselRepository productCarouselRepository;

    @Autowired
    public void setProductCarouselRepository(ProductCarouselRepository productCarouselRepository) {
        this.productCarouselRepository = productCarouselRepository;
    }

    public List<ProductCarousel> getProductCarousel() {
        return productCarouselRepository.findAll();
    }

    public Long addNewCarousel(ProductCarousel productCarousel) {
        ProductCarousel newCarousel = productCarouselRepository.save(productCarousel);
        return newCarousel.getId();
    }
}
