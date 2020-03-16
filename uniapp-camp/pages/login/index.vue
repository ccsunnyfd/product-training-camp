<template>
	<view class="login" :style="{ height: height_home + 'px' }" >
		<view class="logo-wrapper">
			<image src="../../static/images/loginLogo.png" class="logo">
			</image>
		</view>
		<view class="hint">
			柏科数据内部培训使用
		</view>
		<view class="button-wrapper">
			<button type="primary" class="login-button" @click="navToRegPage">我要注册/登录</button>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				height_home: 1400
			}
		},
		onShow() {
			uni.getSystemInfo({
				//获取手机信息
				success: res => {
					this.height_home = res.windowHeight
				},
				fail: () => {
				}
			})
		},
		methods: {
			navToRegPage() {
				uni.navigateTo({
					url: "/pages/login/register"
				})
			}
		}
	}
</script>

<style>
	@import url("index.css");
</style>
