//checkSession.js
//检测session是否过期
//0未过期 1已过期

const check = () => {
	const promise = new Promise((resolve, reject) => {
		uni.checkSession({
			success() {
				console.log('状态未过期')
				//未过期
				resolve(0)
			},
			fail() {
				console.log('状态已过期')
				//已过期
				resolve(1)
			}
		})
	}).catch(res => {
		uni.showToast({
			icon: 'none',
			title: res.errMsg || '验证session失效',
			duration: 2000
		});
	})

	return promise
}

export default check
