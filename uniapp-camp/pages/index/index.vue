<template>
	<view class="page">
		<!-- 轮播图 start -->
		<swiper :indicator-dots="true" :autoplay="true" class="carousel">
			<swiper-item v-for="item in carouselList" :key="item.id">
				<image :src="item.image" class="carousel"></image>
			</swiper-item>
		</swiper>
		<!-- 轮播图 end -->

		<!-- 九宫格 start -->
		<view class="page-block" style="margin-top: 20upx;">
			<uni-grid :column="3" :show-border="false" :square="false">
				<uni-grid-item class="grid-shadow" v-for="item in productList" :key="item.id">
					<view class="grid-item-wrapper" :data-prodId="item.id" @click="showProduct">
						<image :src="item.favicon" class="grid-item-favicon"></image>
						<text class="grid-item-text">{{item.name}}</text>
					</view>
				</uni-grid-item>
			</uni-grid>
		</view>
		<!-- 九宫格 end -->

	</view>
</template>

<script>
	import uniGrid from "@/components/uni-grid/uni-grid.vue"
	import uniGridItem from "@/components/uni-grid-item/uni-grid-item.vue"
	// import common from "../../common/common.js";
	import trailerStars from "../../components/trailerStars.vue";
	import config from '@/config/config.js'

	export default {
		data() {
			return {
				carouselList: [],
				productList: []
				// hotSuperHeroList: [],
				// hotTrailerList: [],
				// guessULike: [],
				// animationData: {},
				// animationDataArr: [{}, {}, {}, {}, {}],
				// pics: [] // 放大预览的图片数组
			}
		},
		components: {
			// trailerStars,
			uniGrid,
			uniGridItem
		},
		onUnload() {
			// // #ifdef APP-PLUS || MP-WEIXIN
			// // 页面卸载的时候,清楚动画数据
			// this.animationData = {};
			// this.animationDataArr = [{}, {}, {}, {}, {}];
			// // #endif
		},
		onPullDownRefresh() {
			this.refresh();
		},
		// #ifdef MP-WEIXIN
		// 页面被隐藏的时候，暂停播放
		onHide() {
			// if(this.videoContext) {
			// 	this.videoContext.pause();
			// }
		},
		// #endif
		onLoad() {

			// #ifdef APP-PLUS || MP-WEIXIN
			// 在页面创建的时候,创建一个临时动画对象
			// this.animation = uni.createAnimation();
			// #endif

			// var serverUrl = common.serverUrl;
			console.log(config.getProductsUrl)
			// 请求轮播图数据
			uni.request({
				url: config.getProductCarousel,
				method: 'POST',
				data: {},
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						this.carouselList = res.data.data;
					}

				},
				fail: () => {},
				complete: () => {}
			});

			this.refresh();
		},
		methods: {
			// iAmPlaying(e) {
			// 	// 播放一个视频的时候,需要暂停其他正在播放的视频
			// 	var trailerId = "";
			// 	if (e) {
			// 		trailerId = e.currentTarget.dataset.playingindex;
			// 		this.videoContext = uni.createVideoContext(trailerId);
			// 	}
			// 	var hotTrailerList = this.hotTrailerList;
			// 	for (var i = 0; i < hotTrailerList.length; i ++) {
			// 		var tempId = hotTrailerList[i].id;
			// 		if (tempId != trailerId) {
			// 			uni.createVideoContext(tempId).pause();
			// 		}
			// 	}
			// }
			// ,
			refresh() {
				uni.showLoading({
					mask: true
				});
				uni.showNavigationBarLoading();

				// 请求电影信息
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
							// 采集放大预览图片数组
							// this.pics = retData.map(x => {
							// 	return x.cover
							// });

							// // 热门超英电影预告信息
							// this.hotTrailerList = retData.slice(8, 10);
							// // 猜你喜欢电影信息
							// this.guessULike = retData.slice(3, 8);
						}

					},
					fail: () => {},
					complete: () => {
						uni.hideNavigationBarLoading();
						uni.hideLoading();
						uni.stopPullDownRefresh();
					}
				});
			},
			
			// 点击九宫格跳转到对应产品详情页
			showProduct(e) {
				var prodId = e.currentTarget.dataset.prodid;
				uni.navigateTo({
					url: '../product/product?prodId=' + prodId, 
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},

			// 实现点击后放大预览效果
			lookMe(e) {
				var picIndex = e.currentTarget.dataset.picindex;
				uni.previewImage({
					current: this.pics[picIndex],
					urls: this.pics
				})
			},

			// 实现点赞动画效果
			praiseMe(e) {
				// #ifdef APP-PLUS || MP-WEIXIN
				// 获取点赞的那行条目的index
				var gIndex = e.currentTarget.dataset.gindex;
				// 构建动画数据,并且通过step来表示这组动画的完成
				this.animation.translateY(-60).opacity(1).step({
					duration: 400
				});
				// 导出动画数据到view组件，实现组件的动画效果
				this.animationData = this.animation;
				this.animationDataArr[gIndex] = this.animationData.export();

				// 还原动画
				setTimeout(function() {
					this.animation.translateY(0).opacity(0).step({
						duration: 0
					});
					this.animationData = this.animation;
					this.animationDataArr[gIndex] = this.animationData.export();
				}.bind(this), 500);
				// #endif
			}
		}
	}
</script>

<style>
	@import url("index.css");
</style>
