import Vue from 'vue'
import App from './App'
import "./common/iconfont.css"

Vue.config.productionTip = false

// const baseUrl = 'https://localhost:8080/'
// Vue.prototype.baseUrl = baseUrl;
// Vue.prototype.loginUrl = baseUrl + 'login';

Vue.prototype.getGlobalUser = function(key) {
	var userInfo = uni.getStorageSync(key);
	if (userInfo != null && userInfo != "" && userInfo != undefined) {
		return userInfo;
	} else {
		return null;
	}
};

App.mpType = 'app'

const app = new Vue({
    ...App
})
app.$mount()
