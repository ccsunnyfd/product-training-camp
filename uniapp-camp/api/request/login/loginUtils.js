//loginUtils.js
import {
	authDo
} from '@/api/mpLogin/wxlogin.js'

//获取服务商信息
// res的内容：{"errMsg":"getProvider:ok","service":"oauth","provider":["weixin"]}
const getProvider = () => {
	const promise = new Promise((resolve, reject) => {
		uni.getProvider({
			service: 'oauth', //服务类型  登录授权
			success: function(res) {
				resolve(res.provider)
			},
			fail(res) {
				reject(res)
			}
		});
	}).catch(res => {
		uni.showToast({
			icon: 'none',
			title: res.errMsg || '获取服务商信息失败',
			duration: 2000
		});
	})

	return promise
}


//获取code
//{"errMsg":"login:ok","code":"009Lbpsj2ggKpE8AM4tj2604sj7Xbpsp"}
const getCode = provider => {
	if (!provider) {
		uni.showToast({
			icon: 'none',
			title: '获取服务商信息失败',
			duration: 2000
		});
		return
	}

	return new Promise((resolve, reject) => {
		uni.login({
			provider: provider,
			success: function(loginRes) {
				if (loginRes && loginRes.code) {
					resolve(loginRes.code)
				} else {
					reject(loginRes)
				}
			}
		});
	}).catch(res => {
		uni.showToast({
			icon: 'none',
			title: res.errMsg || '获取code失败',
			duration: 2000
		});
	})
}

// 申请skey并保存userInfo和skey到本地
const login = async function(e) {
	// 向开发者服务器申请获取自定义登录态
	let param = {
		code: e.code,
		encryptedData: e.encryptedData,
		iv: e.iv
	}
	try {
		const result = await authDo(param)
		// 储存用户信息到本地
		uni.setStorageSync('userInfo', JSON.stringify(result.userInfo));
		// 储存自定义登录态到本地
		uni.setStorageSync('loginFlag', result.skey);
		//登录成功之后的回调
		uni.showToast({
			title: '登录成功',
			duration: 2000
		});
		uni.switchTab({
			url: '/pages/index/index'
		});
	} catch (e) {
		uni.showToast({
			icon: 'none',
			title: '服务器登录失败',
			duration: 2000
		});
	}
}

export {
	login, //申请skey并保存usrInfo和skey到本地
	getCode, //获取code
	getProvider //获取服务商信息
}
