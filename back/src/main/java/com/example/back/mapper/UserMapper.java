package com.example.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.back.entity.User;

public interface UserMapper extends BaseMapper<User> {

    // 使用 QueryWrapper 查询手机号
    default User findByTel(String tel) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tel", tel);
        return selectOne(queryWrapper);  // 返回匹配手机号的唯一用户
    }
}
