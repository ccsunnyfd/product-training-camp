<template>
	<view class="page">
		<!-- 抽屉侧滑菜单start -->
		<uni-drawer :visible="visible">
			<view style="padding:0upx 15upx;">
				<view class="left-nav-head-wrapper">
					<view class="left-nav-head-title-wrapper">
						<text class="left-nav-head-title">产品训练营</text>
					</view>
					<view class="left-nav-head-backicon-wrapper" @click="handleDrawerClose">
						<i class="iconfont icon-daohang_jiantou_zuo_dingbu left-nav-head-backicon"></i>
					</view>
				</view>
				<view class="left-nav-prodList-wrapper">
					<uni-list v-for="(item) in productList" :key="item.id">
						<view :data-prodId="item.id" @click="showProduct">
							<uni-list-item class="left-nav-prodList-item" :title="item.name" show-extra-icon="true" :extra-icon="extraIcon(item)">
							</uni-list-item>
						</view>
					</uni-list>
				</view>
			</view>
		</uni-drawer>
		<!-- 抽屉侧滑菜单end -->

		<!-- 顶部蓝色导航栏start -->
		<view class="top-nav-wrapper">
			<view class="top-nav-directory-wrapper" @click="handleDrawerOpen">
				<i class="iconfont icon-mulu top-nav-directoryicon"></i>
				<text class="top-nav-title">产 品 中 心</text>
			</view>
			<view class="top-nav-search-wrapper">
				<i class="iconfont icon-fangdajing top-nav-searchicon"></i>
			</view>
		</view>
		<!-- 顶部蓝色导航栏end -->

		<!-- 视频start -->
		<!-- 		<view class="player">
			<video id="mytrailer" :src="productDetail.trailer" :poster="productDetail.poster" controls class="movie"></video>
		</view> -->
		<!-- 视频end -->

		<!-- 图片start -->
		<view class="prod-img-wrapper">
			<image class="prod-img" :src="productDetail.prodImg"></image>
		</view>
		<!-- 图片end -->

		<!-- 简介、场景、课程、实例 start -->
		<view class="productInfo page-block">
			<!-- 产品简介start -->
			<view class="prod-desc-wrapper">
				<text class="prod-title">
					产品简介
				</text>
				<text class="prod-desc">
					{{productDetail.description}}
				</text>
			</view>
			<!-- 产品简介end -->
			<!-- 场景start -->
			<view class="prod-scenario-wrapper">
				<text class="prod-title">
					适用场景和人群
				</text>
				<text class="prod-scenario">
					{{productDetail.scenario}}
				</text>
			</view>
			<!-- 场景end -->
			<!-- 课程start -->
			<view class="prod-course-wrapper">
				<text class="prod-title">
					课程章节
				</text>
				<view class="progress-box">
					<progress :percent="courseProg" show-info active stroke-width="3" />
				</view>
				<view class="prod-lesson-wrapper" v-for="(item) in courseList" :key="item.id">
					<!-- 分割线start -->
					<view class="line-wrapper">
						<view class="line"></view>
					</view>
					<!-- 分割线end -->
					<view class="prod-lesson-item-wrapper" :data-courseId="item.id" @click="handleCourseShow">
				<!-- 		<view class="chart-wrapper">
							<text class="iconfont icon-chart18"></text>
						</view> -->
						<text class="prod-lesson-item-title" v-once>
							{{ item.chapterNum }}. {{ item.title }}
						</text>
					</view>
				</view>
			</view>
			<!-- 课程end -->

			<!-- 应用实例start -->
			<view class="prod-example-wrapper">
				<text class="prod-title">
					应用实例
				</text>
				<view class="prod-example-item-wrapper" v-for="(item, index) in exampleList" :key="item.id">
					<text class="prod-example-title" v-once>
						案例{{index + 1}}: {{ item.title }}
					</text>
			<!-- 		<text class="prod-example-content" v-once>
						{{ item.plainContent }}
					</text> -->
					<div v-html="item.htmlContent"></div>
				</view>
			</view>
			<!-- 应用实例end -->
		</view>
		<!-- 简介、场景、课程、实例 end -->

	</view>
</template>

<script>
	import uniDrawer from "@/components/uni-drawer/uni-drawer.vue"
	import uniList from '@/components/uni-list/uni-list.vue'
	import uniListItem from '@/components/uni-list-item/uni-list-item.vue'
	import config from '@/config/config.js'
	import getWatchProg from '@/api/request/watchProg/getWatchProg.js'

	export default {
		data() {
			return {
				productList: [],
				productDetail: {},
				exampleList: [],
				courseList: [],
				prodId: '',
				courseProg: 0,
				visible: false			}
		},
		components: {
			uniDrawer,
			uniList,
			uniListItem
		},
		methods: {
			extraIcon(item) {
				return "{color: '#bbbbbb',size: '16', type: " + item.iconType + "}"
			},
			handleDrawerOpen() {
				this.visible = true;
			},
			handleDrawerClose() {
				this.visible = false;
			},
			handleCourseShow(e) {
				var currentCourseId = e.currentTarget.dataset.courseid;
				uni.navigateTo({
					url: '../course/course?prodId=' + this.prodId + ' &courseId=' + currentCourseId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			// 点击侧滑栏导航到对应产品详情页
			showProduct(e) {
				var currentProdId = e.currentTarget.dataset.prodid;
				this.productDetail = this.productList.filter(x => x.id === parseInt(currentProdId))[0];
				uni.setNavigationBarTitle({
					title: this.productDetail.name
				});
				this.prodId = currentProdId;
				this.refresh();
			},
			async refresh() {
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
				// 请求课程章节
				await uni.request({
					url: config.getCourseUrl + '?prodId=' + this.prodId,
					method: 'GET',
					data: {},
					success: res => {
						// 获取真实数据之前,务必判断状态为success
						if (res.data.status === "success") {
							this.courseList = res.data.data;
						}
					},
					fail: () => {},
					complete: () => {
						// uni.hideNavigationBarLoading();
						// uni.hideLoading();
						// uni.stopPullDownRefresh();
					}
				});

				// 获取到课程章节后请求当前用户该产品所有课程的平均观看进度
				getWatchProg(this.prodId).then(result => {
					const watchProg = result
					let totalPercent = 0
					let count = 0
					this.courseList.forEach(item => {
						for(var i = 0; i < watchProg.length; i++) {
							if(watchProg[i].courseId === item.id) {
								totalPercent = totalPercent + watchProg[i].currentProgress / watchProg[i].duration
								break
							}
						}
						count++
					})
					const averageProg = Math.floor(totalPercent / count * 100)
					this.courseProg = averageProg
				})
				
				this.visible = false;
			}
		},
		// #ifdef MP-WEIXIN
		// 页面初次渲染完成,获得视频上下文对象
		// onReady() {
		// 	this.videoContext = uni.createVideoContext('myTrailer');
		// },
		// onHide() {
		// 	// 页面被隐藏的时候,暂停播放
		// 	this.videoContext.pause();
		// },
		// onShow() {
		// 	// 页面被再次显示的时候,可以继续播放
		// 	if (this.videoContext) {
		// 		this.videoContext.play();
		// 	}
		// },
		// #endif
		onLoad(params) {
			this.prodId = params.prodId;

			// 通过API修改导航栏的属性
			uni.setNavigationBarColor({
				frontColor: "#ffffff",
				backgroundColor: "#000000"
			})

			// 请求产品列表
			uni.request({
				url: config.getProductsUrl,
				method: 'POST',
				data: {},
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						var retData = res.data.data;
						// 产品信息
						this.productList = retData;
						// 获取当前产品信息
						this.productDetail = this.productList.filter(x => x.id === parseInt(this.prodId))[0];
						uni.setNavigationBarTitle({
							title: this.productDetail.name
						});
					}
				},
				fail: () => {},
				complete: () => {}
			});

			this.refresh();
		}
		// 此函数仅仅只支持在小程序端的分享,分享到微信群或者微信好友
		// onShareAppMessage() {
		// 	return {
		// 		title: this.movieDetail.name,
		// 		path: '/pages/movie/movie?trailerId=' + this.productDetail.id
		// 	}
		// },
		// 监听导航栏的按钮
		// onNavigationBarButtonTap(e) {
		// 	// #ifdef APP-PLUS
		// 	var index = e.index;
		// 	var movieDetail = this.movieDetail;
		// 	var trailerId = movieDetail.id;
		// 	var trailerName = movieDetail.name;
		// 	var cover = movieDetail.cover;
		// 	var poster = movieDetail.poster;
		// 	// index为0则分享
		// 	if (index == 0) {
		// 		uni.share({
		// 			provider: 'weixin',
		// 			type: 0,
		// 			title: '产品训练营：《' + trailerName + '》',
		// 			href: 'http://localhost/#/pages/course/course?trailerId=' + trailerId,
		// 			summary: '产品训练营：《' + trailerName + '》',
		// 			imageUrl: cover,
		// 			success: () => {}
		// 		});
		// 	}
		// 	// #endif
		// }
	}
</script>

<style>
	@import url("product.css");
</style>
