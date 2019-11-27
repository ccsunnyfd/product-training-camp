//checkSession.js
//检测session是否过期

const check = () => {
	return new Promise((resolve, reject) => {
		uni.checkSession({
			success() {
				console.log('状态未过期')
				resolve('valid')
			},
			fail() {
				console.log('状态已过期')
				resolve('expire')
			}
		})
	})
}

export default check
