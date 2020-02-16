<template>
	<view class="page">
		<view v-if="status===1 || status===3">
			<view class="index-title-wrapper">
				<span class="index-title">进行中的考试</span>
			</view>
			<view class="test-panel-wrapper">
				<view
				 class="page-block test-card-wrapper"
				 v-for="record in onGoingList"
				 :key=record.id
				 >
					<view class="test-card-container">
						<view class="test-card-icon-container">
							<i class="iconfont icon-jjj test-card-icon"></i>
						</view>
						<view class="test-card-content-container">
							<view class="test-card-item-title-wrapper">
								<view class="test-card-item-title">
									{{record.title}}
								</view>
								<view class="test-card-item-status">
									进行中
								</view>
							</view>
					<!-- 		<view class="test-card-item-time-valid-wrapper">
								<span class="test-card-item-time-valid">{{record.updatedAt}}</span>
							</view> -->
							<view class="test-card-item-time-left-wrapper">
								<span class="test-card-item-time-left">
									还剩:
									<span class="test-card-item-time-left-data">{{timeLeft(record.createdAt, record.timeLimit)[0]}}分{{timeLeft(record.createdAt, record.timeLimit)[1]}}秒</span>
								</span>
							</view>
							<view class="test-card-item-button-startTest-wrapper">
								<view class="test-card-item-button-startTest">
									<button type="primary" size="mini" :data-testid="record.testId" @click="handleJumpIntoTest">继续考试</button>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="line"></view>
		
		<view v-if="status===2 || status===3">
			<view class="index-title-wrapper">
				<span class="index-title">可参加的考试</span>
			</view>
			<view class="test-panel-wrapper">
				<view
				 class="page-block test-card-wrapper"
				 v-for="test in readyList"
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
									可参加
								</view>
							</view>
						<!-- 	<view class="test-card-item-time-valid-wrapper">
								<span class="test-card-item-time-valid">{{test.updatedAt}}</span>
							</view> -->
							<view class="test-card-item-time-left-wrapper">
								<span class="test-card-item-time-left">
									限时:
									<span class="test-card-item-time-left-data">{{test.timeLimit}}分钟</span>
								</span>
							</view>
							<view class="test-card-item-button-startTest-wrapper">
								<view class="test-card-item-button-startTest">
									<button type="primary" size="mini" :data-testid="test.id" @click="handleJumpIntoTest">开始考试</button>
								</view>
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
				// status: 1. 进行中 2. 可参加 3. 全部
				status: 3,
				readyList: [],
				onGoingList: [],
				timeLeftMin: 0,
				timeLeftSec: 0
			}
		},
		onShow() {
			this.refresh()
		},
		onLoad() {
			this.refresh()
		},
		methods: {
			timeLeft(createdAt, timeLimit) {
				const curTime = new Date().getTime()
				const createdTime = new Date(Date.parse(createdAt.replace(/-/g, "/")))
				const timeDifference = curTime - createdTime // 时间差的毫秒数
				const timeDiffInSeconds = parseInt(timeDifference/1000) // 时间差的秒数
				if (timeDiffInSeconds > timeLimit * 60) {
					// 已经超时
					return [0, 0]
				} else {
					const leftTimeInSeconds = timeLimit * 60 - timeDiffInSeconds
					const timeLeftMin = Math.floor(leftTimeInSeconds/60);
					const timeLeftSec = leftTimeInSeconds%60;
					return [timeLeftMin, timeLeftSec]
				}
			},
			refresh() {
				const skey = this.getSkey()
				if(skey) {
					// 请求在线考试数据
					uni.request({
						// url: config.getTestsUrl + '?status=' + '&category=',
						url: config.getTestsUrl + '?skey=' + skey + '&status=3',
						method: 'GET',
						success: res => {
							// 获取真实数据之前,务必判断状态为success
							if (res.data.status === "success") {
								const testList = res.data.data
								this.readyList = testList.ready
								this.onGoingList = testList.onGoing
							}
						},
						fail: () => {},
						complete: () => {}
					})
				}
			},
			// 点击具体考试卡片的继续考试按钮进入到对应考试内容页
			handleJumpIntoTest(e) {
				var testId = e.currentTarget.dataset.testid;
				uni.navigateTo({
					url: '../testGoingOn/testGoingOn?testId=' + testId, 
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
