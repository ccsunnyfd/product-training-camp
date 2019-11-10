package com.productcamp.demo.repository;

import com.productcamp.demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserInfoRepository
 *
 * @version 1.0
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUname(String uname);
    UserInfo findByUid(String uid);
}
