package com.productcamp.demo.repository;

import com.productcamp.demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * UserInfoRepository
 *
 * @version 1.0
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUname(String uname);
    UserInfo findByUid(String uid);
    UserInfo findBySkey(String skey);
    @Query(value="select u.id from UserInfo u where u.skey=?1")
    Long findUserIdBySkey(String skey);
}
