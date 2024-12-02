package com.example.back.Controller;

import com.example.back.entity.User;
import com.example.back.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    private UserMapper userMapper;
    private static long i = 22051109;

    static void setI() {
        i+=1;
    }
    static long getI() {
        return i;
    }

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
                response.put("code", 200);
                response.put("message", "get message successfully!");
                response.put("data", user);
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
}
