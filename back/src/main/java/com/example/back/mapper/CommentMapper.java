package com.example.back.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.back.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.back.entity.Comment;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    // 使用 QueryWrapper 查询与 pid 相关的所有评论
    default List<Comment> findByPostId(Integer pid) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", pid); // 查询与该 pid 相关的所有评论
        return selectList(queryWrapper);  // 返回所有匹配的评论
    }

}
