import checkSession from "@/api/request/login/checkSession.js" //检验sessing是否过期 0未过期 1已过期


//判断登录状态
const checkLoginStatus = async function() {
	//用户缓存信息
	const userInfo = uni.getStorageSync("userInfo");
    if (userInfo) {
        // 检查 session_key 是否过期
        let status = await checkSession()
        //已过期
        if (status === 'expire') {
            //重新登录
            toLogin();
        }
		//未过期则什么都不用做
    } else {
        // 无skey，作为首次登录
        toLogin();
    }
}

//跳转至小程序登录页
const toLogin = () => {
    setTimeout(() => {
        uni.redirectTo({
            url: '/pages/login/index'
        });
    }, 1000)
}

export {
    checkLoginStatus //判断登录状态
}
