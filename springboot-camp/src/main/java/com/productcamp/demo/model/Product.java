package com.productcamp.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Product
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "prod_gen")
    @GenericGenerator(name = "prod_gen", strategy = "native")
    private Long id;

    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Column(name = "description", nullable = false, length = 550)
    private String description;

    @Column(name = "scenario", nullable = false, length = 550)
    private String scenario;

    @Column(name = "favicon", length = 500)
    private String favicon;

    @Column(name = "poster", insertable = false, columnDefinition = "varchar(500) default 'https://172.16.70.2:9000/product-camp/test/justice.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=ZWHVQOKYOVTXSU9WH3CW%2F20190829%2F%2Fs3%2Faws4_request&X-Amz-Date=20190829T075745Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=728e73a94e917744cdd176e4c8d20e23b4c7f8c8a559c73681a4110143432300'")
    private String poster;

}
