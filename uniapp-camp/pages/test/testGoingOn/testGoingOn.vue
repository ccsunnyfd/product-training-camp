<template>
	<view v-if="questionList" :style="{ height: height_home + 'px' }" class="page card-wrapper">
		<view class="header-wrapper">
			<view class="header-layerout">
				<view class="header-hint-text">
					{{qTypes[questionList[currentIndex].qType-1]}}(共{{questionList.length}}题，合计100.0分)
				</view>
				<view class="header-hint-number">
					<span class="header-hint-number-cur">
						{{currentIndex + 1}}
					</span>
					<span class="header-hint-number-all">
						/{{questionList.length}}
					</span>
				</view>
			</view>
		</view>
		<form @submit="formSubmit">
			<view class="scroll-box-wrapper">
				<view class="scroll-box">
					<view v-for="(question, index) in questionList" :key="question.id">
						<view v-show="currentIndex === index" class="card">
							<view class="question-title">
								{{question.title}}({{question.points}}分)
							</view>
							<radio-group v-if="question.qType !== 3" :name="question.id">
								<view class="option-wrapper" v-for="(option) in question.optionAndRight.optionList" :key="option.id">
									<label>
										<radio :value="option.id" />
										<text class="option-words">
											{{option.text}}
										</text>
									</label>
								</view>
							</radio-group>
							<checkbox-group v-else :name="question.id">
								<view class="option-wrapper" v-for="(option) in question.optionAndRight.optionList" :key="option.id">
									<label>
										<checkbox :value="option.id" />
											<text class="option-words">
												{{option.text}}
											</text>
										</checkbox>
									</label>
								</view>
							</checkbox-group>
						</view>
					</view>
				</view>
			</view>
			<view class="nav-wrapper">
				<button v-show="currentIndex !== 0" @click="jumpToPrev">上一题</button>
				<button v-show="currentIndex !== questionList.length - 1" type="primary" @click="jumpToNext">下一题</button>
				<button v-show="currentIndex === questionList.length - 1" form-type="submit">提交试卷</button>
			</view>
		</form>
	</view>
</template>

<script>
	import config from '@/config/config.js'
	const skey = uni.getStorageSync('loginFlag')

	export default {
		data() {
			return {
				height_home: 1400,
				qTypes: ['判断题', '单选题', '多选题'],
				testId: '',
				currentIndex: 0,
				questionList: []
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
		onLoad(params) {
			this.testId = params.testId
			uni.showLoading({
				mask: true
			});

			// 请求当前考试数据
			uni.request({
				// url: config.getTestsUrl + '?status=' + '&category=',
				url: config.getTestByIdUrl + '?id=' + this.testId,
				method: 'GET',
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						this.questionList = res.data.data.questionList;
					}
				},
				fail: () => {},
				complete: () => {
					uni.hideLoading();
				}
			});
		},
		methods: {
			jumpToNext() {
				this.currentIndex = this.currentIndex + 1
			},
			jumpToPrev() {
				this.currentIndex = this.currentIndex - 1
			},
			formSubmit(e) {
				const that = this
				const formdata = e.detail.value
				let flagArr = []
				let completeFlag = true
				Object.keys(formdata).forEach((key) => {
					const val = formdata[key]
					if ((typeof val) === 'string') {
						if (val === '') {
							flagArr.push(0)
							completeFlag = false
						} else {
							flagArr.push(1)
						}
					} else if (val instanceof Array) {
						if (val.length === 0) {
							flagArr.push(0)
							completeFlag = false
						} else {
							flagArr.push(1)
						}
					}
				})
				let hintStr = ''
				if(!completeFlag) {
					for(let i=0; i<flagArr.length; i=i+1) {
						if (flagArr[i] === 0) {
							if (hintStr === '') {
								hintStr = '' + (i + 1)
							} else {
								hintStr = hintStr + '、' + (i + 1)
							}
						}
					}
				}
				uni.showModal({
					title: completeFlag === true ? '确认提交吗?': '还有题目未完成，确认提交吗?',
					content: completeFlag === true ? ':-) 全部题目已完成': '未完成：' + hintStr,
					success: function (res) {
						if (res.confirm) {
							Object.keys(formdata).forEach((key) => {
								const val = formdata[key]
								if ((typeof val) === 'string') {
									if (val === '') {
										formdata[key] = []
									} else {
										formdata[key] = [val]
									}
								}
							})
							// console.log(formdata)
							// 提交考卷
							uni.showLoading({
								mask: true
							})
							uni.request({
								// url: config.getTestsUrl + '?status=' + '&category=',
								url: config.submitTestUrl,
								method: 'POST',
								// header: {
								// 	'content-type': 'application/x-www-form-urlencoded'
								// },
								data: {
									skey: skey,
									testId: that.testId,
									answerMap: formdata
								},
								success: res => {
									// 获取真实数据之前,务必判断状态为success
									if (res.data.status === "success") {
										const testRecord = res.data.data
										uni.redirectTo({
											url: '../testResult/testResult?score=' + testRecord.score + '&testId=' + testRecord.testId + '&recordId=' + testRecord.recordId,
											success: res => {},
											fail: () => {},
											complete: () => {}
										})
									}
								},
								fail: () => {},
								complete: () => {
									uni.hideLoading()
								}
							})
						} else if (res.cancel) {
							// console.log('cancel')
						}
					}
				})
			}
		}
	}
</script>

<style>
	@import url("testGoingOn.css");
</style>
