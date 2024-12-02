<template>
  <el-menu :default-active="activeMenu" class="menu" mode="horizontal">
    <el-menu-item index="1" @click="navigateTo('/Home')">首页</el-menu-item>
    <el-menu-item index="2" @click="navigateTo('/Categories')">商品分类</el-menu-item>
    <el-menu-item index="3" @click="navigateTo('/Notice')">公告</el-menu-item>
  </el-menu>
</template>

<script>
import { useRouter } from 'vue-router';

export default {
  data() {
    return {
      activeMenu: '1',
    };
  },
  methods: {
    navigateTo(path) {
      this.$router.push(path);
    },
  },
};
</script>

<style>
.menu {
  margin-bottom: 10px;
}
</style>
