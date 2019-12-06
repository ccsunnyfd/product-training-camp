import config from '@/config/config.js'

const getWatchProg = (prodId) => {
	return new Promise((resolve, reject) => {
		//自定义登录态
		const skey = uni.getStorageSync('loginFlag');
		if (skey) {
			uni.request({
				url: config.getWatchProgByProdIdUrl,
				method: 'GET',
				data: {
					skey: skey,
					prodId: prodId
				},
				success: res => {
					if (res.data.status === 'success') {
						resolve(res.data.data)
					}
				},
				fail: () => {
					reject()
				},
				complete: () => {}
			});
		} else {
			reject()
		}					
	})
}

export default getWatchProg //获取当前产品当前用户所有课程观看进度
