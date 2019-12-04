import Vue from 'vue'
import App from './App'
import PubFuc from'./common/common'
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
Vue.prototype.$pubFuc = PubFuc

const app = new Vue({
    ...App
})
app.$mount()
