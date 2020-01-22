<template>
	<view class="page">
		<view v-for="(userR) in userRecordList" :key="userR.id" class="page-block record-pane">
			<navigator :url="'../testResult/testResult?recordId=' + userR.id + '&testId=' + userR.record.id + '&score=' + userR.record.score" open-type="navigate">
				<view class="item-floor-container">
					<view class="item-icon-and-title-wrapper">
						<view class="item-icon-container">
							<i class="iconfont icon-chengji5 item-icon"></i>
						</view>
						<view class="item-title ellipsis">{{userR.record.title}}</view>
					</view>
					<view class="item-status-container">
						查看
						<i class="iconfont icon-iconfontyoujiantou item-icon"></i>
					</view>
				</view>
				<view class="item-split"></view>
				<view class="item-ceil">
					<view class="item-label">
						分数：
						<span class="label-data">{{userR.record.score}}</span>
					</view>
					<view class="item-time-range">
						{{userR.updatedAt}}
					</view>
				</view>
			</navigator>
		</view>
	</view>
</template>

<script>
	import config from '@/config/config.js'
	const skey = uni.getStorageSync('loginFlag')

	export default {
		data() {
			return {
				userRecordList: []
			}
		},
		onLoad() {
			// 请求用户历史记录列表
			uni.request({
				url: config.getUserRecordListUrl + '?skey=' + skey,
				method: 'GET',
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						this.userRecordList = res.data.data
					}
				},
				fail: () => {},
				complete: () => {}
			})
		},
		methods: {

		}
	}
</script>

<style>
	@import url("testHist.css");
</style>
