//loginUtils.js
import {
	authDo,
	phoneDo,
	tokenDo
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
		})
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
	})
}

// 申请skey并保存userInfo和skey到本地
const login = (e) => {
	const promise = new Promise((resolve, reject) => {
		let param = {
			code: e.code,
			encryptedData: e.encryptedData,
			iv: e.iv
		}
		authDo(param).then((result) => {
			// 储存用户信息到本地
			uni.setStorageSync('userInfo', JSON.stringify(result.userInfo))
			// 储存自定义登录态到本地
			uni.setStorageSync('loginFlag', result.skey)
			// 储存绑定实名与否到本地用来判断是否曾经实名过
			if (result.identified) {
				uni.setStorageSync('identified', result.identified)
			}
			console.log("更新skey:" + uni.getStorageSync('loginFlag'))
			resolve()
		}).catch(() => {
			reject()
		})	
	})
	return promise
}

const bindPhone = (e) => {
	const promise = new Promise((resolve, reject) => {
		let param = {
			skey: e.skey,
			encryptedData: e.encryptedData,
			iv: e.iv
		}
		phoneDo(param).then((res) => {
			// 储存绑定实名与否到本地用来判断是否曾经实名过
			uni.setStorageSync('identified', true)
			resolve(res)
		}).catch(() => {
			reject()
		})	
	})
	return promise
}

const bindToken = (e) => {
	const promise = new Promise((resolve, reject) => {
		let param = {
			skey: e.skey,
			token: e.token
		}
		tokenDo(param).then((res) => {
			// 储存绑定实名与否到本地用来判断是否曾经实名过
			uni.setStorageSync('identified', true)
			resolve(res)
		}).catch(() => {
			reject()
		})	
	})
	return promise
}

export {
	login, //申请skey并保存usrInfo和skey到本地
	bindPhone, //通过手机号绑定实名
	bindToken, //通过令牌绑定实名
	getCode, //获取code
	getProvider //获取服务商信息
}
