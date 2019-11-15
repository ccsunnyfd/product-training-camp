<template>
	<view class="page">
		<!-- 轮播图 start -->
		<swiper :indicator-dots="true" :autoplay="true" class="carousel">
			<swiper-item v-for="(item) in carouselList" :key="item.id">
				<image :src="item.image" class="carousel"></image>
			</swiper-item>
		</swiper>
		<!-- 轮播图 end -->

		<!-- 九宫格 start -->
		<view class="page-block" style="margin-top: 20upx;">
			<uni-grid :column="3" :show-border="false" :square="false">
				<uni-grid-item class="grid-shadow" v-for="(item) in productList" :key="item.id">
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
	import config from '@/config/config.js'

	export default {
		data() {
			return {
				carouselList: [],
				productList: []
			}
		},
		components: {
			uniGrid,
			uniGridItem
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
			// 请求轮播图数据
			uni.request({
				url: config.getCarouselUrl,
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

				// 请求产品列表信息
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
		}
	}
</script>

<style>
	@import url("index.css");
</style>
