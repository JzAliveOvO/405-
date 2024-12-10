package com.example.back.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;

@TableName("comment") // 确保表名与数据库一致
public class Comment {

    @TableId("cid")
    private Integer cid; // 评论 ID（主键）

    @TableField("uid")
    private Long uid; // 评论者用户 ID（外键）

    @TableField("pid")
    private Integer pid; // 关联的说说 ID（外键）

    @TableField("c_info")
    private String cInfo; // 评论内容

    @TableField("ctime")
    private Date cTime; // 评论时间

    // Getter 和 Setter 方法
    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getCInfo() {
        return cInfo;
    }

    public void setCInfo(String cInfo) {
        this.cInfo = cInfo;
    }

    public Date getCTime() {
        return cTime;
    }

    public void setCTime(Date cTime) {
        this.cTime = cTime;
    }
}
