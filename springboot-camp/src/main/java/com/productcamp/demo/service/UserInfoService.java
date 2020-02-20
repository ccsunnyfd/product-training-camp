package com.productcamp.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.productcamp.demo.model.UserInfo;
import com.productcamp.demo.repository.UserInfoRepository;
import com.productcamp.demo.utils.MPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;

/**
 * UserInfoService
 *
 * @version 1.0
 */
@Service
public class UserInfoService {
    private UserInfoRepository userInfoRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public void setUserInfoRepository(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

//    // 更新skey
//    public String updateSkey(String code) {
//        String session_key = MPUtils.getSessionKey(code);
//        return MPUtils.encryptSha1(session_key);
//    }

    // 微信用户认证
    public JSONObject wxAuth(String code, String encryptedData, String iv) {
        String session_key = MPUtils.getSessionKey(code);
        String skey = MPUtils.encryptSha1(session_key);
        JSONObject decryptedData = MPUtils.decryptByAES(encryptedData, session_key, iv);
        // 构造UserInfo,存入数据库
        UserInfo userInfo = new UserInfo();
        String uid = decryptedData.getString("openId");
        userInfo.setUid(uid);
        userInfo.setUname(decryptedData.getString("nickName"));
        userInfo.setUgender(decryptedData.getInteger("gender"));
        userInfo.setUaddress(decryptedData.getString("city") + "," + decryptedData.getString("province"));
        userInfo.setSkey(skey);
        userInfo.setSessionkey(session_key);
        userInfo.setUavatar(decryptedData.getString("avatarUrl"));
        UserInfo searchUser = userInfoRepository.findByUid(uid);
        if(searchUser == null) {
            // 新增用户
            UserInfo newUser = userInfoRepository.save(userInfo);
            System.out.println("新增用户： userId:" + newUser.getId() + "; skey:" + newUser.getSkey());
        } else {
            // 更新用户
            userInfo.setId(searchUser.getId());
            UserInfo newUser = userInfoRepository.save(userInfo);
            System.out.println("更新用户： userId:" + newUser.getId() + "; skey:" + newUser.getSkey());
        }

        // 去除敏感信息并返回
        decryptedData.remove("openId");
        decryptedData.remove("watermark");
        JSONObject result = new JSONObject();
        result.put("userInfo", decryptedData);
        result.put("skey", skey);
        return result;
    }

    // 根据用户名查找用户
    public UserInfo findByUname(String uname) {
        return userInfoRepository.findByUname(uname);
    }

    // 根据用户skey查找用户id
    public Long getUserIdBySkey(String skey) {
        Long userId = userInfoRepository.findUserIdBySkey(skey);
        return userId;
    }

    // 注册用户
//    @Transactional
//    public UserInfo register(UserInfo userInfo) {
//        String password = userInfo.getPassword();
//        String encodedPass = DigestUtils.md5DigestAsHex(password.getBytes());
//        String userUniqueToken = UUID.randomUUID().toString();
//        userInfo.setPassword(encodedPass);
//        userInfo.setNickname(userInfo.getUsername());
//        userInfo.setUserUniqueToken(userUniqueToken);
//
//        UserInfo ret = userInfoRepository.save(userInfo);//因为缓存原因，数据库数据没更新到实体类中，用entityManager.refresh(Entity)方法更新下就好，否则会拿不到default的faceImage字段数据
//        entityManager.refresh(ret);  //这会发送一条select请求
//        return ret;
//    }


    // 用户登录验证
//    public Boolean validation(UserInfo savedUser, UserInfo loginUser) {
//        return savedUser.getPassword().equals(DigestUtils.md5DigestAsHex(loginUser.getPassword().getBytes()));
//    }

//    @Transactional(propagation = Propagation.SUPPORTS)
//    public boolean queryWXOpenIdIsExist(String openId) {
//        UserInfo userInfo = userInfoRepository.findByMpWxOpenId(openId);
//        return userInfo != null;
//    }

//    @Transactional(propagation = Propagation.SUPPORTS)
//    public boolean queryAppUnionOpenIdIsExist(String openId, String logintype) {
//        UserInfo userInfo;
//        if ("weixin".equals(logintype)) {
//            userInfo = userInfoRepository.findByAppWxOpenId(openId);
//        } else if ("qq".equals(logintype)) {
//            userInfo = userInfoRepository.findByAppQqOpenId(openId);
//        } else if ("sinaweibo".equals(logintype)) {
//            userInfo = userInfoRepository.findByAppWeiboUId(openId);
//        } else {
//            userInfo = null;
//        }
//        return userInfo != null;
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    public UserInfo saveUserMPWXOpenId(String openId, MPWXUserBO mpwxUserBO) {
//        UserInfo user = new UserInfo();
//        user.setMpWxOpenId(openId);
//        user.setNickname(mpwxUserBO.getNickName());
//
//        user.setFaceImage(mpwxUserBO.getAvatarUrl());
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, 1988);
//        calendar.set(Calendar.MONTH, 11);
//        calendar.set(Calendar.DAY_OF_MONTH, 30);
//        user.setBirthday(new Date(calendar.getTime().getTime()));
//        user.setIsCertified(0);
//        user.setRegistTime(new Date(System.currentTimeMillis()));
//        String userUniqueToken = UUID.randomUUID().toString();
//        user.setUserUniqueToken(userUniqueToken);
//        user.setUsername(mpwxUserBO.getNickName());
//
//        userInfoRepository.save(user);
//        return user;
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    public UserInfo saveUserFaceImg(Long userId, String faceImg) {
//        Optional<UserInfo> opt = userInfoRepository.findById(userId);
//        if (!opt.isPresent()) {
//            return null;
//        }
//        UserInfo user = opt.get();
//        user.setFaceImage(faceImg);
//
//        userInfoRepository.save(user);
//        return user;
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    public UserInfo modifyUserInfo(UserInfo newInfo) {
//        if (newInfo == null || newInfo.getId() == null) {
//            return null;
//        }
//        Optional<UserInfo> opt = userInfoRepository.findById(newInfo.getId());
//        if (!opt.isPresent()) {
//            return null;
//        }
//        UserInfo user = opt.get();
//        if (!(newInfo.getNickname() == null || "".equals(newInfo.getNickname().trim()))) {
//            user.setNickname(newInfo.getNickname());
//        } else if (!(newInfo.getBirthday() == null)) {
//            user.setBirthday(newInfo.getBirthday());
//        } else if (!(newInfo.getSex() == null || "".equals(newInfo.getSex().trim()))) {
//            user.setSex(newInfo.getSex());
//        } else {
//            return user;
//        }
//
//        userInfoRepository.save(user);
//        return user;
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    public UserInfo saveUserAppUnion(String logintype, AppUnionUserBO appUnionUserBO) {
//        UserInfo user = new UserInfo();
//        if ("weixin".equals(logintype)) {
//            user.setAppWxOpenId(appUnionUserBO.getOpenIdOrUid());
//        } else if ("qq".equals(logintype)) {
//            user.setAppQqOpenId(appUnionUserBO.getOpenIdOrUid());
//        } else if ("sinaweibo".equals(logintype)) {
//            user.setAppWeiboUId(appUnionUserBO.getOpenIdOrUid());
//        }
//        user.setNickname(appUnionUserBO.getNickName());
//
//        user.setFaceImage(appUnionUserBO.getFace());
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, 1988);
//        calendar.set(Calendar.MONTH, 12);
//        calendar.set(Calendar.DAY_OF_MONTH, 30);
//        user.setBirthday(new Date(calendar.getTime().getTime()));
//        user.setIsCertified(0);
//        user.setRegistTime(new Date(System.currentTimeMillis()));
//        String userUniqueToken = UUID.randomUUID().toString();
//        user.setUserUniqueToken(userUniqueToken);
//        user.setUsername(appUnionUserBO.getNickName());
//
//        userInfoRepository.save(user);
//        return user;
//    }
//
//    @Transactional(propagation = Propagation.SUPPORTS)
//    public UserInfo queryUserForLoginByMPWX(String openId) {
//        return userInfoRepository.findByMpWxOpenId(openId);
//    }
//
//    @Transactional(propagation = Propagation.SUPPORTS)
//    public UserInfo queryUserForAppLogin(String openId, String logintype) {
//        UserInfo userInfo;
//        if ("weixin".equals(logintype)) {
//            userInfo = userInfoRepository.findByAppWxOpenId(openId);
//        } else if ("qq".equals(logintype)) {
//            userInfo = userInfoRepository.findByAppQqOpenId(openId);
//        } else if ("sinaweibo".equals(logintype)) {
//            userInfo = userInfoRepository.findByAppWeiboUId(openId);
//        } else {
//            userInfo = null;
//        }
//        return userInfo;
//    }

}
