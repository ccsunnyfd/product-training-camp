<template>
	<view class="login">
		<view class="logo-wrapper">
			<image src="../../static/images/loginLogo.png" class="logo">
			</image>
		</view>
		<view class="button-wrapper">
			<button type="primary" class="login-button" @click="openSetting" v-if="status==0">微信登录</button>
			<button type="primary" class="login-button" open-type="getUserInfo" @getuserinfo="getuserinfo" v-else>微信登录</button>
			<!-- <view class="phone-login">手机号登录/注册</view> -->
		</view>

	</view>
</template>

<script>
	import {
		getProvider,
		login,
		getCode
	} from '@/api/request/login/loginUtils.js'
	import {
		getAuthSetting
	} from '@/api/request/login/getAuthSetting.js'

	export default {
		data() {
			return {
				code: '',  // 小程序授权码
				status: 0 //2未操作 1已经授权  0拒绝授权
			}
		},
		onShow() {
			(async () => {
				//获取授权状态 2未操作 1已经授权  0拒绝授权
				this.status = await getAuthSetting()
				//获取服务商信息 "provider":["weixin"]
				let provider = await getProvider();
				//获取code "009Lbpsj2ggKpE8AM4tj2604sj7Xbpsp"
				this.code = await getCode(provider[0])
			})()
		},
		methods: {
			getuserinfo(e) {
				// e的内容：
				// {type: "getuserinfo", timeStamp: 2937, target: {…}, currentTarget: {…}, detail: {…}, …}
				// currentTarget: {id: "", offsetLeft: 32, offsetTop: 212, dataset: {…}}
				// detail: {errMsg: "getUserInfo:ok", rawData: "{"nickName":"xxx","gender":1,"language":"zh_CN",…xxx"}", signature: "xxx", encryptedData: "xxx", iv: "xxx", …}
				// mp: {type: "getuserinfo", timeStamp: 2937, target: {…}, currentTarget: {…}, detail: {…}}
				// target: {id: "", offsetLeft: 32, offsetTop: 212, dataset: {…}, errMsg: "getUserInfo:ok", …}
				// type: "getuserinfo"
				// getUserInfo为ok表明有授权
				if (!this.code) {
					uni.showToast({
						icon: 'none',
						title: '正在加载中，稍等一下',
						duration: 2000
					});
					return
				}
				if (e.detail && e.detail.errMsg == 'getUserInfo:ok') {
					e.detail.code = this.code //把code信息添加到detail中，一起作为login的参数
					login(e.detail)
				} else {
					//授权拒绝之后的回调index
					uni.showToast({
						icon: 'none',
						title: '已拒绝授权',
						duration: 2000
					});
					//用户拒绝授权
					this.status = 0
				}
			},
			openSetting() {
				uni.showModal({
					title: '提示',
					content: '你已经取消过授权，需设置授权权限',
					confirmText: '设置',
					success: function(res) {
						if (res.confirm) {
							uni.openSetting({
								success(res) {
								}
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			}
		}
	}
</script>

<style>
	@import url("index.css");
</style>
