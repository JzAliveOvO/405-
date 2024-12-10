<template>
  <el-header class="header">
    <div class="header-content">
      <!-- 网页标题和首页图标 -->
      <div class="title-home">
        <span class="page-title" style="margin-left: 20px;margin-right: 20px;">405朋友圈</span>
        <div class="home-icon" @click="navigateToHome" style="cursor: pointer;">
          <el-icon class="large-icon">
            <House />
          </el-icon>
          <span style="margin-left: 5px;">首页</span>
        </div>
      </div>

      <!-- 搜索框 -->
      <el-input
        class="search-input"
        placeholder="搜索..."
        v-model="searchQuery"
        suffix-icon="el-icon-search"
      />

      <!-- 右侧图标 -->
      <div class="right-icons" style="margin-right: 10px;">
        <el-icon @click="handleAvatarClick" class="icon large-icon">
          <Avatar />
        </el-icon>
        <el-icon @click="navigateToSet" class="icon large-icon">
          <Setting />
        </el-icon>
      </div>
    </div>

    <!-- 登录/注册弹框 -->
    <login-dialog @login-success="onLoginSuccess" ref="loginDialog" />
  </el-header>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { House, Avatar, Setting } from '@element-plus/icons-vue';
import LoginDialog from './LoginDialog.vue';

export default {
  components: {
    House,
    Avatar,
    Setting,
    LoginDialog,
  },
  setup() {
    const router = useRouter();
    const searchQuery = ref('');
    const isLoggedIn = ref(localStorage.getItem('uid') !== null); // 从 localStorage 获取登录状态
    const loginDialog = ref(null); // 创建对 loginDialog 的引用

    const navigateToHome = () => {
      router.push({ name: 'Home' });
    };

    const navigateToPersonal = () => {
      router.push({ name: 'Personal' });
    };

    const navigateToSet = () => {
      router.push({ name: 'Set' });
    };

    const handleAvatarClick = () => {
      if (isLoggedIn.value) {
        navigateToPersonal();
      } else {
        // 使用 loginDialog 引用调用 openDialog 方法
        if (loginDialog.value) {
          loginDialog.value.openDialog();
        }
      }
    };

    const onLoginSuccess = () => {
      isLoggedIn.value = true;
      navigateToPersonal();
    };

    return {
      searchQuery,
      isLoggedIn,
      navigateToHome,
      navigateToPersonal,
      navigateToSet,
      handleAvatarClick,
      onLoginSuccess,
      loginDialog, // 返回 loginDialog 引用
    };
  },
};
</script>

<style scoped>
.header {
  background-color: #333; /* 深灰色背景 */
  color: white; /* 文字颜色为白色 */
  display: flex;
  align-items: center;
  padding: 0 20px;
  position: sticky; /* 设置为吸顶 */
  top: 0; /* 吸顶位置 */
  z-index: 1000; /* 确保在其他元素之上 */
}

.header-content {
  display: flex;
  width: 100%;
  justify-content: space-between; /* 左右分布 */
  align-items: center;
}

.title-home {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
}

.page-title {
  font-size: 20px; /* 设置标题大小 */
  font-weight: bold; /* 加粗标题 */
  margin-right: 5px; /* 减少标题与首页图标的间距 */
  color: white; /* 标题颜色为白色 */
}

.icon {
  cursor: pointer; /* 鼠标指针变为手形 */
  color: white; /* 图标颜色为白色 */
  margin: 0 10px; /* 图标间距 */
}

.large-icon {
  font-size: 24px; /* 调整图标的大小 */
  margin: 0 5px; /* 减少图标之间的边距 */
}

.home-icon {
  display: flex; /* 使图标和文字在一行 */
  align-items: center; /* 垂直居中对齐 */
}

.search-input {
  flex: 1; /* 让搜索框填满剩余空间 */
  max-width: 600px; /* 最大宽度 */
  margin: 0 20px; /* 左右间距 */
}
</style>












