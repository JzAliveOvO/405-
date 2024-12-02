import Vue from 'vue'
import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/components/Home.vue'
import LoginDialog from '@/components/LoginDialog.vue'


//import Home from '@/components/Home.vue'

const routes = [
  {
    path: '/LoginDialog',
    name: LoginDialog,
    component: LoginDialog,
    meta: {
      requiresLayout: true // 登录页不需要布局
    }
  },
  {
    path: '/Home',
    name: 'Home',
    component: () => import ('@/components/Home.vue'),
    meta: {
      requiresLayout: false 
    }
  },
  {
    path: '/Personal',
    name: 'Personal',
    component: () => import ('@/components/Personal.vue'),
    meta: {
      requiresLayout: false 
    }
  },
  {
    path: '/Set',
    name: 'Set',
    component: () => import ('@/components/Set.vue'),
    meta: {
      requiresLayout: false 
    }
  },
  { 
    path: '/:pathMatch(.*)*', 
    component: Home, 
    meta: {
      requiresLayout: true // 登录页不需要布局
    }
  }, // 捕获所有未匹配的路由
];

const router = createRouter({
  history: createWebHistory(), // 使用 HTML5 History 模式
  routes, // 路由配置
});

export default router;