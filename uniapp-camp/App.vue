<script>
	import { checkLoginStatus } from '@/api/request/login/checkLoginStatus.js'
	export default {
		// // app全局数据
		// globalData: {
		// 	userInfo: null
		// },
		onLaunch: async function() {
			// #ifdef MP
			//检查登录状态
			await checkLoginStatus()
			// #endif
		},
		onShow: function() {
			// console.log('App Show')
		},
}
</script>

<style>
	/*每个页面公共css */
	.page {
		width: 100%;
		height: 100%;
		background-color: #f7f7f7;
		/* position: absolute; */
	}

	.page-block {
		background-color: #ffffff;
		margin: 20upx 20upx 20upx 20upx;
	}

	.line-wrapper {
		padding: 0upx 20upx;
	}

	.line {
		height: 1px;
		background-color: #DBDBDA;
	}
</style>
