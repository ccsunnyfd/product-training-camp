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
					<uni-list v-for="item in productList" :key="`pdList_${item.id}`">
						<uni-list-item class="left-nav-prodList-item" :title="item.name" show-extra-icon="true" :extra-icon="{color: '#bbbbbb',size: '16',type: 'fenbushicunchu3'}">
						</uni-list-item>
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
		<view class="player">
			<video id="mytrailer" :src="productDetail.trailer" :poster="productDetail.poster" controls class="movie"></video>
		</view>
		<!-- 视频end -->

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
				<view class="prod-lesson-wrapper">
					<!-- 分割线start -->
					<view class="line-wrapper">
						<view class="line"></view>
					</view>
					<!-- 分割线end -->
					<view class="prod-lesson-item-wrapper">
						<view class="chart-wrapper">
							<text class="iconfont icon-chart18"></text>
						</view>
						<text class="prod-lesson-item-title">
							超融合产品特点？你能说出几个呢？尊敬的客户
						</text>
					</view>


					<!-- 分割线start -->
					<view class="line-wrapper">
						<view class="line"></view>
					</view>
					<!-- 分割线end -->
					<view class="prod-lesson-item-wrapper">
						<view class="chart-wrapper">
							<text class="iconfont icon-chart38"></text>
						</view>
						<text class="prod-lesson-item-title">
							超融合产品特点？你能说出几个呢？尊敬的客户
						</text>
					</view>
					<!-- 分割线start -->
					<view class="line-wrapper">
						<view class="line"></view>
					</view>
					<!-- 分割线end -->
					<view class="prod-lesson-item-wrapper">
						<view class="chart-wrapper">
							<text class="iconfont icon-chart14"></text>
						</view>
						<text class="prod-lesson-item-title">
							超融合产品特点？你能说出几个呢？尊敬的客户
						</text>
					</view>
					<!-- 分割线start -->
					<view class="line-wrapper">
						<view class="line"></view>
					</view>
					<!-- 分割线end -->
					<view class="prod-lesson-item-wrapper">
						<view class="chart-wrapper">
							<text class="iconfont icon-chart34"></text>
						</view>
						<text class="prod-lesson-item-title">
							超融合产品特点？你能说出几个呢？尊敬的客户
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
				<view class="prod-example-item-wrapper" v-for="(item, index) in exampleList" :key="`example_${item.id}`">
					<text class="prod-example-title" v-once>
						案例{{index + 1}}: {{item.title}}
					</text>
					<text class="prod-example-content" v-once>
						{{item.plainContent}}
					</text>
				</view>
			</view>
			<!-- 应用实例end -->
		</view>
		<!-- 简介、场景、课程、实例 end -->


		<!-- 剧情介绍start -->
		<!-- 	<view class="plots-block">
		<view class="plots-title">
			剧情介绍：
		</view>
		<view class="plots-desc">
			{{movieDetail.plotDesc}}
		</view>
	</view> -->
		<!-- 剧情介绍end -->
	</view>
</template>

<script>
	import uniDrawer from "@/components/uni-drawer/uni-drawer.vue"
	import uniList from '@/components/uni-list/uni-list.vue'
	import uniListItem from '@/components/uni-list-item/uni-list-item.vue'

	export default {
		data() {
			return {
				productList: [],
				productDetail: {},
				exampleList: [],
				visible: false
			}
		},
		components: {
			uniDrawer,
			uniList,
			uniListItem
		},
		methods: {
			handleDrawerOpen() {
				this.visible = true;
			},
			handleDrawerClose() {
				this.visible = false;
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
			var prodId = params.prodId;

			// 通过API修改导航栏的属性
			// uni.setNavigationBarColor({
			// 	frontColor: "#ffffff",
			// 	backgroundColor: "#000000"
			// })

			var serverUrl = this.serverUrl;
			// 请求产品信息
			uni.request({
				url: serverUrl + '/product/detail?prodId=' + prodId,
				method: 'GET',
				data: {},
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						this.productDetail = res.data.data;
					}
					uni.setNavigationBarTitle({
						title: this.productDetail.name
					})
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
				url: serverUrl + '/example/list?prodId=' + prodId,
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

			// 请求产品列表
			uni.request({
				url: serverUrl + '/product/list',
				method: 'POST',
				data: {},
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						var retData = res.data.data;
						// 产品信息
						this.productList = retData;
					}

				},
				fail: () => {},
				complete: () => {}
			});


		},
		// 此函数仅仅只支持在小程序端的分享,分享到微信群或者微信好友
		onShareAppMessage() {
			return {
				title: this.movieDetail.name,
				path: '/pages/movie/movie?trailerId=' + this.productDetail.id
			}
		},
		// 监听导航栏的按钮
		onNavigationBarButtonTap(e) {
			// #ifdef APP-PLUS
			var index = e.index;
			var movieDetail = this.movieDetail;
			var trailerId = movieDetail.id;
			var trailerName = movieDetail.name;
			var cover = movieDetail.cover;
			var poster = movieDetail.poster;
			// index为0则分享
			if (index == 0) {
				uni.share({
					provider: 'weixin',
					type: 0,
					title: 'NEXT超英预告：《' + trailerName + '》',
					href: 'http://localhost/#/pages/movie/movie?trailerId=' + trailerId,
					summary: 'NEXT超英预告：《' + trailerName + '》',
					imageUrl: cover,
					success: () => {}
				});
			}
			// #endif
		}
	}
</script>

<style>
	@import url("product.css");
</style>
