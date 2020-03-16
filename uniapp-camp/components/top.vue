<template>
	<view class="top bgColor">
		<view class="bar">
			<cmd-icon prefix-class="iconfont" type="icon-mulu" size="21" color="white" @click="handleCatalogClick"></cmd-icon>
<!-- 			<view class="search bgDeepColor">
				<input type="text" placeholder="搜索" placeholder-style="color:rgba(255,255,255,.8)" />
				<i class="iconfont icon-fangdajing icon"></i>
			</view> -->
		</view>
	</view>
</template>

<script>
	import cmdIcon from "@/components/cmd-icon/cmd-icon.vue"
	export default {
		name: "top",
		components: {
			cmdIcon
		},
		data() {
			return {
			}
		},
		methods:{
			handleCatalogClick() {
				this.$emit("catalogClicked")
			}
		}
	}
</script>

<style  lang="scss" scoped>
	.top{
		z-index: 100;
		position: fixed;
		padding: 20upx 20upx;
		width:100%;
		box-sizing: border-box;
		color: #ffffff;
		font-size: 32upx;
		.bar{
			display: flex;
			flex-direction: row;
			line-height: 64upx;

			.barContent{
				display: flex;
				width: 360upx;
				margin: 0 auto;
				justify-content: space-between;
				
				.sel{
					font-size: 36upx;
					font-weight: 600;
				}
			}
		
			.search{
				width: 90%;
				position: relative;
				margin-left: 20upx;
				// padding: 10upx 20upx;
				height: 60upx;
				line-height: 55upx;
				box-sizing: border-box;
				color: rgba(255,255,255,1);
				input{
					padding-left: 60upx;
				}
				.icon{
					position: absolute;
					top: 5upx;
					left: 10upx;
					width: 40upx;
					height: 40upx;
				}
			}
		}
	}
</style>
