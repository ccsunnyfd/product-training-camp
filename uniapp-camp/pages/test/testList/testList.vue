<template>
	<view class="page">
		<view class="index-title-wrapper">
			<span class="index-title">全部考试</span>
		</view>
		<view class="test-panel-wrapper">
			<view
			 class="page-block test-card-wrapper"
			 v-for="test in testList"
			 :key=test.id
			 >
				<view class="test-card-container">
					<view class="test-card-icon-container">
						<i class="iconfont icon-jjj test-card-icon"></i>
					</view>
					<view class="test-card-content-container">
						<view class="test-card-item-title-wrapper">
							<view class="test-card-item-title">
								{{test.title}}
							</view>
							<view class="test-card-item-status">
								进行中
							</view>
						</view>
						<view class="test-card-item-time-valid-wrapper">
							<span class="test-card-item-time-valid">2019-12-30 10:05 ~ 2020-01-02 10:05</span>
						</view>
						<view class="test-card-item-time-left-wrapper">
							<span class="test-card-item-time-left">
								剩余:
								<span class="test-card-item-time-left-data">55分钟</span>
							</span>
						</view>
						<view class="test-card-item-button-startTest-wrapper">
							<view class="test-card-item-button-startTest">
								<button type="primary" size="mini" :data-testid="test.id" @click="handleJumpIntoTest">继续考试</button>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import config from '@/config/config.js'
	
	export default {
		data() {
			return {
				testList: []
			}
		},
		onLoad() {
			// 请求在线考试数据
			uni.request({
				// url: config.getTestsUrl + '?status=' + '&category=',
				url: config.getTestsUrl,
				method: 'GET',
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						this.testList = res.data.data;
					}
		
				},
				fail: () => {},
				complete: () => {}
			});
		},
		methods: {
			// 点击具体考试卡片的继续考试按钮进入到对应考试内容页
			handleJumpIntoTest(e) {
				var testId = e.currentTarget.dataset.testid;
				uni.navigateTo({
					url: '../test/testGoingOn/testGoingOn?testId=' + testId, 
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}
		}
	}
</script>

<style>
	@import url("testList.css");
</style>
