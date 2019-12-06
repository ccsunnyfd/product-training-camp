<template>
	<view class="info-padding">
		<cmd-nav-bar back title="信息设置"></cmd-nav-bar>
		<cmd-page-body type="top">
			<cmd-transition name="fade-up">
				<view v-if="userIsLogin">
					<cmd-cel-item title="头像" slot-right arrow>
						<cmd-avatar :src="userInfo.avatarUrl"></cmd-avatar>
					</cmd-cel-item>
					<!-- <cmd-cel-item title="积分" addon="566" arrow></cmd-cel-item> -->
					<cmd-cel-item title="性别" :addon="getGender" arrow></cmd-cel-item>
					<cmd-cel-item title="昵称" :addon="userInfo.nickName" arrow></cmd-cel-item>
					<cmd-cel-item title="省份" :addon="userInfo.province" arrow></cmd-cel-item>
					<cmd-cel-item title="区域" :addon="userInfo.city" arrow></cmd-cel-item>
					<!-- <cmd-cel-item title="修改密码" @click="fnClick('modify')" arrow></cmd-cel-item> -->
					<button class="btn-logout" @click="logout">退出登录</button>
				</view>
			</cmd-transition>
		</cmd-page-body>
	</view>	
</template>

<script>
	import cmdNavBar from "@/components/cmd-nav-bar/cmd-nav-bar.vue"
	import cmdPageBody from "@/components/cmd-page-body/cmd-page-body.vue"
	import cmdTransition from "@/components/cmd-transition/cmd-transition.vue"
	import cmdCelItem from "@/components/cmd-cell-item/cmd-cell-item.vue"
	import cmdAvatar from "@/components/cmd-avatar/cmd-avatar.vue"
	import config from '@/config/config.js'
	
	export default {
		data() {
			return {
				userIsLogin: false,
				userInfo: {}
			}
		},
		components: {
			cmdNavBar,
			cmdPageBody,
			cmdTransition,
			cmdCelItem,
			cmdAvatar
		},
		computed: {
			getGender: function () {
				if(this.userIsLogin && this.userInfo) {
					if (this.userInfo.gender === 1) {
						return '男'
					} else if (this.userInfo.gender === 2) {
						return '女'
					} else {
						return '未知'
					}
				}
			}
		},
		onShow() {
			// 使用挂载方法获取用户数据
			var userInfo = this.getGlobalUser("userInfo");
			if (userInfo != null) {
				this.userIsLogin = true;
				this.userInfo = JSON.parse(userInfo);
			} else {
				this.userIsLogin = false;
				this.userInfo = {};
			}
		},
		methods: {
			cleanStorage() {
				uni.clearStorageSync();
				uni.showToast({
					title: "清理缓存成功",
					mask: false,
					duration: 1500
				})
			},
			logout() {
				let skey = this.getSkey()
				if (skey) {
					// 发起退出登录的请求
					uni.request({
						url: config.logoutUrl + '?skey=' + skey,
						method: 'POST',
						data: {},
						success: res => {
							if (res.data.status == 200) {
								uni.removeStorageSync("userInfo")
								uni.removeStorageSync("loginFlag")
								uni.navigateTo({
									url: "/pages/login/index"
								})
							}
						},
						fail: () => {},
						complete: () => {}
					});
				} else {
					uni.navigateTo({
						url: "/pages/login/index"
					})
				}
			}
		}
	}
</script>

<style>
	.info-padding {
		padding: 25upx 25upx;
	}
	.btn-logout {
		margin-top: 100upx;
		width: 80%;
		border-radius: 50upx;
		font-size: 16px;
		color: #fff;
		background: linear-gradient(to right, #133386, #6a8ce2);
	}

	.btn-logout-hover {
		background: linear-gradient(to right, #1333dd, #6a8cfa);
	}
</style>
