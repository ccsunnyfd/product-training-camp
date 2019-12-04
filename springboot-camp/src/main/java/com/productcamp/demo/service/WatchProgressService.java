package com.productcamp.demo.service;

import com.productcamp.demo.model.Course;
import com.productcamp.demo.model.WatchProgress;
import com.productcamp.demo.repository.CourseRepository;
import com.productcamp.demo.repository.WatchProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * WatchProgressService
 *
 * @version 1.0
 */
@Service
public class WatchProgressService {
    private WatchProgressRepository watchProgressRepository;

    @Autowired
    public void setWatchProgressRepository(WatchProgressRepository watchProgressRepository) {
        this.watchProgressRepository = watchProgressRepository;
    }

    public WatchProgress findByUserIdIsAndCourseIdIs(Long userId, Long courseId) {
        return watchProgressRepository.findByUserIdIsAndCourseIdIs(userId, courseId);
    }

    public List<WatchProgress> findByUserId(Long userId) {
        return watchProgressRepository.findByUserId(userId);
    }

    public List<WatchProgress> findByUserIdAndProdId(Long userId, Long prodId) {
        return watchProgressRepository.findByUserIdAndProdId(userId, prodId);
    }

    @Transactional
    public Long saveOrUpdateProgress(WatchProgress watchProgress) {
        WatchProgress newProgress = watchProgressRepository.save(watchProgress);
        return newProgress.getId();
    }

    @Transactional
    public void clearProgressById(Long id) {
        watchProgressRepository.deleteById(id);
    }
}
