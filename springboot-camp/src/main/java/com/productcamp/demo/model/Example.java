package com.productcamp.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Example
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(columnList = "productId")})
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "ex_gen")
    @GenericGenerator(name = "ex_gen", strategy = "native")
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "htmlContent", columnDefinition = "TEXT")
    private String htmlContent;

    @Column(name = "plainContent", columnDefinition = "TEXT")
    private String plainContent;

    @Column(name = "productId", nullable = false)
    private Long productId;

    public Example(String title, String htmlContent, Long productId) {
        this.title = title;
        this.htmlContent = htmlContent;
        this.productId = productId;
    }
}
