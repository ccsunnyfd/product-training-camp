package com.productcamp.demo.service;

import com.productcamp.demo.model.Course;
import com.productcamp.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CourseService
 *
 * @version 1.0
 */
@Service
public class CourseService {
    private CourseRepository courseRepository;

    @Autowired
    public void setExampleRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCoursesByProductId(Long prodId) {
        return courseRepository.findByProductIdOrderByChapterNum(prodId);
    }

    @Transactional
    public Long addNewCourse(Course course) {
        Course newCourse = courseRepository.save(course);
        return newCourse.getId();
    }

    @Transactional
    public void delCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
