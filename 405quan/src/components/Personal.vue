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
            name: '用户A'
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
    };

    const handleLike = (postId) => {
      const post = posts.value.find(p => p.id === postId);
      if (post) {
        post.likes += 1;
      }
    };

    // api获取用户信息数据
    const user = {
      avatar: 'https://via.placeholder.com/80',
      name: '用户A',
      signature: '人生短短几载，别忘了微笑。',
      totalPosts: 123,
      totalVisitors: 4567,
      totalLikes: 789
    };

    return {
      posts,
      handleComment,
      handleLike,
      user
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
</style>
