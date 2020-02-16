<template>
	<view :style="{ height: height_home + 'px' }" class="page card-wrapper">
			<view class="content-tab">
				<view class="exam-result-score">
					<span class="score">{{score}}</span>
					<span>分</span>
				</view>
			</view>
			<view class="nav-wrapper">
				<view class="exam-look-analye" @click="handleExamCheck">
					查看试卷
				</view>
				<view class="exam-again-test" @click="handleExamAgain">
					再考一次
				</view>
			</view>
	</view>
</template>

<script>
	import config from '@/config/config.js'

	export default {
		data() {
			return {
				height_home: 1400,
				testId: '',
				recordId: '',
				score: ''
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
		onLoad(params) {
			this.recordId = params.recordId
			this.testId = params.testId
			this.score = params.score
		},
		methods: {
			handleExamCheck() {
				uni.navigateTo({
					url: '../testCheck/testCheck?recordId=' + this.recordId, 
					success: res => {},
					fail: () => {},
					complete: () => {}
				})
			},
			handleExamAgain() {
				uni.redirectTo({
					url: '../testGoingOn/testGoingOn?testId=' + this.testId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				})

			}
		}
	}
</script>

<style>
	@import url("testResult.css");
</style>
