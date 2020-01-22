<template>
	<view class="page" :style="{ height: height_home + 'px' }">
		<view class="task-pane">
            <navigator class="task-one-container" url="testList/testList" open-type="redirect" hover-class="navigator-hover">
				<i class="iconfont icon-kaoshi2 task-one-icon"/>
				<p class="task-txt">考试</p>
            </navigator>
            <navigator class="task-one-container" url="testHist/testHist" open-type="redirect" hover-class="navigator-hover">
				<i class="iconfont icon-jindudengdai task-one-icon"/>
				<p class="task-txt">历史</p>
			</navigator>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				height_home: 1400
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
		methods: {
		}
	}
</script>

<style>
	@import url("test.css");
</style>
