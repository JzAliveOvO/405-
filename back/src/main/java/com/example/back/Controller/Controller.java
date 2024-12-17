package com.example.back.Controller;

import com.example.back.entity.Comment;
import com.example.back.entity.User;
import com.example.back.entity.Post;
import com.example.back.mapper.UserMapper;
import com.example.back.mapper.PostMapper;
import com.example.back.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class Controller {

    @Autowired
    private UserMapper userMapper;
    private static long i = 22051109;
    private static int x = 2;
    private static int a = 3001;

    static void setI() {
        i+=1;
    }
    static long getI() {
        return i;
    }

    static void setPid() {
        x+=1;
    }
    static long getPid() {
        return x;
    }
    static void setC() {
        a+=1;
    }
    static long getC() {
        return a;
    }

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CommentMapper commentMapper;

    // 测试接口，返回所有用户
    @GetMapping("/test")
    public List<User> list() {
        return userMapper.selectList(null); // 直接传递 null 查询所有用户
    }

    // 注册接口
    @PostMapping("/register_user")
    public Map<String, Object> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        // 检查是否已存在相同手机号的用户
        User existingUser = userMapper.findByTel(user.getTel());
        if (existingUser != null) {
            response.put("code", 400);
            response.put("message", "该手机号已被注册");
            return response; 
        }

        // 保存新用户
        setI();
        user.setUid(i);
        System.out.println(user.getUid() );
        System.out.println(user.getTel() );
        System.out.println(user.getPassword() );
        userMapper.insert(user);
        response.put("code", 201);
        response.put("data", user);
        response.put("message", "注册成功");
        return response;
    }

    // 获取所有用户信息的接口
    @GetMapping("/get_all_users")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<User> users = userMapper.selectList(null); // 查询所有用户
            response.put("code", 200);
            response.put("data", users);
            response.put("message", "成功获取所有用户信息");
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "获取用户信息失败");
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/get_user_info")
    public Map<String, Object> getUserInfo(@RequestParam Long uid) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = userMapper.selectById(uid); // 根据 uid 查询用户
            if (user != null) {
                String avatarBase64 = null;
                if (user.getAvatar() != null) {
                    avatarBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getAvatar());
                }
                // 创建一个包含用户信息的 Map，并添加头像（如果有）
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("sex", user.getSex());
                userInfo.put("uname", user.getUname());
                userInfo.put("tel", user.getTel()); // 根据需要返回其他用户信息
                userInfo.put("avatar", avatarBase64);  // 添加头像信息
                userInfo.put("birth", user.getBirth());  // 添加生日信息
                userInfo.put("selfIntroduction", user.getSelfIntroduction());  // 个性签名
                userInfo.put("password", user.getPassword());  // 添加密码信息

                // 将用户信息放入响应中
                response.put("code", 200);
                response.put("message", "Get user info successfully!");
                response.put("data", userInfo);
            } else {
                response.put("code", 404);
                response.put("message", "User not found");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Failed to get user information");
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping("/update_user_info")
    public Map<String, Object> updateUserInfo(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        try {
            User existingUser = userMapper.selectById(user.getUid()); // 根据 uid 查询用户
            if (existingUser != null) {
                // 更新用户信息
                userMapper.updateById(user);
                response.put("code", 200);
                response.put("message", "update message successfully!");
            } else {
                response.put("code", 404);
                response.put("message", "User not found");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Failed to update user information");
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping("/update_user_password")
    public Map<String, Object> updateUserPassword(@RequestBody Map<String, String> params) {
        Map<String, Object> response = new HashMap<>();
        Long uid = Long.parseLong(params.get("uid"));
        String newPassword = params.get("newPassword");

        try {
            System.out.println("2: " + newPassword);
            User user = userMapper.selectById(uid); // 根据 uid 查询用户
            if (user != null) {
                // 更新密码
                user.setPassword(newPassword); // 假设你没有进行密码加密，实际应该加密
                userMapper.updateById(user);
                response.put("code", 200);
                response.put("message", "update user password successfully!");
            } else {
                response.put("code", 404);
                response.put("message", "User not found");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Failed to update user password");
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping("/upload_avatar")
    public Map<String, Object> uploadAvatar(@RequestParam("avatar") MultipartFile file, @RequestParam("uid") Long uid) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取上传的图并转换为字节数组
            byte[] avatarData = file.getBytes();

            // 查找用户并更新头像
            User user = userMapper.selectById(uid);
            if (user != null) {
                user.setAvatar(avatarData);
                userMapper.updateById(user);

                response.put("code", 200);
                response.put("message", "头像上传成功");
            } else {
                response.put("code", 404);
                response.put("message", "用户不存在");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "头像上传失败");
            e.printStackTrace();
            response.put("error", e.getMessage());  // 将错误信息加入响应体
        }
        return response;
    }

    // 登录接口
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody Map<String, String> loginData) {
        Map<String, Object> response = new HashMap<>();
        String tel = loginData.get("tel");
        String password = loginData.get("password");

        System.out.println("接收到的手机号: " + tel);
        System.out.println("接收到的密码: " + password);

        User user = userMapper.findByTel(tel);
        if (user != null) {
            System.out.println("数据库中的密码: " + user.getPassword() + "\n");
            if (user.getPassword().equals(password)) {
                response.put("code", 200);
                response.put("data", user);
                response.put("message", "登录成功");
            } else {
                response.put("code", 400);
                response.put("message", "账号或密码错误");
            }
        } else {
            response.put("code", 400);
            response.put("message", "账号或密码错误");
        }
        return response;
    }

    // 获取所有说说信息的接口
    @GetMapping("/get_all_posts")
    public Map<String, Object> getAllPosts() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Post> posts = postMapper.selectList(null); // 查询所有说说
            List<Map<String, Object>> postList = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 格式化时间

            // 遍历所有说说，构建返回数据
            for (Post post : posts) {
                Map<String, Object> postData = new HashMap<>();
                postData.put("id", post.getPid());
                postData.put("content", post.getInfo());
                postData.put("uid", post.getUid());

                // 获取用户信息
                User user = userMapper.selectById(post.getUid());
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("name", user.getUname());
                // 假设你有一个头像的 URL 生成逻辑，这里可以根据用户头像存储方式进行修改
                String avatar_1 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getAvatar());
                userInfo.put("avatar", avatar_1);

                postData.put("user", userInfo);

                // 格式化时间
                postData.put("publishTime", sdf.format(post.getTime()));

                // 获取图片（如果有的话）
                if (post.getPicture() != null) {
                    // 将图片数据转换为 Base64 字符串（前端可直接使用）
                    String imageUrl = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(post.getPicture());
                    postData.put("image", imageUrl);
                } else {
                    postData.put("image", "");
                }

                // 评论字段（此处暂时为空，可以根据需求增加评论模块）
                postData.put("comments", new ArrayList<>());

                // 点赞数
                postData.put("likes", post.getLikes());

                postList.add(postData);
            }

            // 返回结果
            response.put("code", 200);
            response.put("message", "get post successful!");
            response.put("data", postList);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Failed to get posts");
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/get_user_avatar")
    public Map<String, Object> getUserAvatar(@RequestParam Long uid) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 根据 uid 查询用户
            User user = userMapper.selectById(uid);

            // 如果用户存在且头像不为空
            if (user != null && user.getAvatar() != null) {
                // 将头像 byte[] 转换为 Base64 编码
                String avatarBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getAvatar());

                // 将头像信息返回
                response.put("code", 200);
                response.put("message", "头像获取成功");
                response.put("data", avatarBase64);
            } else {
                response.put("code", 404);
                response.put("message", "用户或头像不存在");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "获取头像失败");
            e.printStackTrace();
        }

        return response;
    }

    // 新增说说接口
    @PostMapping("/add_post")
    public Map<String, Object> addPost(@RequestParam String content,
                                       @RequestParam("image") MultipartFile image,
                                       @RequestParam Long uid) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 创建新的 Post 对象
            Post post = new Post();
            post.setInfo(content); // 设置说说内容
            post.setUid(uid); // 设置用户 ID
            post.setTime(new Date());  // 设置当前时间作为发布时间
            post.setLikes(0);  // 初始点赞数为 0

            // 处理图片上传
            if (image != null && !image.isEmpty()) {
                System.out.println("图片文件: " + image);
                byte[] imageData = image.getBytes();  // 将图片转换为字节数组
                post.setPicture(imageData);  // 设置说说图片
            }

            // 保存说说到数据库
            postMapper.insert(post);

            // 返回成功响应
            response.put("code", 200);
            response.put("message", "submit post successful!");
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Failed to submit post");
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping("/add_comment")
    public Map<String, Object> addComment(@RequestBody Comment c) {
        Map<String, Object> response = new HashMap<>();
        try {
            setC();
            c.setCid(a);
            c.setctime(new Date());
            // 保存评论到数据库
            commentMapper.insert(c);

            // 返回成功响应
            response.put("code", 200);
            response.put("message", "Comment added successfully!");
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Failed to add comment");
            e.printStackTrace();
        }

        return response;
    }



    @PostMapping("/upload_post_picture")
    public Map<String, Object> uploadPostPicture(@RequestParam("picture") MultipartFile file, @RequestParam("pid") Integer pid) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取上传的图片并转换为字节数组
            byte[] pictureData = file.getBytes();

            // 查找说说并更新图片
            Post post = postMapper.selectById(pid); // 假设你有 PostMapper 来操作数据库
            if (post != null) {
                post.setPicture(pictureData);
                postMapper.updateById(post);

                response.put("code", 200);
                response.put("message", "说说图片上传成功");
            } else {
                response.put("code", 404);
                response.put("message", "说说不存在");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "说说图片上传失败");
            e.printStackTrace();
            response.put("error", e.getMessage());  // 将错误信息加入响应体
        }
        return response;
    }

    @GetMapping("/get_comments_by_pid")
    public Map<String, Object> getCommentsByPid(@RequestParam Integer pid) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取与该说说相关的所有评论
            List<Comment> comments = commentMapper.findByPostId(pid);  // 通过pid查询评论

            // 如果有评论
            if (comments != null && !comments.isEmpty()) {
                List<Map<String, Object>> commentList = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                for (Comment comment : comments) {
                    Map<String, Object> commentData = new HashMap<>();
                    commentData.put("uid", comment.getUid());  // 用户 ID
                    commentData.put("c_info", comment.getcInfo());  // 评论内容
                    commentData.put("c_time", sdf.format(comment.getctime()));  // 评论时间
                    commentList.add(commentData);
                }

                // 返回评论数据
                response.put("code", 200);
                response.put("message", "Get comments successfully!");
                response.put("data", commentList);
            } else {
                // 返回 204，表示没有评论内容
                response.put("code", 204);
                response.put("message", "No comments found for this post");
                response.put("data", new ArrayList<>());  // 确保返回空数组
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Failed to get comments");
            e.printStackTrace();
        }
        return response;
    }

    // 注册接口
    @PostMapping("/p_like")
    public Map<String, Object> registerUser(@RequestBody Post p) {
        Map<String, Object> response = new HashMap<>();

        try {
            Post post = postMapper.selectById(p.getPid());
            post.setLikes(post.getLikes() + 1);
            postMapper.updateById(post);

            response.put("code", 200);
            response.put("message", "Post likes updated successfully");
            response.put("likes", post.getLikes());  // 返回更新后的点赞数
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Failed to get comments");
            e.printStackTrace();
        }
        return response;
    }


}
