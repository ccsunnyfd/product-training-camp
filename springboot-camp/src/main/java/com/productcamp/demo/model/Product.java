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

    @Column(name = "favicon", length = 1000)
    private String favicon;

//    iconfont的图标class
    @Column(name = "icon_type", length = 100)
    private String iconType;

//    产品详情页图片
    @Column(name = "prod_img", length = 1000)
    private String prodImg;

    @Column(name = "poster", insertable = false, columnDefinition = "varchar(1000) default 'https://172.16.70.2:9000/product-camp/test/favicon/BCM.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=ZWHVQOKYOVTXSU9WH3CW%2F20190901%2F%2Fs3%2Faws4_request&X-Amz-Date=20190901T111711Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=d80417e01f3857dc1b15edaebb06c8aedd374d8ef4fdecf2a0e166fe1b605738'")
    private String poster;

}
