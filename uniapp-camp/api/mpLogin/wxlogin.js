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
				if (res.status == '201') {
					resolve(res.data) // {"userInfo": {...}, "skey": xxx}
				} else {
					reject()
				}
			},
			fail(error) {
				reject()
			}
		})
	}).catch(exception => {
		reject()
	})
	return promise
}

export {
	authDo //向开发者服务器发送code，返回userInfo和skey
}
