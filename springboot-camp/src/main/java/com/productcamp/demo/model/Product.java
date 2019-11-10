package com.productcamp.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "prod_gen")
    @GenericGenerator(name = "prod_gen", strategy = "native")
    private Long id;

    @Column(name = "name", nullable = false, length = 48)
    private String name;

    @Column(name = "description", nullable = false, length = 550)
    private String description;

    @Column(name = "scenario", nullable = false, length = 550)
    private String scenario;

//    小程序首页的产品图标
    @Column(name = "favicon", length = 1000)
    private String favicon;

//    抽屉式左侧划栏的iconfont的图标class
    @Column(name = "icon_type", length = 100)
    private String iconType;

//    产品详情页图片
    @Column(name = "prod_img", length = 1000)
    private String prodImg;

    // 构造函数
    public Product(String name, String description, String scenario, String favicon, String iconType, String prodImg) {
        this.name = name;
        this.description = description;
        this.scenario = scenario;
        this.favicon = favicon;
        this.iconType = iconType;
        this.prodImg = prodImg;
    }
}
