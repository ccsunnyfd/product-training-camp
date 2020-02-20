<template>
	<view v-if="questionList" :style="{ height: height_home + 'px' }" class="page card-wrapper">
		<view class="count-down-panel bgColor">
			<uni-countdown splitorColor="#FFFFFF" :show-day="false" :hour="0" :minute="minute" :second="second" :reset="reset" @timeup="timeUpTrigger"></uni-countdown>
		</view>
		<view class="header-wrapper">
			<view v-show="currentIndex !== -1" class="header-layerout">
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
		
		<form>
			<view class="scroll-box-wrapper">
				<view class="scroll-box">
					<view v-for="(question, index) in questionList" :key="question.id">
						<view v-show="currentIndex === index" class="card">
							<view class="question-title">
								{{question.title}}({{question.points}}分)
							</view>
							<radio-group v-if="question.qType !== 3" :name="question.id" :value="form[question.id]" @change="changeOption(question.id, $event)">
								<view class="option-wrapper" v-for="(option) in question.optionAndRight.optionList" :key="option.id">
									<label>
										<radio :value="option.id" />
										<text class="option-words">
											{{option.text}}
										</text>
									</label>
								</view>
							</radio-group>
							<checkbox-group v-else :name="question.id" :value="form[question.id]" @change="changeOption(question.id, $event)">
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
				<button v-show="currentIndex > 0" @click="jumpToPrev">上一题</button>
				<button v-show="currentIndex !== questionList.length - 1" type="primary" @click="jumpToNext">下一题</button>
				<button v-show="currentIndex === (questionList.length - 1)" @click="formSubmit">提交</button>
			</view>
		</form>
	</view>
</template>

<script>
	import config from '@/config/config.js'
	import uniCountdown from "@/components/linnian-CountDown/uni-countdown.vue"
	
	export default {
		components: {
			uniCountdown
		},
		data() {
			return {
				minute: 0,
				second: 0,
				reset: false,
				height_home: 1400,
				qTypes: ['判断题', '单选题', '多选题'],
				testId: '',
				recordId: '',
				currentIndex: 0,
				questionList: [],
				form: {}
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
			})
			const skey = this.getSkey()
			const that = this

			// 请求当前考试数据
			uni.request({
				// url: config.getTestsUrl + '?status=' + '&category=',
				url: config.getTestByIdUrl,
				method: 'POST',
				data: {
					skey: skey,
					testId: that.testId
				},
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						this.questionList = res.data.data.questionList;
						this.questionList.forEach(q => {
							const optionList = q.optionAndRight.optionList
							const answerList = optionList.map(op => op.checked).filter(checked => checked === true)
							this.form[q.id]=answerList
						})
						this.recordId = res.data.data.id
						const createdAt = res.data.data.createdAt
						const timeLimit = res.data.data.timeLimit
						this.reset = !this.reset
						this.timeLapse(createdAt, timeLimit)
					}
				},
				fail: () => {},
				complete: () => {
					uni.hideLoading();
				}
			});
		},
		// onUnload() {
		// 	console.log('onUnload')
		// },
		onHide() {
			const skey = this.getSkey()
			const that = this
			uni.request({
				url: config.pauseTestUrl,
				method: 'POST',
				// header: {
				// 	'content-type': 'application/x-www-form-urlencoded'
				// },
				data: {
					skey: skey,
					recordId: that.recordId,
					answerMap: that.form
				},
				success: () => {
					// 获取真实数据之前,务必判断状态为success
				},
				fail: () => {},
				complete: () => {}
			})
		},
		methods: {
			timeLapse(createdAt, timeLimit) {
				const curTime = new Date().getTime()
				const createdTime = new Date(Date.parse(createdAt.replace(/-/g, "/")))
				const timeDifference = curTime - createdTime // 时间差的毫秒数
				const timeDiffInSeconds = parseInt(timeDifference/1000) // 时间差的秒数
				if (timeDiffInSeconds > timeLimit * 60) {
					// 已经超时,设定3秒倒计时
					this.minute = 0
					this.second = 3
				} else {
					const leftTimeInSeconds = timeLimit * 60 - timeDiffInSeconds
					this.minute = Math.floor(leftTimeInSeconds/60);
					this.second = leftTimeInSeconds%60;
				}
			},
			changeOption(key, e) {
				const val = e.detail.value
				if ((typeof val) === 'string') {
					if (val === '') {
						this.form[key] = []
					} else {
						this.form[key] = [val]
					}
				} else if (val instanceof Array) {
					this.form[key] = val
				}				
			},
			jumpToNext() {
				this.currentIndex = this.currentIndex + 1
			},
			jumpToPrev() {
				this.currentIndex = this.currentIndex - 1
			},
			timeUpTrigger() {
				uni.showToast({
				    title: '考试时间到，将自动提交！',
				    duration: 3000
				})
				this.submitMethod(this.form)
			},
			// 真正的提交方法，供formSubmit调用
			submitMethod(formdata) {
				const skey = this.getSkey()
				const that = this
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
						recordId: that.recordId,
						answerMap: formdata
					},
					success: res => {
						// 获取真实数据之前,务必判断状态为success
						if (res.data.status === "success") {
							const savedHistRecord = res.data.data
							uni.redirectTo({
								url: '../testResult/testResult?score=' + savedHistRecord.score + '&testId=' + savedHistRecord.testId + '&recordId=' + savedHistRecord.recordId,
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
			},
			formSubmit() {
				const that = this
				let flagArr = []
				let completeFlag = true
				Object.keys(that.form).forEach((key) => {
					const val = that.form[key]
					if (val.length === 0) {
						flagArr.push(0)
						completeFlag = false
					} else {
						flagArr.push(1)
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
							that.submitMethod(that.form)
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
