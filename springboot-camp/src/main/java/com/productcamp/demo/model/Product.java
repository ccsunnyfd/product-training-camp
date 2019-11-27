package com.productcamp.demo.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * Product
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
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

    @CreatedDate
    @Column(name = "create_time",insertable = false,updatable = false,columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createTime; // 产品创建时间

    // 构造函数
    public Product(Long id, String name, String description, String scenario, String favicon, String iconType, String prodImg) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.scenario = scenario;
        this.favicon = favicon;
        this.iconType = iconType;
        this.prodImg = prodImg;
    }
}
