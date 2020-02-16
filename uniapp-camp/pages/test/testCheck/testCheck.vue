<template>
	<!-- tabPane start -->
	<view class="page position-class">
		<view class="position-absolute">
			<view class="header-area">
				<view class="header-area-back-icon"  @click="navigateBack">
					<i class="iconfont icon-daohang_jiantou_zuo_dingbu"></i>
				</view>
				<view class="header-area-statistics-icon" @click="statisticsShow = ! statisticsShow">
					<i class="iconfont icon-chengji3"></i>
				</view>
				<view class="tab-wrapper">
					<wuc-tab :tab-list="tabList" tab-class="tab-class" textFlex :tabCur.sync="TabCur" select-class="textColor"></wuc-tab>
				</view>
			</view>
		</view>
		
		<view v-show="statisticsShow">
			<view class="number-card">
				<view class="card-content">
					<view class="card-content-title">题目统计</view>
					<view class="split"></view>
					<view class="box-list">
						<view class="box">
							<view v-for="(q, index) in questionList" :key="q.id">
								<view v-if="q.getPoints === 0" class="iconBox icon-answer-no">
									{{index+1}}
								</view>
								<view v-else class="iconBox icon-answer-yes">
									{{index+1}}
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view v-show="!statisticsShow" :current="TabCur">
			<!-- 全部 start -->
			<view v-show="TabCur === 0" class="tabContent-wrapper">
				<view v-if="questionList" class="page card-wrapper">
					<view class="scroll-box-wrapper">
						<view class="scroll-box">
							<view v-for="(question, index) in questionList" :key="question.id">
								<view class="card">
									<view class="question-type">
										{{qTypes[question.qType-1]}}
									</view>
									<view class="question-title">
										{{index+1}}.&nbsp;{{question.title}}({{question.points}}分)
									</view>
									<view class="option-wrapper" v-for="(option, optionIndex) in question.optionAndRight.optionList" :key="option.id">
										<view :style="option.checked? (indexOfArray(option.id, question.optionAndRight.rightIds)!==-1? 'color: #1A8CFE;background: #1DC88C;': 'color: #1A8CFE;background: #FF4B50;'): 'color: #656577;'"
										 class="option-line">
											<text class="option-words">
												{{String.fromCharCode('A'.charCodeAt() + optionIndex)}}.&nbsp;{{option.text}}
											</text>
										</view>
									</view>
	
									<!-- 题目对错状态区 -->
									<view v-if="question.getPoints !== 0" class="status-content-wrapper">
										<view class="status-content-icon-container">
											<icon type="success" size="20" />
										</view>
										<view class="status-content">
											<text>恭喜你答对啦</text>
										</view>
									</view>
									<view v-else class="status-content-wrapper">
										<view class="status-content-icon-container">
											<icon type="cancel" size="20" />
										</view>
										<view class="status-content">
											<text>答错了没关系，看看解析吧！</text>
										</view>
									</view>
									<!-- 分析区 -->
									<view class="analysis">
										<view class="analysis-ans-wrapper">
											<view class="analysis-ans-title">正确答案：</view>
											<view class="analysis-ans-content-wrapper">
												<text class="analysis-ans-content" v-for="(option, index) in question.optionAndRight.optionList" :key="option.id">
													{{indexOfArray(option.id, question.optionAndRight.rightIds) !== -1 ? String.fromCharCode('A'.charCodeAt() + index): ''}}
												</text>
											</view>
										</view>
										<view class="analysis-desc-wrapper">
											<view class="analysis-desc-title">解释说明：</view>
											<view class="analysis-desc-content">
												{{question.analysis}}
											</view>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>

				</view>
			</view>
			<!-- 全部 end -->

			<!-- 答对 start -->
			<view v-show="TabCur === 1" class="tabContent-wrapper">
				<view v-if="rightList" class="page card-wrapper">
					<view class="scroll-box-wrapper">
						<view class="scroll-box">
							<view v-for="(question, index) in rightList" :key="question.id">
								<view class="card">
									<view class="question-type">
										{{qTypes[question.qType-1]}}
									</view>
									<view class="question-title">
										{{index+1}}.&nbsp;{{question.title}}({{question.points}}分)
									</view>
									<view class="option-wrapper" v-for="(option, optionIndex) in question.optionAndRight.optionList" :key="option.id">
										<view :style="option.checked? (indexOfArray(option.id, question.optionAndRight.rightIds)!==-1? 'color: #1A8CFE;background: #1DC88C;': 'color: #1A8CFE;background: #FF4B50;'): 'color: #656577;'"
										 class="option-line">
											<text class="option-words">
												{{String.fromCharCode('A'.charCodeAt() + optionIndex)}}.&nbsp;{{option.text}}
											</text>
										</view>
									</view>

									<!-- 题目对错状态区 -->
									<view class="status-content-wrapper">
										<view class="status-content-icon-container">
											<icon type="success" size="20" />
										</view>
										<view class="status-content">
											<text>恭喜你答对啦</text>
										</view>
									</view>
									<!-- 分析区 -->
									<view class="analysis">
										<view class="analysis-ans-wrapper">
											<view class="analysis-ans-title">正确答案：</view>
											<view class="analysis-ans-content-wrapper">
												<text class="analysis-ans-content" v-for="(option, index) in question.optionAndRight.optionList" :key="option.id">
													{{indexOfArray(option.id, question.optionAndRight.rightIds) !== -1 ? String.fromCharCode('A'.charCodeAt() + index): ''}}
												</text>
											</view>
										</view>
										<view class="analysis-desc-wrapper">
											<view class="analysis-desc-title">解释说明：</view>
											<view class="analysis-desc-content">
												{{question.analysis}}
											</view>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>
			
				</view>
			</view>
			<!-- 答对 end -->

			<!-- 答错 start -->
			<view v-show="TabCur === 2" class="tabContent-wrapper">
				<view v-if="wrongList" class="page card-wrapper">
					<view class="scroll-box-wrapper">
						<view class="scroll-box">
							<view v-for="(question, index) in wrongList" :key="question.id">
								<view class="card">
									<view class="question-type">
										{{qTypes[question.qType-1]}}
									</view>
									<view class="question-title">
										{{index+1}}.&nbsp;{{question.title}}({{question.points}}分)
									</view>
									<view class="option-wrapper" v-for="(option, optionIndex) in question.optionAndRight.optionList" :key="option.id">
										<view :style="option.checked? (indexOfArray(option.id, question.optionAndRight.rightIds)!==-1? 'color: #1A8CFE;background: #1DC88C;': 'color: #1A8CFE;background: #FF4B50;'): 'color: #656577;'"
										 class="option-line">
											<text class="option-words">
												{{String.fromCharCode('A'.charCodeAt() + optionIndex)}}.&nbsp;{{option.text}}
											</text>
										</view>
									</view>
									
									<!-- 题目对错状态区 -->
									<view class="status-content-wrapper">
										<view class="status-content-icon-container">
											<icon type="cancel" size="20" />
										</view>
										<view class="status-content">
											<text>答错了没关系，看看解析吧！</text>
										</view>
									</view>
									<!-- 分析区 -->
									<view class="analysis">
										<view class="analysis-ans-wrapper">
											<view class="analysis-ans-title">正确答案：</view>
											<view class="analysis-ans-content-wrapper">
												<text class="analysis-ans-content" v-for="(option, index) in question.optionAndRight.optionList" :key="option.id">
													{{indexOfArray(option.id, question.optionAndRight.rightIds) !== -1 ? String.fromCharCode('A'.charCodeAt() + index): ''}}
												</text>
											</view>
										</view>
										<view class="analysis-desc-wrapper">
											<view class="analysis-desc-title">解释说明：</view>
											<view class="analysis-desc-content">
												{{question.analysis}}
											</view>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>
			
				</view>
			</view>
			<!-- 答错 end -->
		</view>
	</view>
	<!-- tabPane end -->

</template>

<script>
	import config from '@/config/config.js'
	import WucTab from '@/components/wuc-tab/wuc-tab.vue'

	export default {
		components: {
			WucTab
		},
		data() {
			return {
				statisticsShow: false,
				tabList: [{
						name: '全部'
					},
					{
						name: '答对'
					},
					{
						name: '答错'
					}
				],
				TabCur: 0,
				qTypes: ['判断题', '单选题', '多选题'],
				testId: '',
				questionList: [],
				rightList: [],
				wrongList: []
			}
		},
		onShow() {},
		onLoad(params) {
			// let skey = this.getSkey()
			this.recordId = params.recordId
			uni.showLoading({
				mask: true
			});

			// 请求当前用户该门考试的答题记录
			uni.request({
				// url: config.getTestsUrl + '?status=' + '&category=',
				url: config.getTestRecordUrl + '?recordId=' + this.recordId,
				method: 'GET',
				success: res => {
					// 获取真实数据之前,务必判断状态为success
					if (res.data.status === "success") {
						this.questionList = res.data.data.questionList;
						this.rightList = this.questionList.filter(item => item.getPoints !== null && item.getPoints !== 0)
						this.wrongList = this.questionList.filter(item => item.getPoints === null || item.getPoints === 0)
					}
				},
				fail: () => {},
				complete: () => {
					uni.hideLoading();
				}
			});
		},
		methods: {
			navigateBack() {
				uni.navigateBack({
				});
			},
			indexOfArray(item, array) {
				for (let i = 0; i < array.length; i = i + 1) {
					if (item == array[i]) {
						return i
					}
				}
				return -1
			}
		}
	}
</script>

<style>
	@import url("testCheck.css");
</style>
