package com.productcamp.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * UserInfo
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
//@EntityListeners(AuditingEntityListener.class)
@ToString
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "user_gen")
    @GenericGenerator(name = "user_gen", strategy = "native")
    private Long id;

    @Column(name = "uid", columnDefinition = "varchar(128) default ''")
    private String uid; // 用户openId

    @Column(name = "uname", columnDefinition = "varchar(40) default ''")
    private String uname; // 用户微信名

    @Column(name = "ugender", length = 1, columnDefinition = "int default 0")
    private Integer ugender;    // 用户性别 1: 男  2：女  0: 未知

    @Column(name = "uaddress", columnDefinition = "varchar(128) default ''")
    private String uaddress; // 用户地址

    @Column(name = "uavatar", columnDefinition = "varchar(255) default ''")
    private String uavatar; // 用户头像

    @Column(name = "skey", columnDefinition = "varchar(128) default ''")
    private String skey; // 用户登录态标识

    @Column(name = "sessionkey", columnDefinition = "varchar(128) default ''")
    private String sessionkey; // 微信登录态标识

    @CreatedDate
//    @Column(name = "create_time")
    @Column(name = "create_time",insertable = false,updatable = false,columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createTime; // 账号注册时间

    @LastModifiedDate
//    @Column(name = "update_time")
    @Column(name = "update_time",insertable = false,updatable = false,columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updateTime; // 用户最近登录时间

    @Column(name = "identified_id")
    private Long identified_id; // 关联的IdentifiedUser表的主键id
}
