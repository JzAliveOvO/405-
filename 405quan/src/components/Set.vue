<template>
  <div>
    <!-- Header 部分 -->
    <Header />
    
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
                  <img :src="user.avatar" class="avatar2" alt="头像" />
                </div>
              </div>
              <div class="profile-details" style="font-size: 18px; text-align: left;padding-left: 230px;">
                <!-- 用户昵称 -->
                <el-row>
                  <el-col>昵称：{{ user.uname }}</el-col> 
                </el-row>
                <!-- 用户个性签名 -->
                <el-row>
                  <el-col>个性签名：{{ user.selfintroduction }}</el-col> 
                </el-row>
                <!-- 个人信息 -->
                <el-row>
                  <el-col>性别：{{ user.sex }}</el-col> 
                </el-row>
                <el-row>
                  <el-col>生日：{{ user.birth }}</el-col> 
                </el-row>
                <el-row>
                  <el-col>绑定手机：{{ user.tel }}</el-col> 
                </el-row>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="1">
          </el-col>

          <el-col :span="7">
            <el-card>
              <p>你可能感兴趣的人</p>
              <div class="user-info">
                <img class="avatar" src="./@src\assets\ava3.jpg" alt="User Avatar" />
                <span class="user-name">{小圆同学}</span>
              </div>
              <div class="user-info">
                <img class="avatar" src="./@src\assets\ava4.jpg" alt="User Avatar" />
                <span class="user-name">{文心万言}</span>
              </div>
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
          <el-input v-model="user.uname" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item label="个性签名" :label-width="'80px'">
          <el-input v-model="user.selfintroduction" placeholder="请输入个性签名"></el-input>
        </el-form-item>
        <el-form-item label="性别" :label-width="'80px'">
          <el-select v-model="user.sex" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="生日" :label-width="'80px'">
          <el-date-picker v-model="user.birth" type="date" placeholder="请选择生日"></el-date-picker>
        </el-form-item>
        <el-form-item label="绑定手机" :label-width="'80px'">
          <el-input v-model="user.tel" placeholder="请输入手机号"></el-input>
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
        <el-button type="primary" @click="updatePassword">确定</el-button>
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
import { useRouter } from 'vue-router';
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
    const router = useRouter();
    const value1 = ref(0);
    const activeMenu = ref('1'); 
    const isLogoutDialogVisible = ref(false); // 控制退出登录弹窗的显示
    const isEditDialogVisible = ref(false); // 控制编辑资料弹窗的显示
    const isPasswordDialogVisible = ref(false); // 控制密码管理弹窗的显示
    let a = '';
    // 用户信息
    const user = reactive({
      uid:localStorage.getItem('uid'),
      avatar: '',
      uname: '',
      selfintroduction: '',
      password: '',
      sex: '',
      birth: '',
      tel: '', 
    });
    const uid = localStorage.getItem('uid'); // 用户uid
    //检验uid是否存在
    if (uid) {
      console.log('用户ID:', uid);
    } else {
      console.log('没有找到用户ID');
    }
    // 密码相关数据
    const passwordData = reactive({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    });
    // API获取用户数据
    const getUserData = async () => {
      if (!uid) {
        ElMessage.error("用户丢失");
        console.log('缺少uid');
        return;
      }
      try {
        const response = await fetch(`http://localhost:4050/get_user_info?uid=${uid}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json(); // 返回数据处理并且存储在 data 中
        if (data.code === 200) { // 成功获取数据，对数据进行处理
          console.log(data.message);
          // 传递用户信息
          user.avatar = data.data.avatar; // 转换为 JPEG 格式
          console.log(user.avatar);
          // 处理其他用户信息
          user.uname = data.data.uname;
          user.selfintroduction = data.data.selfIntroduction; // 确保个性签名绑定
          user.sex = data.data.sex;
          const birthDate = data.data.birth.split('T')[0]; // 只取日期部分
          user.birth = birthDate;
          user.tel = data.data.tel;
          user.password = data.data.password;
          console.log(data.data);

        } else {
          ElMessage.error(data.message || "获取用户信息失败");
        }
      } catch (error) {
        console.error('请求失败：', error);
        ElMessage.error("获取用户信息失败!");
      }
    };
    // 初始触发获取用户信息
    onMounted(() => {
      getUserData();
    });
     // 打开密码管理弹窗
    const openPasswordDialog = () => {
      isPasswordDialogVisible.value = true;
      console.log(isPasswordDialogVisible)
    };
    // 关闭密码管理弹窗
    const closePasswordDialog = () => {
      isPasswordDialogVisible.value = false;
    };
    //修改密码api
    const updatePassword = async () => {
      if (passwordData.newPassword !== passwordData.confirmPassword || passwordData.oldPassword !== user.password ) {
        console.log(passwordData,user.password);
        ElMessage.error("密码错误或者两次密码不一致");
        console.log('error');
        return;
      }
      try {
        const response = await fetch("http://localhost:4050/update_user_password", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            uid: user.uid,
            newPassword: passwordData.newPassword
          })
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log(data);
        if (data.code === 200) {
          isEditDialogVisible.value = false;  // 关闭编辑资料的弹窗
          savePassword();
        } else {
          ElMessage.error(data.message || "修改失败");
        }
      } catch (error) {
        console.error('请求失败：', error);
        ElMessage.error("修改失败!");
      }
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

    // 保存修改后的用户数据api
    const saveUserData = async () => {
      user.avatar=a;
      console.log(user);
      try {
        const response = await fetch("http://localhost:4050/update_user_info", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(user),
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        if (data.code === 200) {
          ElMessage.success("修改成功!");
          isEditDialogVisible.value = false;  // 关闭编辑资料的弹窗
          getUserData();  //再次获取用户信息--刷新数据
        } else {
          ElMessage.error(data.message || "修改失败");
        }
      } catch (error) {
        console.error('请求失败：', error);
        ElMessage.error("修改失败!");
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
    // 清空 localStorage 中的 uid
      localStorage.removeItem('uid');
      ElMessage('退出登录成功')  
      console.log('用户已退出');
      closeLogoutDialog(); // 关闭对话框
      // 跳转到首页页面
      router.push({ name: 'Home' });
    };

    // 确认修改密码的逻辑
    const savePassword = () => {
      ElMessage('修改密码成功')  
      console.log('密码已修改'); 
      closeLogoutDialog(); // 关闭对话框
      //退出登录
      confirmLogout();
    };

    return { 
      value1,
      activeMenu,
      user,  
      uid,
      passwordData,  
      isLogoutDialogVisible, 
      isEditDialogVisible,
      isPasswordDialogVisible,
      updatePassword,
      getUserData,
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
.user-card {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-name {
  margin-left: 10px;
}
.profile-details .el-col:first-child {
  font-weight: bold;
}
.avatar{
  width: 50px;
  height: 50px;
  border-radius: 50%; /* 使图片圆形 */
  margin-right: 20px; /* 头像和用户名之间的间距 */
  margin-left: 30px;
  object-fit: cover; /* 防止图片拉伸 */
}
.avatar2{
  width:120px;
  height:120px;
  border-radius: 50%; /* 使图片圆形 */
  object-fit: cover; /* 防止图片拉伸 */
}
</style>
