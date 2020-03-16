package com.productcamp.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * TokenUser
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
@ToString
@Table(indexes = {@Index(columnList = "token")})
public class TokenUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "identifiedUser_gen")
    @GenericGenerator(name = "identifiedUser_gen", strategy = "native")
    private Long id;

    @Column(name = "token", columnDefinition = "varchar(20) default ''")
    private String token; // 手机号码

}
