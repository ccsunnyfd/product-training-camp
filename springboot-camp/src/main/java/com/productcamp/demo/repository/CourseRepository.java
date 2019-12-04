package com.productcamp.demo.repository;

import com.productcamp.demo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * CourseRepository
 *
 * @version 1.0
 */
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor {
    List<Course> findByProductIdOrderByChapterNum(Long prodId);
}
