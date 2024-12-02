import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // 导入 router
import store from './store/store';
import axios from 'axios';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import ViewUIPlus from 'view-ui-plus';
import 'view-ui-plus/dist/styles/viewuiplus.css'; // 引入样式
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 设置 Vue 的一些全局配置
app.config.productionTip = false;

// 使用 ViewUI 和 ElementPlus
app.use(ViewUIPlus);
app.use(ElementPlus);

// 注册 router 和 store
app.use(router);
app.use(store);

// 设置 Axios 的一些默认配置
axios.defaults.baseURL = 'http://localhost:5000';
axios.defaults.timeout = 5000;

// 将 Axios 挂载到 Vue.prototype 上
app.config.globalProperties.$http = axios; // 适用于 Vue 3

// 挂载应用
app.mount('#app');

