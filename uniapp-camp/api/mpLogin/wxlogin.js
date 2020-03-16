import api from '@/config/config'
const authDo = function(param) {
	const promise = new Promise((resolve, reject) => {
		uni.request({
			url: api.loginUrl,	
			method: "POST",
			data: {
				code: param.code,
				encryptedData: param.encryptedData,
				iv: param.iv
			},
			success(res) {
				res = res.data
				if (res.status === '201') {
					resolve(res.data) // {"userInfo": {...}, "skey": xxx}
				} else {
					reject()
				}
			},
			fail(error) {
				console.log(error)
				reject()
			}
		})
	})
	return promise
}

const phoneDo = function(param) {
	const promise = new Promise((resolve, reject) => {
		uni.request({
			url: api.bindPhoneUrl,	
			method: "POST",
			data: {
				skey: param.skey,
				encryptedData: param.encryptedData,
				iv: param.iv
			},
			success(res) {
				res = res.data
				if (res.status === '201') {
					resolve(res.data) // 手机号绑定实名成功,返回实名对象
				} else {
					reject()
				}
			},
			fail(error) {
				console.log(error)
				reject()
			}
		})
	})
	return promise
}

const tokenDo = function(param) {
	const promise = new Promise((resolve, reject) => {
		uni.request({
			url: api.bindTokenUrl,	
			method: "POST",
			data: {
				skey: param.skey,
				token: param.token
			},
			success(res) {
				res = res.data
				if (res.status === '201') {
					resolve(res.data) // 令牌绑定实名成功,返回实名对象
				} else {
					reject()
				}
			},
			fail(error) {
				reject()
			}
		})
	})
	return promise
}

export {
	authDo, //向开发者服务器发送code，返回userInfo和skey
	phoneDo, //通过手机号绑定实名
	tokenDo //通过令牌绑定实名
}