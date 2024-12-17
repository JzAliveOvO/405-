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

      <!-- 点赞图标和点赞数 -->
      <div style="display: flex; align-items: center; cursor: pointer;" @click="likePost">
        <el-icon style="font-size: 24px;">
          <StarFilled />
        </el-icon>
        <span style="margin-left: 5px;">{{ localPost.likes }}</span>
      </div>
    </div>

    <div v-if="showComments" class="comments-section">
      <div class="comment-input-section" style="display: flex; align-items: flex-start;">
        <!-- 头像 -->
        <img :src="currentUserAvatar" alt="Avatar" class="avatar" style="width: 40px; height: 40px; border-radius: 50%;" />

        <!-- 评论输入框 -->
        <el-input v-model="newComment" placeholder="发表评论" size="large" class="comment-input" style="width: 610px; margin-left: 10px;" />
        <!-- 提交评论按钮 -->
        <el-button round @click="submitComment" style="margin-left: 40px; height: 40px;">评论</el-button>
      </div>

      <!-- 评论列表 -->
      <div v-for="(comment, index) in localPost.comment" :key="index" class="comment-item">
        <div class="comment-header">
          <img :src="comment.avatar" alt="Avatar" class="comment-avatar" style="width: 30px; height: 30px; border-radius: 50%;" />
          <div class="comment-info">
            <div class="comment-user-info">
              <span class="comment-user-name">{{ comment.userName }}:</span>
              <span class="comment-content">{{ comment.c_info }}</span>
            </div>
            <span class="comment-time">{{ comment.c_time }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, watchEffect, onMounted } from 'vue';
import { ElMessage } from 'element-plus';  // 导入 ElMessage 组件


export default {
  props: {
    post: Object
  },
  setup(props, { emit }) {
    const currentUserAvatar = ref('');
    const userName = ref('');
    const localPost = reactive({
      ...props.post,
      comment: [],
      likes: props.post.likes || 0 ,// 确保点赞数存在，默认为0
      hasLiked: false,  // 添加字段来标记是否已点赞
    });
    const showComments = ref(false);
    const newComment = ref('');
    
    // 获取用户信息
    const fetchUserInfo = async () => {
      const uid = localStorage.getItem('uid'); // 用户uid
        if (!uid) {
          console.error('UID 未找到');
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
        const data = await response.json();
        if (data.code === 200) {
          currentUserAvatar.value = data.data.avatar;  // 获取用户头像
          console.log('hxiusnidcnsncneincenc',currentUserAvatar.value)
          userName.value = data.data.uname;            // 获取用户名
        } else {
          console.error('获取用户信息失败:', data.message);
        }
      } catch (error) {
        console.error('用户信息加载失败:', error);
      }
    };

    // 获取评论用户信息并更新评论
    const fetchCommentUserInfo = async () => {
      // 遍历每条评论，使用uid获取用户头像
      for (let comment of localPost.comment) {
        const uid = comment.uid; // 获取评论的用户ID
        try {
          const response = await fetch(`http://localhost:4050/get_user_info?uid=${uid}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
          });

          if (response.ok) {
            const data = await response.json();
            if (data.code === 200) {
              // 将用户头像和用户名添加到评论对象中
              comment.avatar = data.data.avatar;   // 设置头像
              comment.userName = data.data.uname;  // 设置用户名
              console.log({ ...props.post }); // 打印展开后的 props.post 对象
            } else {
              console.error(`获取用户头像失败：${data.message}`);
            }
          } else {
            console.error('获取用户信息失败');
          }
        } catch (error) {
          console.error('获取用户信息时发生错误:', error);
        }
      }
    };


    const fetchComments = async () => {
      console.log(props.post.id);
      console.log(`http://localhost:4050/get_comments_by_pid?pid=${props.post.id}`);
      try {
        const response = await fetch(`http://localhost:4050/get_comments_by_pid?pid=${props.post.id}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });
        if (response.status === 200) {
          const data = await response.json();
          localPost.comment = data.data; // 更新评论
          await fetchCommentUserInfo();
        } else if (response.status === 204) {
          console.log('该说说没有评论');
          localPost.comment = [];
        } else {
          console.error('获取评论失败', response.status);
        }
      } catch (error) {
        console.error('评论加载错误:', error);
      }
    };


    // 切换评论显示
    const toggleComments = async () => {
      showComments.value = !showComments.value;
      if (showComments.value) {
        await fetchComments(); // 展示评论时加载评论
      }
    };

    // 点赞
    const likePost = async () => {

      if (localPost.hasLiked) {
        ElMessage({
          message: '您已经赞过该说说',
          type: 'warning',  // 弹窗的类型，warning 是警告
          duration: 2000,    // 弹窗显示的时长，单位是毫秒
        });
        return;  // 阻止重复点赞
      }

      try {
        const response = await fetch('http://localhost:4050/p_like', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            pid: props.post.id
          })
        });

        if (response.ok) {
          const data = await response.json();
          if (data.code === 200) {
            localPost.likes = data.likes; // 更新点赞数
            localPost.hasLiked = true; 
            emit('like', props.post.id, localPost.likes); // 通知父组件更新点赞数
          } else {
            console.error('点赞失败:', data.message);
          }
        } else {
          console.error('点赞请求失败');
        }
      } catch (error) {
        console.error('点赞请求失败:', error);
      }
    };

    // 提交评论
    const submitComment = async () => {
      if (newComment.value.trim()) {
        const comment = {
          user: {
            avatar: currentUserAvatar.value,
            name: userName.value
          },
          content: newComment.value,
          time: new Date().toLocaleString()
        };
        try {
          const response = await fetch('http://localhost:4050/add_comment', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({
              cInfo: comment.content,  // 评论内容
              uid: localStorage.getItem('uid'),  // 用户 ID
              pid: props.post.id,  // 说说 ID
              
            })
          });

          if (response.ok) {
            console.log('评论成功');
            localPost.comment.push(comment); // 更新本地评论列表
            await fetchComments();
            await fetchCommentUserInfo();
            newComment.value = ''; // 清空输入框
          } else {
            console.error('评论提交失败');
          }
        } catch (error) {
          console.error('评论提交失败:', error);
        }
      }
    };

    // 监听post变化，更新评论
    watchEffect(() => {
      localPost.comments = [...props.post.comments];
    });

    // 页面加载时获取用户信息
    onMounted(() => {
      fetchUserInfo(); // 获取当前用户信息
    });

    return {
      localPost,
      showComments,
      newComment,
      toggleComments,
      likePost,
      submitComment,
      currentUserAvatar,
      userName
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
  width: 400px;
  height: 400px;
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
  
  
  