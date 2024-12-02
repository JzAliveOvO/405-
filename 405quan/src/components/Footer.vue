<template>
    <footer class="footer">
      <div class="footer-content">
        <p class="project-name">405朋友圈</p>
        <p class="project-description">
          2024.10 405小组
        </p>
      </div>
      <div class="footer-links">
        <a href="#about">关于我们</a>
        <a href="#contact">联系我们</a>
        <a href="#privacy">隐私政策</a>
      </div>
    </footer>
  </template>
  
  <script>
  export default {
    name: 'Footer',
  };
  </script>
  
  <style scoped>
  .footer {
    background-color: #282c34;
    color: white;
    padding: 20px;
    text-align: center;
  }
  
  .footer-content {
    margin-bottom: 10px;
  }
  
  .project-name {
    font-size: 24px;
    margin: 0;
  }
  
  .project-description {
    font-size: 16px;
    margin: 5px 0;
  }
  
  .footer-links a {
    color: #61dafb;
    margin: 0 10px;
    text-decoration: none;
  }
  
  .footer-links a:hover {
    text-decoration: underline;
  }
  </style>
  