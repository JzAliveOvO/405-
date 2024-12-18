package com.example.back.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;

@TableName("post") // 确保表名与数据库一致
public class Post {

    @TableId("pid")
    private Integer pid;

    @TableField("time")
    private Date time;

    @TableField("info")
    private String info;

    @TableField("uid")
    private Long uid;

    @TableField("likes")
    private Integer likes;

    @TableField("picture")
    private byte[] picture; // 使用 byte[] 来存储 mediumblob 类型的字段

    // Getter 和 Setter 方法
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
