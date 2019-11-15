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
			<video id="mytrailer" :src="courseDetail.videoTrail" :poster="productDetail.poster" controls class="movie"></video>
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
							<view class="chart-wrapper">
								<text class="iconfont icon-chart18"></text>
							</view>
							<text class="prod-lesson-item-title" v-once>
								{{item.chapterNum}}. {{item.title}}
							</text>
						</view>
					</view>
				</view>
				<!-- 课程章节 end -->
				
				<!-- 应用实例 start -->
				<view v-show="TabCur === 1" class="tabContent-wrapper">
					<view class="prod-example-item-wrapper" v-for="(item, index) in exampleList" :key="item.id">
						<text class="prod-example-title" v-once>
							案例{{index + 1}}: {{item.title}}
						</text>
						<text class="prod-example-content" v-once>
							{{item.plainContent}}
						</text>
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
	import WucTab from '@/components/wuc-tab/wuc-tab.vue';
	import config from '@/config/config.js'

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
				courseId: ''
			}
		},
		methods: {
			handleCourseShow(e) {
				var currentCourseId = e.currentTarget.dataset.courseid;
				this.courseId = currentCourseId;
				this.courseDetail = this.courseList.filter(x => x.id === parseInt(currentCourseId))[0];
			}
		},
		// #ifdef MP-WEIXIN
		// 页面初次渲染完成,获得视频上下文对象
		onReady() {
			this.videoContext = uni.createVideoContext('myTrailer');
		},
		onHide() {
			// 页面被隐藏的时候,暂停播放
			this.videoContext.pause();
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

			// 通过API修改导航栏的属性
			// uni.setNavigationBarColor({
			// 	frontColor: "#ffffff",
			// 	backgroundColor: "#000000"
			// })

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
					// 获取真实数据之前,务必判断状态为success
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
		// 此函数仅仅只支持在小程序端的分享,分享到微信群或者微信好友
		// onShareAppMessage() {
		// 	return {
		// 		title: this.movieDetail.name,
		// 		path: '/pages/movie/movie?trailerId=' + this.productDetail.id
		// 	}
		// },
	}
</script>

<style>
	@import url("course.css");
</style>
