package com.productcamp.demo.service;

import com.productcamp.demo.model.Example;
import com.productcamp.demo.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ExampleService
 *
 * @version 1.0
 */
@Service
public class ExampleService {
    private ExampleRepository exampleRepository;

    @Autowired
    public void setExampleRepository(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    public List<Example> getExamplesByProductId(Long prodId) {
        return exampleRepository.findByProductId(prodId);
    }

    @Transactional
    public Long addNewExample(Example example) {
        Example newExample = exampleRepository.save(example);
        return newExample.getId();
    }

    @Transactional
    public void delExample(Long id) {
        exampleRepository.deleteById(id);
    }
}
