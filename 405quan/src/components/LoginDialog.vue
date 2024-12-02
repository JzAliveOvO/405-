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
          label="账号"
          prop="tel"
          :rules="[ { required: true, message: '请输入账号', trigger: 'blur' } ]">
          <el-input v-model="user.tel"></el-input>
        </el-form-item>
        <el-form-item v-if="choose==='signIn'"
          label="账号"
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
import { ref } from 'vue';

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
  },
};
</script>

<style scoped>
.card {
  width: 100%;
}
</style>



