<template>
	<view class="page">
		<!-- 抽屉侧滑菜单start -->
		<uni-drawer :visible="visible">
			<view>
				<cmd-nav-bar title="产品目录" icon-one="chevron-left" font-color="white" background-color="#133386" @iconOne="handleDrawerClose"></cmd-nav-bar>
				<cmd-page-body type="top">
					<cmd-transition name="fade-up">
						<view v-for="(item) in productList" :key="item.id">
							<view :data-prodId="item.id" @click="showProduct">
								<cmd-cel-item :title="item.name" slot-left arrow >
									<cmd-icon prefix-class="iconfont" :type="item.iconType" size="24" color="#133386"></cmd-icon>
								</cmd-cel-item>
							</view>
						</view>
					</cmd-transition>
				</cmd-page-body>

			</view>
		</uni-drawer>
		<!-- 抽屉侧滑菜单end -->


		<top @catalogClicked="this.visible = !this.visible"></top>
		<!-- 顶部蓝色导航栏end -->

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
					<view :data-courseId="item.id" @click="handleCourseShow">
						<cmd-cel-item :title="item.title" slot-left>
							{{ item.chapterNum }} | 
						</cmd-cel-item>
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
					<view class="divider-line" />
					<text class="prod-example-title">
						案例{{index + 1}} | {{ item.title }}
					</text>
					<div class="rich-text-style" v-html="item.htmlContent"></div>
				</view>
			</view>
			<!-- 应用实例end -->
		</view>
		<!-- 简介、场景、课程、实例 end -->

	</view>
</template>

<script>
	import cmdNavBar from "@/components/cmd-nav-bar/cmd-nav-bar.vue"
	import cmdIcon from "@/components/cmd-icon/cmd-icon.vue"
	import cmdPageBody from "@/components/cmd-page-body/cmd-page-body.vue"
	import cmdTransition from "@/components/cmd-transition/cmd-transition.vue"
	import cmdCelItem from "@/components/cmd-cell-item/cmd-cell-item.vue"
	import top from '@/components/top'
	import uniDrawer from "@/components/uni-drawer/uni-drawer.vue"
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
			top,
			uniDrawer,
			cmdNavBar,
			cmdPageBody,
			cmdTransition,
			cmdCelItem,
			cmdIcon
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
		onLoad(params) {
			this.prodId = params.prodId;

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
			})
		},
		onShow() {
			this.refresh()
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
