package com.productcamp.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * WatchProgress
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(indexes = {
        @Index(name="IDX_UserId", columnList = "userId"),
        @Index(name="IDX_CourseId", columnList = "courseId")
})
public class WatchProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "watchProgress_gen")
    @GenericGenerator(name = "watchProgress_gen", strategy = "native")
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "courseId", nullable = false)
    private Long courseId;

    @Column(name = "currentProgress", nullable = false)
    private Long currentProgress;

    @Column(name = "duration", nullable = false)
    private Long duration;

    public WatchProgress(Long id, Long userId, Long courseId, Long currentProgress, Long duration) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.currentProgress = currentProgress;
        this.duration = duration;
    }
}
