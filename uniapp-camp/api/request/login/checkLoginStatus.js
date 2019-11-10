//checkLoginStatus.js
import check from "@/api/request/login/checkSession.js" //检验sessing是否过期 0未过期 1已过期
import {
    getCode,
    getProvider
} from "@/api/request/login/login.js"


//跳转至小程序登录页
const toLogin = () => {
//  uni.showToast({
//      icon: 'none',
//      title: '您的登录已过期,跳转至授权页',
//      mask: true,
//      duration: 2000
//  });
    setTimeout(() => {
        uni.redirectTo({
            url: '/pages/login/index'
        });
    }, 2000)
}

//用户缓存信息
const userInfo = (uni.getStorageSync('userInfo'));

//判断登录状态
const checkLoginStatus = async function() {
    if (userInfo) {
        // 检查 session_key 是否过期
        let status = await check()
        //已过期
        if (status == 1) {
            //重新登录
            toLogin();
        } else {
            console.log('执行下一步操作')
        }
    } else {
        // 无skey，作为首次登录
        toLogin();
    }
}

export {
    checkLoginStatus, //判断登录状态
    toLogin, //跳转至小程序登录页
}
