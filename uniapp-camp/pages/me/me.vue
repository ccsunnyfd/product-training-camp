<template>
	<view>
		<view class="person-head linearBg">
			<view v-if="userIsLogin">
				<cmd-avatar
					:src="userInfo.avatarUrl"
					@click="meInfo"
					size="lg"
					:make="{'background-color': '#ffffff'}">		
				</cmd-avatar>
			</view>
			<view v-else>
				<cmd-avatar
					src="https://product-camp.oss-cn-shanghai.aliyuncs.com/images/noavatar_small.gif"
					size="lg"
					:make="{'background-color': '#ffffff'}">		
				</cmd-avatar>  
			</view>
		  
			<view class="person-head-box">
				<view class="person-head-nickname" v-if="userIsLogin">
					{{userInfo.nickName}}
				</view>
				<!-- <view class="person-head-username">ID：slimmer9501</view> -->
				<view v-else>
					<navigator url="@/pages/login/index">
						<view class="person-head-nickname">
							<view @click="gotoLogin">立即登录</view>
						</view>
					</navigator>
				</view>
			</view>
		</view>
		  
		<view class="person-list" v-if="userIsLogin">
			<cmd-cell-item title="常见问题" slot-left arrow>
				<cmd-icon type="message" size="24" color="#133386"></cmd-icon>
			</cmd-cell-item>
			<cmd-cell-item title="成绩查询" slot-left arrow>
				<cmd-icon type="edit" size="24" color="#133386"></cmd-icon>
			</cmd-cell-item>
			<cmd-cell-item title="系统设置" slot-left arrow>
				<cmd-icon type="settings" size="24" color="#133386"></cmd-icon>
			</cmd-cell-item>
			<cmd-cell-item title="检查版本" addon="v1.0" slot-left arrow>
				<cmd-icon type="alert-circle" size="24" color="#133386"></cmd-icon>
			</cmd-cell-item>
		</view>
	</view>
</template>

<script>
	import cmdAvatar from "@/components/cmd-avatar/cmd-avatar.vue"
	import cmdIcon from "@/components/cmd-icon/cmd-icon.vue"
	import cmdCellItem from "@/components/cmd-cell-item/cmd-cell-item.vue"
	
	export default {
		data() {
			return {
				userIsLogin: false,
				userInfo: {}
			}
		},
		components: {
			cmdAvatar,
			cmdCellItem,
			cmdIcon
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
			gotoLogin() {
				uni.navigateTo({
					url: "/pages/login/index"
				})
			},
			// 打开用户信息页
			meInfo() {
				uni.navigateTo({
					url: '/pages/meInfo/meInfo'
				})
			}
		}
	}
</script>

<style>
  .person-head {
    display: flex;
    flex-direction: row;
    align-items: center;
    height: 150px;
    padding-left: 20px;
  }

  .person-head-box {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    margin-left: 10px;
  }

  .person-head-nickname {
    font-size: 18px;
    font-weight: 500;
    color: #fff;
  }

  .person-head-username {
    font-size: 14px;
    font-weight: 500;
    color: #fff;
  }

  .person-list {
    line-height: 0;
  }
</style>
