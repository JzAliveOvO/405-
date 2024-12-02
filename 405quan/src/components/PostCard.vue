<template>
  <div class="post-card-container">
    <div class="header-section">
      <img :src="post.user.avatar" alt="Avatar" class="avatar" />
      <div class="user-info">
        <span class="user-name">{{ post.user.name }}</span>
        <span class="publish-time">{{ post.publishTime }}</span>
      </div>
    </div>
    <div class="content-section">
      <p class="content">{{ post.content }}</p>
      <img v-if="post.image" :src="post.image" alt="Post Image" class="post-image" />
    </div>

    <div class="actions-section" style="display: flex; justify-content: flex-end; align-items: center;">
      <!-- 评论图标 -->
      <el-icon @click="toggleComments" style="cursor: pointer; margin-right: 10px; font-size: 24px;">
        <Comment />
      </el-icon>

      <!-- 点赞图标 -->
      <el-icon @click="likePost" style="cursor: pointer; font-size: 24px;">
        <StarFilled />
      </el-icon>
    </div>

    <div v-if="showComments" class="comments-section">
      <div class="comment-input-section" style="display: flex; align-items: flex-start;">
        <!-- 头像 -->
        <img :src="currentUserAvatar" alt="Avatar" class="avatar" style="width: 40px; height: 40px; border-radius: 50%;" />

        <!-- 评论输入框 -->
        <el-input v-model="newComment" placeholder="发表评论" size="large" class="comment-input" style="width: 610px; margin-left: 10px;" />
        <!-- 提交评论按钮 -->
        <el-button  round @click="submitComment" style="margin-left: 40px; height: 40px;">评论</el-button>
      </div>

      <!-- 评论列表 -->
      <div v-for="(comment, index) in localPost.comments" :key="index" class="comment-item">
        <div class="comment-header">
          <img :src="comment.user.avatar" alt="Avatar" class="comment-avatar" style="width: 30px; height: 30px; border-radius: 50%;" />
          <div class="comment-info">
            <div class="comment-user-info">
              <span class="comment-user-name">{{ comment.user.name }}:</span>
              <span class="comment-content">{{ comment.content }}</span>
            </div>
            <span class="comment-time">{{ comment.time }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, watchEffect } from 'vue';

export default {
  props: {
    post: Object
  },
  setup(props, { emit }) {
    // 创建一个本地状态来存储 post 数据
    const localPost = reactive({ ...props.post });
    const showComments = ref(false);
    const newComment = ref('');
    const currentUserAvatar = ref('https://via.placeholder.com/40'); // 当前评论用户的头像

    const toggleComments = () => {
      showComments.value = !showComments.value;
    };

    const likePost = () => {
      emit('like', props.post.id);
    };

    const submitComment = () => {
      if (newComment.value.trim()) {
        // 模拟评论数据
        const comment = {
          user: {
            avatar: currentUserAvatar.value, // 使用当前评论用户的头像
            name: '用户C' // 假设评论者名称
          },
          content: newComment.value,
          time: new Date().toLocaleString() // 获取当前时间作为评论时间
        };

        // 向父组件发送评论事件，父组件处理更新评论数据
        emit('comment', props.post.id, comment);
        localPost.comments.push(comment); // 更新本地评论列表
        newComment.value = ''; // 清空输入框
      }
    };

    // 如果 props.post 发生变化，更新 localPost
    watchEffect(() => {
      localPost.comments = [...props.post.comments];
    });

    return {
      localPost,
      showComments,
      newComment,
      toggleComments,
      likePost,
      submitComment,
      currentUserAvatar // 返回评论用户头像的绑定
    };
  }
};
</script>

<style scoped>
.post-card-container {
  padding: 10px;
  background: white;
  border-radius: 8px;
}

.header-section {
  display: flex;
  align-items: center;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-weight: bold;
  text-align: left;
}

.publish-time {
  font-size: 12px;
  color: #888;
}

.content-section {
  text-align: left; /* 让内容（文字和图片）靠左对齐 */
}

.content {
  margin: 0; /* 移除默认的段落外边距 */
  padding: 0; /* 移除段落的内边距 */
}

.post-image {
  display: block; /* 确保图片作为块级元素显示，避免与文字并排 */
  margin-top: 10px; /* 图片和文字之间的间距 */
}

.actions-section {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.comments-section {
  margin-top: 10px;
}

.comment-input {
  width: 100%;
  margin-bottom: 10px;
}

.comment-item {
  margin-bottom: 15px;
  padding: 10px;
}

.comment-header {
  display: flex;
  align-items: flex-start; /* 头像和评论内容垂直对齐 */
}

.comment-avatar {
  width: 40px;  /* 设置头像的宽度 */
  height: 40px; /* 设置头像的高度 */
  border-radius: 50%; /* 圆形头像 */
  margin-right: 10px; /* 头像与评论信息的间距 */
}

.comment-info {
  display: flex;
  flex-direction: column; /* 列布局，评论内容垂直排列 */
}

.comment-user-info {
  display: flex;
  align-items: center; /* 用户名和评论内容水平排列 */
}

.comment-user-name {
  font-weight: bold;
  margin-right: 8px; /* 用户名与评论内容的间距 */
}

.comment-content {
  font-size: 14px;
  color: #333;
}

.comment-time {
  font-size: 12px;
  color: #888;
  margin-top: 5px; /* 评论时间位于头像下方 */
}
</style>

  
  
  