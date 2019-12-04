<template>
	<view class="page">
		<!-- 顶部蓝色导航栏start -->
		<view class="top-nav-wrapper">
			<view class="top-nav-title-wrapper">
				<text class="top-nav-title">{{courseDetail.chapterNum}}. {{courseDetail.title}}</text>
			</view>
		</view>
		<!-- 顶部蓝色导航栏end -->

		<!-- 视频start -->
		<view class="player">
			<video id="mytrailer"
				:initial-time="initialTime"
				:show-center-play-btn="true"
				:autoplay="true"
				@pause="handlePause"
				@timeupdate="handleTimeUpdate"
				:src="courseDetail.videoTrail"
				:poster="productDetail.poster"
				controls
				class="movie"
			></video>
		</view>
		<!-- 视频end -->

		<!-- tabPane start -->
		<view>
			<wuc-tab :tab-list="tabList" textFlex :tabCur.sync="TabCur" select-class="text-orange"></wuc-tab>
			<view :current="TabCur">
				<!-- 课程章节 start -->
				<view v-show="TabCur === 0" class="tabContent-wrapper">
					<view class="prod-lesson-wrapper" v-for="(item) in courseList" :key="item.id">
						<!-- 分割线start -->
						<view class="line-wrapper">
							<view class="line"></view>
						</view>
						<!-- 分割线end -->
						<view :class="'prod-lesson-item-wrapper' + (parseInt(courseId) === item.id ? ' chapter-chosen' : '')" :data-courseId="item.id" @click="handleCourseShow">
							<text class="prod-lesson-item-title">
								{{item.chapterNum}} | {{item.title}}
							</text>
							<text class="lesson-item-progress">{{calcPercent(item.id)}}%</text>
						<!-- 	<view class="chart-wrapper">
								<text class="iconfont icon-chart18"></text>
							</view> -->
						</view>
					</view>
				</view>
				<!-- 课程章节 end -->
				
				<!-- 应用实例 start -->
				<view v-show="TabCur === 1" class="tabContent-wrapper">
					<view class="prod-example-item-wrapper" v-for="(item) in exampleList" :key="item.id">
						<text class="prod-example-title">
							案例{{index + 1}} | {{item.title}}
						</text>
					<!-- 	<text class="prod-example-content" v-once>
							{{item.plainContent}}
						</text> -->
						<div v-html="item.htmlContent"></div>
					</view>
				</view>
				<!-- 应用实例 end -->
			</view>
		</view>
	</view>
	<!-- tabPane end -->

	</view>
</template>

<script>
	import WucTab from '@/components/wuc-tab/wuc-tab.vue'
	import config from '@/config/config.js'
	import getWatchProg from '@/api/request/watchProg/getWatchProg.js'

	export default {
		components: {
			WucTab
		},
		data() {
			return {
				tabList: [{
						name: '章节目录'
					},
					{
						name: '应用实例'
					}
				],
				TabCur: 0,
				productDetail: {},
				courseList: [],
				courseDetail: {},
				exampleList: [],
				prodId: '',
				courseId: '',
				currentTime: 0,
				duration: 1,
				initialTime: 0,
				watchProg: []
			}
		},
		methods: {
			calcPercent(courseId) {
				const curWatchProg = this.watchProg.filter(x => x.courseId === parseInt(courseId))[0]
				if (curWatchProg) {
					return (curWatchProg.currentProgress / curWatchProg.duration).toFixed(2) * 100
				} else {
					return 0
				}
			},
			refreshWatchProg() {
				// 获取当前观看进度到this.initialTime
				getWatchProg(this.prodId).then(result => {
					this.watchProg = result
					const currentCourseProg = result.filter(item => item.courseId === parseInt(this.courseId))[0]
					if (currentCourseProg) {
						this.initialTime = currentCourseProg.currentProgress
						this.duration = currentCourseProg.duration
					} else {
						this.initialTime = 0
					}
					this.currentTime = this.initialTime
					const formattedTime = this.$pubFuc.secondFormact(this.initialTime)
					if(this.initialTime > 0) {
						uni.showToast({
							title: '从' + formattedTime + '续播',
							icon: 'none',
							duration: 2000
						});
						if (!this.videoContext) {
							this.videoContext = uni.createVideoContext('myTrailer');
						}
					}
				})
			},
			saveProgress(currentTime) {
				const skey = (uni.getStorageSync('loginFlag'));
				if (skey) {
					// 请求保存当前用户的视频进度
					uni.request({
						url: config.saveWatchProgByCouseIdUrl,
						method: 'POST',
						data: {
							skey: skey,
							courseId: this.courseId,
							currentTime: currentTime,
							duration: this.duration
						},
						success: res => {
							console.log('保存用户视频进度')
							// 把当前的this.watchProg也更新一下
						},
						fail: () => {},
						complete: () => {}
					});
				}
			},
			handleTimeUpdate(event) {
				this.currentTime = Math.floor(event.detail.currentTime)
				this.duration = Math.floor(event.detail.duration)
			},
			// 在视频pause\ended时不保存进度,在界面隐藏和卸载前保存当前视频观看进度
			handlePause() {
				// this.saveProgress(this.currentTime)
			},
			// 切换课程视频需要先保存当前视频的观看进度
			handleCourseShow(e) {
				this.saveProgress(this.currentTime)
				var currentCourseId = e.currentTarget.dataset.courseid;
				this.courseId = currentCourseId;
				this.courseDetail = this.courseList.filter(x => x.id === parseInt(currentCourseId))[0];
				const currentCourseProg = this.watchProg.filter(item => item.courseId === this.courseId)[0]
				this.refreshWatchProg()
			}
		},
		// #ifdef MP-WEIXIN
		// 页面初次渲染完成,获得视频上下文对象
		onReady() {
			this.refreshWatchProg()
		},
		onHide() {
			// 页面被隐藏的时候,暂停播放
			this.saveProgress(this.currentTime)
			this.videoContext.pause();
		},
		onUnload() {
			this.saveProgress(this.currentTime)
		},
		onShow() {
			// 页面被再次显示的时候,可以继续播放
			if (this.videoContext) {
				this.videoContext.play();
			}
		},
		// #endif
		onLoad(params) {
			this.prodId = params.prodId;
			this.courseId = params.courseId;

			// 请求课程所属产品名称
			uni.request({
				url: config.getProductDetailUrl + '?prodId=' + this.prodId,
				method: 'GET',
				data: {},
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						this.productDetail = res.data.data;
						uni.setNavigationBarTitle({
							title: this.productDetail.name
						});
					}
				},
				fail: () => {},
				complete: () => {
					// uni.hideNavigationBarLoading();
					// uni.hideLoading();
					// uni.stopPullDownRefresh();
				}
			});

			// 请求课程章节
			uni.request({
				url: config.getCourseUrl + '?prodId=' + this.prodId,
				method: 'GET',
				data: {},
				success: res => {
					if (res.data.status === "success") {
						this.courseList = res.data.data;
						this.courseDetail = this.courseList.filter(x => x.id === parseInt(this.courseId))[0];
					}
				},
				fail: () => {},
				complete: () => {
					// uni.hideNavigationBarLoading();
					// uni.hideLoading();
					// uni.stopPullDownRefresh();
				}
			});
			
			// 请求应用案例
			uni.request({
				url: config.getExampleUrl + '?prodId=' + this.prodId,
				method: 'GET',
				data: {},
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						this.exampleList = res.data.data;
					}
				},
				fail: () => {},
				complete: () => {
					// uni.hideNavigationBarLoading();
					// uni.hideLoading();
					// uni.stopPullDownRefresh();
				}
			});	
		}
	}
</script>

<style>
	@import url("course.css");
</style>
