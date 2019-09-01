package com.productcamp.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * ProductCarousel
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
public class ProductCarousel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "carousel_gen")
    @GenericGenerator(name = "carousel_gen", strategy = "native")
    private Long id;

    @Column(name = "image", nullable = false, length = 500)
    private String image;

    @Column(name = "sort", nullable = false, length = 10)
    private Integer sort;

    @Column(name = "isShow", length = 1, insertable = false, columnDefinition = "int default 1")
    private Integer isShow;
}
