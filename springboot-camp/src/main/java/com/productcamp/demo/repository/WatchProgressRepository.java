package com.productcamp.demo.repository;

import com.productcamp.demo.model.WatchProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * WatchProgressRepository
 *
 * @version 1.0
 */
public interface WatchProgressRepository extends JpaRepository<WatchProgress, Long>, JpaSpecificationExecutor {
    List<WatchProgress> findByUserId(Long userId);
    @Query(value="select w from WatchProgress w, Course c where w.courseId=c.id and w.userId=?1 and c.productId=?2")
    List<WatchProgress> findByUserIdAndProdId(Long userId, Long prodId);
    WatchProgress findByUserIdIsAndCourseIdIs(Long userId, Long courseId);
    void deleteAllByUserId(Long userId);
    void deleteAllByCourseId(Long courseId);
}
