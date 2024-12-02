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

export default {
  components: {
    Header,
    PostCard
  },
  setup() {
    const posts = ref([]);

    // 模拟从数据库/API获取数据
    const fetchPosts = async () => {
      // 假设从后端获取数据
      posts.value = [
        {
          id: 1,
          user: {
            avatar: 'https://via.placeholder.com/40',
            name: '用户A'
          },
          content: '这是一条说说内容，带有文字和图片。',
          image: 'https://via.placeholder.com/200',
          publishTime: '2024-11-06 10:00:00',
          comments: [],
          likes: 0
        },
        {
          id: 2,
          user: {
            avatar: 'https://via.placeholder.com/40',
            name: '用户B'
          },
          content: '这是另一条说说内容，包含图片。',
          image: 'https://via.placeholder.com/200',
          publishTime: '2024-11-06 11:00:00',
          comments: [],
          likes: 0
        },
      ];
    };

    onMounted(() => {
      fetchPosts();
    });

    const handleComment = (postId, comment) => {
      console.log(`评论说说 ${postId}: ${comment.content}`);
      // 在实际项目中这里会有 API 调用来提交评论
    };

    const handleLike = (postId) => {
      const post = posts.value.find(p => p.id === postId);
      if (post) {
        post.likes += 1;
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





  