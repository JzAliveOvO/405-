<template>
  <el-dialog
    v-model="dialogVisible"
    width="400px"
    @close="resetForm"
  >
      <h1 style="text-align: center;">欢迎使用</h1>
      <!-- 登录 or 注册 -->
      <el-radio-group v-model="choose" class="radioGroup" size="small">
        <el-radio-button style="margin-bottom:20px;margin-top:18px" type="seccess" plain label="login">登录</el-radio-button>
        <el-radio-button style="margin-bottom:20px;margin-top:18px" type="seccess" plain label="signIn">注册</el-radio-button>
      </el-radio-group>
      <!-- 用户输入表单 -->
      <el-form label-width="90px" :model="user">
        <el-form-item v-if="choose==='login'"
          label="电话号码☎️"
          prop="tel"
          :rules="[ { required: true, message: '请输入账号', trigger: 'blur' } ]">
          <el-input v-model="user.tel"></el-input>
        </el-form-item>
        <el-form-item v-if="choose==='signIn'"
          label="电话号码☎️"
          prop="tel"
          :rules="[ { required: true, message: '请输入账号', trigger: 'blur' } ]">
          <el-input v-model="user.tel"></el-input>
        </el-form-item>
        <el-form-item
          label="密码"
          prop="password"
          :rules="[ { required: true, message: '请输入密码', trigger: 'blur' } ]">
          <el-input type="password" v-model="user.password" show-password></el-input>
        </el-form-item>
        <el-form-item
          v-if="choose==='signIn'"
          label="确认密码"
          prop="checkPassword"
          :rules="[ { required: true, message: '请输入再次输入密码', trigger: 'blur' } ]">
          <el-input type="password" v-model="user.checkPassword" show-password></el-input>
        </el-form-item>
        <el-form-item v-if="choose==='signIn'"
          label="姓名"
          prop="uname"
          :rules="[ { required: true, message: '请输入姓名', trigger: 'blur' } ]">
          <el-input v-model="user.uname"></el-input>
        </el-form-item>
        <!-- 上传头像 -->
        <el-form-item v-if="choose === 'signIn'" label="头像">
          <el-upload
            class="avatar-uploader"
            action=""
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleAvatarChange"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="avatar" :src="avatar" class="profile-avatar" alt="Avatar" />
            <div v-else class="upload-trigger">
              <el-icon><Plus /></el-icon>
            </div>
          </el-upload>
        </el-form-item>
        <!--按钮-->
        <el-button v-if="choose==='login'" type="success" @click="login"
        :disabled="user.tel===''||user.password===''" plain>登录
        </el-button>
        <el-button v-if="choose==='signIn'" type="success" plain @click="signIn"
        :disabled="user.tel===''||user.password===''||user.checkPassword===''" >注册
        </el-button>
      </el-form>
  </el-dialog>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage, ElUpload, ElIcon, Plus } from 'element-plus';

export default {
  data() {
    return {
      choose: 'login',
      user: {
        uid: '',
        tel: '',
        password: '',
        checkPassword: '',
        uname: '',
      },
      dialogVisible: false,
      avatar:'',
      newAvatar:'',
    };
  },
  methods: {
    openDialog() {
      this.dialogVisible = true;
    },
    resetForm() {
      this.user.uid = "";
      this.user.tel = "";
      this.user.password = "";
      this.user.checkPassword = "";
      this.user.uname = "";
      this.avatar = "";
      this.newAvatar = "";
    },
    
    // 处理头像上传
    handleAvatarChange(file) {
      console.log('选中的文件：', file);
      this.avatar = URL.createObjectURL(file.raw);  // 将文件对象转换为URL
      this.newAvatar = file.raw;
    },

    // 头像上传前的校验
    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith('image/');
      if (!isImage) {
        ElMessage.error('只能上传图片!');
      }
      return isImage;
    },

    // 上传头像的函数
    async uploadAvatar() {
      if (!this.avatar) {
        ElMessage.error('请先选择一个头像');
        return;
      }

      const formData = new FormData();
      formData.append('avatar', this.newAvatar);
      formData.append('uid', localStorage.getItem('uid'));
      formData.forEach((value, key) => {
        console.log(key, value);
      });

      try {
        const response = await axios.post('http://localhost:4050/upload_avatar', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        if (response.data.code === 200) {
          ElMessage.success('头像更新成功！');
        } else {
          ElMessage.error('头像更新失败！');
        }
      } catch (error) {
        console.error('请求失败：', error);
        ElMessage.error('头像更新失败！');
      }
    },

    async signIn() {
      if (this.user.checkPassword !== this.user.password) {
        this.$message.error("两次输入的密码不一致!");
        return;
      }
      try {
        const response = await fetch("http://localhost:4050/register_user", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(this.user),
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        if (data.code === 201) {
          this.$message.success("注册成功!");
          localStorage.setItem('uid', data.data.uid);
          this.dialogVisible = false;  // 关闭弹窗
        } else {
          this.$message.error(data.message || "注册失败");
        }
        if (response.ok) {
          this.uploadAvatar();
        }
        
      } catch (error) {
        console.error('请求失败：', error);
        this.$message.error("注册失败!");
      }
    },

    async login() {
      try {
        const response = await fetch("http://localhost:4050/login", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            tel: this.user.tel,
            password: this.user.password,
          }),
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        if (data.code === 200) {
          localStorage.setItem('uid', data.data.uid);
          this.$message.success("登录成功!");
          this.dialogVisible = false;
          this.$emit('login-success');  // 发出登录成功事件
        } else {
          this.$message.error("密码或账号错误!");
        }
      } catch (error) {
        console.error('请求失败：', error);
        this.$message.error("密码或账号错误!");
      }
    },
  }

};
</script>

<style scoped>
.card {
  width: 100%;
}
.profile-avatar {
  width: 100px;   /* 设置图片宽度 */
  height: 100px;  /* 设置图片高度 */
  object-fit: cover; /* 保持图片比例填充 */
  border-radius: 50%; /* 圆形头像 */
}

.upload-trigger {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100px;
  height: 100px;
  border: 1px dashed #dcdfe6;
  border-radius: 50%;
}
</style>



