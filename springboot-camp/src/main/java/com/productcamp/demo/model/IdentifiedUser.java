package com.productcamp.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * IdentifiedUser
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
@ToString
@Table(indexes = {@Index(columnList = "phoneNumber")})
public class IdentifiedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "identifiedUser_gen")
    @GenericGenerator(name = "identifiedUser_gen", strategy = "native")
    private Long id;

    @Column(name = "phoneNumber", columnDefinition = "varchar(20) default ''")
    private String phoneNumber; // 手机号码

    @Column(name = "realname", columnDefinition = "varchar(40) default ''")
    private String realname; // 真实姓名

    @Column(name = "district", columnDefinition = "varchar(128) default ''")
    private String district; // 区域或公司名

}
