<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <Header />
      </el-header>
      <el-main style="padding: 0px;">
        <div class="main">
          <!-- 新增的自我介绍卡片，固定在左边 -->
          <div class="profile-card-wrapper">
            <el-card class="profile-card" style="width: 250px;">
              <div class="profile-header">
                <img :src="user.avatar" alt="Avatar" class="profile-avatar" />
                <div class="profile-info">
                  <span class="profile-name">{{ user.name }}</span>
                  <span class="profile-signature">{{ user.signature }}</span>
                </div>
              </div>
              <div class="profile-stats">
                <div class="profile-stat">
                  <span class="stat-label">总说说数:</span>
                  <span>{{ user.totalPosts }}</span>
                </div>
                <div class="profile-stat">
                  <span class="stat-label">总访客量:</span>
                  <span>{{ user.totalVisitors }}</span>
                </div>
                <div class="profile-stat">
                  <span class="stat-label">总点赞量:</span>
                  <span>{{ user.totalLikes }}</span>
                </div>
              </div>
            </el-card>
          </div>

             <!-- 用于发表说说的卡片 -->
             <div class="post-form-wrapper">
              <el-card class="post-form-card" style="width: 850px; margin-bottom: 10px;">
              <el-input
                type="textarea"
                v-model="newPostContent"
                placeholder="有什么新鲜事想分享吗？"
                rows="3"
              />
              <el-upload 
                action="" 
                list-type="picture-card" 
                :auto-upload="false"
                :on-change="handleSuccess">
              <el-icon><Plus /></el-icon>

              <template #file="{ file }">
                <div>
                  <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
                  <span class="el-upload-list__item-actions">
                    <span
                      class="el-upload-list__item-preview"
                      @click="handlePictureCardPreview(file)"
                    >
                      <el-icon><zoom-in /></el-icon>
                    </span>
                    <span
                      v-if="!disabled"
                      class="el-upload-list__item-delete"
                      @click="handleDownload(file)"
                    >
                      <el-icon><Download /></el-icon>
                    </span>
                    <span
                      v-if="!disabled"
                      class="el-upload-list__item-delete"
                      @click="handleRemove(file)"
                    >
                      <el-icon><Delete /></el-icon>
                    </span>
                  </span>
                </div>
              </template>
            </el-upload>

            <el-dialog v-model="dialogVisible">
              <img w-full :src="dialogImageUrl" alt="Preview Image" />
            </el-dialog>
              <el-button
                type="primary"
                @click="publishPost"
                style="margin-top: 10px;"
              >
                发表说说
              </el-button>
            </el-card>

          </div>

          <!-- 原来的说说卡片展示 -->
          <div class="card-wrapper">
            <el-card v-for="post in posts" :key="post.id" class="post-card" style="width: 850px; margin-bottom: 10px;">
              <PostCard :post="post" @comment="handleComment" @like="handleLike" />
            </el-card>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>


<script>
import Header from '@/components/Header.vue';
import PostCard from '@/components/PostCard.vue';
import axios from 'axios'
import { ref, onMounted } from 'vue';
import { ElMessage,Delete, Download, Plus, ZoomIn} from 'element-plus';

export default {
  components: {
    Header,
    PostCard,
  },
  
  setup() {
    const dialogImageUrl = ref('')
    const dialogVisible = ref(false)
    const disabled = ref(false)
    const posts = ref([]);
    const newPostContent = ref(''); // 新增的说说内容
    const uploadedImage = ref(null); // 上传的图片
    const uid = localStorage.getItem('uid'); // 用户uid
    const uname = ref('');
    const uploadRef = ref(null);


    const handleRemove = (file) => {
      console.log(file)
    }

    const handlePictureCardPreview = (file) => {
      dialogImageUrl.value = file.url
      dialogVisible.value = true
      console.log(dialogImageUrl.value,file.url)
    }

    const handleDownload = (file) => {
      console.log(file)
      console.log(file.raw)
      dialogImageUrl.value = file.raw
      console.log(dialogImageUrl.value)
    }
    const handleSuccess = (file) => {
      dialogImageUrl.value = file.raw;
      console.log(file.raw)
      console.log(dialogImageUrl.value)
    }
    //检验uid是否存在
    if (uid) {
      console.log('用户ID:', uid);
    } else {
      console.log('没有找到用户ID');
    }
    const fetchPosts = async () => {
      try {
        const response = await fetch('http://localhost:4050/get_all_posts', {
          method: 'GET',
          headers: {
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      if (data.code === 200) {
        console.log(data);
        // 传递说说信息
        const filteredPosts = data.data.filter(post => post.user.name === uname.value);
        // 设置说说数据
        posts.value = filteredPosts;
        // 计算个人说说数量和个人总点赞数量
        user.value.totalPosts = posts.value.length;
        user.value.totalLikes = filteredPosts.reduce((total, post) => total + post.likes, 0);
      } else {
        ElMessage.error(data.message || "获取个人说说信息失败");
      }
      } catch (error) {
        console.error('请求失败：', error);
        ElMessage.error("获取个人说说信息失败!");
      }
    };
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
          // 处理其他用户信息
          uname.value = data.data.uname;

        } else {
          ElMessage.error(data.message || "获取用户信息失败");
        }
      } catch (error) {
        console.error('请求失败：', error);
        ElMessage.error("获取用户信息失败!");
      }
    };
    onMounted(() => {
      getUserData();
      fetchPosts();
    });

    const handleComment = (postId, comment) => {
      console.log(`评论说说 ${postId}: ${comment.content}`);
    };

    const handleLike = (postId) => {
      const post = posts.value.find(p => p.id === postId);
      if (post) {
        post.likes += 1;
      }
    };

    const user = ref({
      avatar:'',
      name: '',
      signature: '',
      totalPosts: '',
      totalVisitors:'',
      totalLikes: ''
    });

    const publishPost = async () => {
      if (!newPostContent.value) {
        ElMessage.error('内容不能为空！');
        return;
      }

      // 创建 FormData 对象并添加文本和图片字段
      const formData = new FormData();
      formData.append('content', newPostContent.value);
      formData.append('uid', localStorage.getItem('uid'));
      formData.append('image', dialogImageUrl.value);
      console.log(dialogImageUrl.value);

      // 调试：输出 FormData 内容
      formData.forEach((value, key) => {
        console.log(key, value);
      });

      try {
        const response = await axios.post('http://localhost:4050/add_post', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        if (response.data.code === 200) {
          ElMessage.success('发表成功！');
          newPostContent.value = ''; // 清空输入框
          uploadedImage.value = null; // 清空图片
        } else {
          ElMessage.error('发布失败！');
        }
      } catch (error) {
        ElMessage.error('发布失败！');
      }
    };


  


    return {
      posts,
      handleComment,
      handleLike,
      user,
      uid,
      getUserData,
      uname,
      newPostContent,
      uploadedImage,
      publishPost,
      dialogImageUrl,
      dialogVisible,
      disabled,
      handleRemove,
      handlePictureCardPreview,
      handleDownload,
      handleSuccess,
    };
  }
};
</script>

<style scoped>
.main {
  width: 100%;
  height: 90%; /* 减去 Header 的高度 */
  background-color: #f0f0f0;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 10px;
}

.card-wrapper {
  width: 70%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.post-card {
  margin-bottom: 20px;
}

.header {
  position: sticky;
  top: 0;
  z-index: 10;
  margin: 0;
  padding: 0;
}

.profile-card-wrapper {
  position: sticky;
  top: 100px; /* 固定位置，从 Header 底部开始 */
  flex-shrink: 0;
  margin-right: 0px;
}

.profile-card {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
}

.profile-header {
  display: flex;
  align-items: center;
}

.profile-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  margin-right: 15px;
}

.profile-info {
  display: flex;
  flex-direction: column;
}

.profile-name {
  font-weight: bold;
  font-size: 18px;
}

.profile-signature {
  font-size: 14px;
  color: #888;
}

.profile-stats {
  margin-top: 20px;
}

.profile-stat {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.stat-label {
  font-weight: bold;
}

.card-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.post-form-wrapper {
  width: 70%;
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.post-form-card {
  width: 100%;
}

.upload-demo {
  margin-top: 10px;
}
</style>
