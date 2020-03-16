<template>
	<view class="ground-page">
		<!-- 步骤条 -->
		<view class="header-panel">
			<uni-steps
			 :options="[{title: '微信授权'}, {title: '实名信息'}]"
			 :active="TabCur"
			 active-color="#133386">
			</uni-steps>
		</view>

		<view class="register-panel">
			<wuc-tab :tab-list="tabList" tab-class="tab-class" textFlex :tabCur="TabCur" select-class="textColor"></wuc-tab>
			<view class="divider-line" />
			<view v-show="TabCur === 0" class="tabContent-wrapper">
				<view>
					<view class="auth-hint">
						微信登录以进一步验证身份
					</view>
				</view>
				<view class="divider-line" />
				<view class="button-grabAuth">
					<view class="button-wrapper">
						<button :disabled="!lawChecked" type="primary" @click="openSetting" v-if="status==0">微信登录</button>
						<button :disabled="!lawChecked" type="primary" open-type="getUserInfo" @getuserinfo="getuserinfo" v-else>微信登录</button>
					</view>
				</view>
				<view class="divider-line" />
				<view class="auth-law-check">
					<checkbox-group @change="changeLawCheck">
						<label>
							<checkbox color="#133386" style="transform:scale(0.7)" />
							阅读并同意以下协议
						</label>
					</checkbox-group>
				</view>
				<view class="divider-line" />
				<view class="auth-law">
					1. 本应用仅限柏科数据内部使用。<br/>
					2. 内部员工可通过手机号识别认证。<br/>
					3. 未提前认证的用户请向公司管理员咨询。<br/>
					4. 获取手机号需先通过微信授权。
				</view>
			</view>
			<view v-show="TabCur === 1" class="tabContent-wrapper">
				<view class="phone-or-token-panel">
					<radio-group @change="radioChange">
						<view class="phone-or-token-layout">
							<label for="phone">
								<radio id="phone" value="phone" checked="true" color="#133386" />
								手机号码验证
							</label>
							<label for="token">
								<radio id="token" value="token" color="#133386" />
								令牌验证
							</label>
						</view>
					</radio-group>
					<view v-show="phoneOrToken === 'phone'">
						<view>
							<view class="auth-hint">
								已录入手机号的员工通过手机号确认身份								
							</view>
						</view>
						<view class="divider-line" />
						<view class="button-grabAuth">
							<view class="button-wrapper">
								<button type="primary" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">获取手机号</button>
							</view>
						</view>
						<view class="divider-line" />
						<view class="auth-law">
							1. 手机号码仅用于验证员工身份。<br/>
						</view>
					</view>
					<view v-show="phoneOrToken === 'token'">
						<view>
							<view class="auth-hint">
								请输入6位数字组合令牌通过身份验证
							</view>
							<view class="divider-line" />
							<view class="button-grabAuth">
								<view class="button-wrapper">
									<valid-code ref="code" :maxlength="6" :isPwd="false" @finish="verifyCode" ></valid-code>
								</view>
							</view>
							<view class="divider-line" />
							<view class="auth-law">
								1. 令牌仅用于特殊身份验证，比如审计管理员。<br/>
							</view>
						</view>
					</view>
				</view>
				<view class="divider-line"/>
			</view>
		</view>

	</view>
</template>

<script>
	import {
		getProvider,
		login,
		bindPhone,
		bindToken,
		getCode
	} from '@/api/request/login/loginUtils.js'
	import {
		getAuthSetting
	} from '@/api/request/login/getAuthSetting.js'
	import uniSteps from '@/components/uni-steps/uni-steps.vue'
	import WucTab from '@/components/wuc-tab/wuc-tab.vue'
	import validCode from '@/components/p-validCode/validCode.vue'

	export default {		
		components: {
			uniSteps,
			WucTab,
			validCode
		},
		data() {
			return {
				lawChecked: false,
				code: '',  // 小程序授权码
				status: 0 ,//2未操作 1已经授权  0拒绝授权
				height_home: 1400,
				tabList: [{
						name: '微信授权'
					},
					{
						name: '实名信息'
					}
				],
				TabCur: 0,
				phoneOrToken: "phone"
			}
		},
		onLoad(params) {
			if(params.step) {
				this.TabCur = parseInt(params.step);
			}
		},
		onShow() {
			(async () => {
				//获取授权状态 2未操作 1已经授权  0拒绝授权
				this.status = await getAuthSetting()
				//获取服务商信息 "provider":["weixin"]
				let provider = await getProvider();
				//获取code "009Lbpsj2ggKpE8AM4tj2604sj7Xbpsp"
				this.code = await getCode(provider[0])
			})()
		},
		methods: {
			changeLawCheck() {
				this.lawChecked = !this.lawChecked
			},
			radioChange(e) {
				this.phoneOrToken = e.detail.value
			},
			getuserinfo(e) {
				// e的内容：
				// {type: "getuserinfo", timeStamp: 2937, target: {…}, currentTarget: {…}, detail: {…}, …}
				// currentTarget: {id: "", offsetLeft: 32, offsetTop: 212, dataset: {…}}
				// detail: {errMsg: "getUserInfo:ok", rawData: "{"nickName":"xxx","gender":1,"language":"zh_CN",…xxx"}", signature: "xxx", encryptedData: "xxx", iv: "xxx", …}
				// mp: {type: "getuserinfo", timeStamp: 2937, target: {…}, currentTarget: {…}, detail: {…}}
				// target: {id: "", offsetLeft: 32, offsetTop: 212, dataset: {…}, errMsg: "getUserInfo:ok", …}
				// type: "getuserinfo"
				// getUserInfo为ok表明有授权
				if (!this.code) {
					uni.showToast({
						icon: 'none',
						title: '正在加载中，稍等一下',
						duration: 2000
					});
					return
				}
				if (e.detail && e.detail.errMsg == 'getUserInfo:ok') {
					e.detail.code = this.code //把code信息添加到detail中，一起作为login的参数
					login(e.detail).then(() => {
						// 添加手机号或令牌实名验证环节
						const identified = uni.getStorageSync('identified')
						if (identified === true) {
							// 已经实名，直接跳转主页
							uni.showToast({
								icon: 'none',
								title: '实名用户，欢迎回来',
								duration: 4000
							})
							uni.switchTab({
								url: '/pages/index/index'
							})
						} else {
							this.TabCur = 1
						}
					}).catch(() => {
						//登录失败之后的回调
						uni.showToast({
							icon: 'none',
							title: '登录失败',
							duration: 2000
						})
					})
				} else {
					//授权拒绝之后的回调
					uni.showToast({
						icon: 'none',
						title: '已拒绝授权',
						duration: 2000
					})
					//用户拒绝授权
					this.status = 0
				}
			},
			getPhoneNumber(e) {
				var that = this
				uni.checkSession({
					success() {
						if(e.detail.errMsg == 'getPhoneNumber:fail user deny') {
						} else {
							const param = {
								skey: that.getSkey(),
								encryptedData: e.detail.encryptedData,
								iv: e.detail.iv
							}
							bindPhone(param).then((res) => {
								// 通过手机号绑定实名成功
								uni.showToast({
									icon: 'none',
									title: '欢迎你，' + res.realname,
									duration: 4000
								})
								uni.switchTab({
									url: '/pages/index/index'
								})									
							}).catch(() => {
								// 通过手机号绑定实名不成功
								uni.showToast({
									icon: 'none',
									title: '对不起，未找到您的手机号记录，请联系管理员',
									duration: 3000
								})
							})
						}
					},
					fail() {
						console.log('状态已过期，需要重新执行登录流程')
						uni.showToast({
							icon: 'none',
							title: '微信登录已过期，请重新登录',
							duration: 3000
						})
						this.TabCur = 0
					}
				})
			},
			verifyCode(token) {
				var that = this
				uni.checkSession({
					success() {
						const param = {
							skey: that.getSkey(),
							token: token
						}
						bindToken(param).then((res) => {
							// 通过令牌绑定实名成功
							uni.showToast({
								icon: 'none',
								title: '欢迎你，' + token,
								duration: 4000
							})
							uni.switchTab({
								url: '/pages/index/index'
							})									
						}).catch(() => {
							// 通过令牌绑定实名不成功
							uni.showToast({
								icon: 'none',
								title: '对不起，令牌无效，请联系管理员',
								duration: 3000
							})
							that.$refs.code.clear();
						})
					},
					fail() {
						console.log('状态已过期，需要重新执行登录流程')
						uni.showToast({
							icon: 'none',
							title: '微信登录已过期，请重新登录',
							duration: 3000
						})
						this.TabCur = 0
					}
				})
			},
			openSetting() {
				const that = this;
				uni.showModal({
					title: '提示',
					content: '你已经取消过授权，需设置授权权限',
					confirmText: '设置',
					success: function(res) {
						if (res.confirm) {
							uni.openSetting({
								success(res) {
									if(res.authSetting) {
										that.status = 1
									}
								}
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				})
			}
		}
	}
</script>

<style>
	@import url("register.css");
</style>
