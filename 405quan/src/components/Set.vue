<template>
  <div>
    <el-header class="header">
        <Header />
      </el-header>
    
    <!-- 主容器 -->
    <el-container style="height: 100vh;">
      <!-- 左侧菜单部分 -->
      <el-aside class="aside" width="350px">
        <el-card class="custom-card" shadow="always">
          <template #header>
            <div class="avatar-container">
              <span>个人设置</span>
            </div>
          </template>
          <div class="avatar-container">
            <img :src="user.avatar" class="avatar" alt="头像" />
          </div> 
          <el-menu :default-active="activeMenu" class="custom-menu">
            <el-menu-item index="1" @click="openEditDialog">
              <el-icon ><Setting /></el-icon> 编辑资料
            </el-menu-item> 
            <el-menu-item index="2" @click="openPasswordDialog">
              <el-icon><Lock /></el-icon> 密码管理
            </el-menu-item>
            <el-menu-item index="3" @click="openLogoutDialog">
              <el-icon class="large-icon">
                <Avatar />
              </el-icon>退出登录 
            </el-menu-item>
          </el-menu>
          <template #footer>
            <el-row justify="center" align="middle">
              <el-col :span="24">
                <el-text>请为我们打分</el-text>
                <el-row justify="center" align="middle">
                  <el-text>Rate</el-text>
                  <el-rate v-model="value1"/> 
                </el-row>
                <el-text>感谢您的支持</el-text>
              </el-col>
            </el-row>
          </template>
        </el-card>    
      </el-aside>

      <UserForm ref="userFormRef" />
      <!-- 右侧主内容区域 -->
      <el-main class="main">
        <!-- 展示资料区域 -->
        <el-row> 
          <el-col :span="16"> 
            <el-card class="profile-card" shadow="hover">
              <div class="demo-basic--circle">
                <div class="block">
                  <el-avatar :size="150" :src="circleUrl" />
                </div>
              </div>
              <div class="profile-details" style="font-size: 18px; text-align: left;padding-left: 230px;">
                <!-- 用户昵称 -->
                <el-row>
                  <el-col>昵称：{{ user.nickname }}</el-col> 
                </el-row>
                <!-- 用户个性签名 -->
                <el-row>
                  <el-col>个性签名：{{ user.signature }}</el-col> 
                </el-row>
                <!-- 个人信息 -->
                <el-row>
                  <el-col>性别：{{ user.gender }}</el-col> 
                </el-row>
                <el-row>
                  <el-col>生日：{{ user.birthday }}</el-col> 
                </el-row>
                <el-row>
                  <el-col>绑定手机：{{ user.phone }}</el-col> 
                </el-row>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="1">
          </el-col>

          <el-col :span="7">
            <el-card>
              <p>你可能感兴趣的人</p>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <!-- 编辑资料弹窗 -->
    <el-dialog
      v-model="isEditDialogVisible"
      title="编辑资料"
      width="500px"
      :before-close="closeEditDialog"
    >
      <el-form>
        <el-form-item label="昵称" :label-width="'80px'">
          <el-input v-model="user.nickname" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item label="个性签名" :label-width="'80px'">
          <el-input v-model="user.signature" placeholder="请输入个性签名"></el-input>
        </el-form-item>
        <el-form-item label="性别" :label-width="'80px'">
          <el-select v-model="user.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="生日" :label-width="'80px'">
          <el-date-picker v-model="user.birthday" type="date" placeholder="请选择生日"></el-date-picker>
        </el-form-item>
        <el-form-item label="绑定手机" :label-width="'80px'">
          <el-input v-model="user.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog">取消</el-button>
        <el-button type="primary" @click="saveUserData">保存修改</el-button>
      </span>
    </el-dialog>

    <!-- 密码管理弹窗 -->
    <el-dialog
      v-model="isPasswordDialogVisible"
      title="修改密码"
      width="400px"
      :before-close="closePasswordDialog"
    >
      <el-form :model="passwordData" label-width="100px">
        <el-form-item label="原密码">
          <el-input v-model="passwordData.oldPassword" type="password" placeholder="请输入原密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordData.newPassword" type="password" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码">
          <el-input v-model="passwordData.confirmPassword" type="password" placeholder="请确认新密码"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closePasswordDialog">取消</el-button>
        <el-button type="primary" @click="savePassword">确定</el-button>
      </span>
    </el-dialog>

    <!-- 退出登录确认对话框 -->
    <el-dialog
      v-model="isLogoutDialogVisible"
      title=""
      width="400px"
      :before-close="closeLogoutDialog"
    > 
      <el-row class="out">
        <span>是否确定退出</span>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmLogout">确定</el-button>
        <el-button @click="closeLogoutDialog">取消</el-button> 
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import Header from '@/components/Header.vue';
import { Avatar, Setting, Lock } from '@element-plus/icons-vue'; 
import { ElMessage } from 'element-plus'

export default {
  components: {
    Header,
    Avatar, 
    Setting,
    Lock
  },
  setup() {
    const value1 = ref(0);
    const activeMenu = ref('1'); 
    const isLogoutDialogVisible = ref(false); // 控制退出登录弹窗的显示
    const isEditDialogVisible = ref(false); // 控制编辑资料弹窗的显示
    const isPasswordDialogVisible = ref(false); // 控制密码管理弹窗的显示
    // api获取用户信息
    const user = reactive({
      avatar: 'https://via.placeholder.com/100',
      nickname: 'John',
      signature: '11111',
      gender: '男',
      birthday: '1990-01-01',
      phone: '13888888888',
    });


    // 密码相关数据
    const passwordData = reactive({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    });

    // api获取user数据
    const getUserData = () => {
      user.avatar = 'https://via.placeholder.com/100';
      user.nickname = 'John Doe';
      user.signature = '111111';
      user.gender = '男';
      user.birthday = '1990-01-01';
      user.phone = '13888888888';
    };

    // 初始触发
    onMounted(() => {
      getUserData();
    });

     // 打开密码管理弹窗
     const openPasswordDialog = () => {
      isPasswordDialogVisible.value = true;
      console.log(isPasswordDialogVisible)
    };

    // 打开编辑资料弹窗
    const openEditDialog = () => {
      isEditDialogVisible.value = true;
      console.log(isEditDialogVisible.value )
    };

    // 关闭编辑资料弹窗
    const closeEditDialog = () => {
      isEditDialogVisible.value = false;
    };

      // 关闭密码管理弹窗
    const closePasswordDialog = () => {
      isPasswordDialogVisible.value = false;
    };

    // 保存修改后的用户数据
    const saveUserData = async () => {
      try {
        // 这里可以调用API发送用户数据到后端进行更新
        // 假设保存成功后返回响应，更新 UI
        ElMessage.success('资料修改成功');
        closeEditDialog();
        console.log('保存的数据：', user);
        // 在这里发送 API 请求更新数据库
        // await api.updateUserProfile(user);
      } catch (error) {
        ElMessage.error('保存失败');
        console.error(error);
      }
    };

    // 打开退出登录确认对话框
    const openLogoutDialog = () => {
      isLogoutDialogVisible.value = true;
    };
    // 关闭退出登录对话框
    const closeLogoutDialog = () => {
      isLogoutDialogVisible.value = false;
    };
    // 确认退出登录的逻辑
    const confirmLogout = () => {
      ElMessage('退出登录成功') 
      // 在这里添加退出登录的逻辑，比如清除 token、重定向等
      console.log('用户已退出');
      closeLogoutDialog(); // 关闭对话框
      // 实际应用中可以在这里处理跳转到登录页面
    };

    // 确认修改密码的逻辑
    const savePassword = () => {
      ElMessage('修改密码成功') 
      // 在这里添加退出登录的逻辑，比如清除 token、重定向等
      console.log('密码已退出');
      closeLogoutDialog(); // 关闭对话框
      // 实际应用中可以在这里处理跳转到登录页面
    };

    return { 
      value1,
      activeMenu,
      user,  
      passwordData,  
      isLogoutDialogVisible, 
      isEditDialogVisible,
      isPasswordDialogVisible,
      openLogoutDialog,
      closeLogoutDialog,
      confirmLogout,
      openEditDialog,
      closeEditDialog,
      saveUserData,
      closePasswordDialog,
      savePassword,
      openPasswordDialog,
    };
  },
};
</script>

<style scoped>

.header {
  position: sticky;
  top: 0;
  z-index: 10;
  margin: 0;
  padding: 0;
}
.custom-card {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3); /* 自定义阴影效果 */
  border-radius: 10px; /* 可选，增加圆角 */
  margin: 20px;
  margin-top: 20px;
}

.custom-menu {
  border: 1px solid #ccc; /* 添加灰色边框 */
  border-radius: 8px; /* 可选，增加圆角 */
  margin-top: 10px;
}

.profile-card {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.profile-details el-row {
  margin-bottom: 10px;
}

.profile-details el-col {
  font-size: 16px;
  color: #333;
}
.out{ 
  margin-left: 120px;
  font-size: 20px;
}

.profile-details .el-col:first-child {
  font-weight: bold;
}
</style>
