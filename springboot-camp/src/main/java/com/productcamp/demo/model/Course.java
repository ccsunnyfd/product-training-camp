package com.productcamp.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Course
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "productId")})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "course_gen")
    @GenericGenerator(name = "course_gen", strategy = "native")
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "videoTrail", nullable = false, length = 1000)
    private String videoTrail;

    @Column(name = "chapterNum", nullable = false)
    private Long chapterNum;

    @Column(name = "productId", nullable = false)
    private Long productId;

}
