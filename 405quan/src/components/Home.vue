<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <Header />
      </el-header>
      <el-main style="padding: 0px;">
        <div class="main">
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
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

export default {
  components: {
    Header,
    PostCard
  },
  setup() {
    const posts = ref([]);

    // 从数据库/API获取说说数据
    const fetchPosts = async () => {
      try {
        const response = await fetch(`http://localhost:4050/get_all_posts`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        if (data.code === 200) {         //成功获取数据，对数据进行处理
          console.log(data.message);
          // 传递用户信息
          posts.value = data.data;
        } else {
          ElMessage.error(data.message || "获取说说信息失败");
        }
      } catch (error) {
        console.error('网络错误:', error);
      }
    };

    onMounted(() => {
      fetchPosts(); // 页面加载时调用 API 获取数据
    });

    // 发表评论
    const handleComment = async (postId, comment) => {
      try {
        const response = await fetch('/api/comments', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            uid: localStorage.getItem('uid'), // 从localStorage获取当前登录用户的uid
            postId: postId,
            c_info: comment.content,
            c_time: new Date().toLocaleString() // 获取当前时间
          })
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        if (data.code === 200) {         //成功获取数据，对数据进行处理
          console.log(`评论成功: ${comment.content}`);
          fetchPosts(); // 刷新帖子列表及评论
          posts.value = data.data;
        } else {
          ElMessage.error(data.message || '评论发送失败');
        }
      } catch (error) {
        console.error('评论提交错误:', error);
      }
    };

    const handleLike = async (postId) => {
      try {
        const response = await fetch(`/api/like/${postId}`, {
          method: 'POST',
        });
        if (response.ok) {
          console.log(`点赞成功`);
        } else {
          console.error('点赞失败');
        }
      } catch (error) {
        console.error('点赞请求失败:', error);
      }
    };

    return {
      posts,
      handleComment,
      handleLike
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
  width: 100%;
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
</style>





  