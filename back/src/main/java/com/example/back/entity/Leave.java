package com.example.back.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;

@TableName("Leave a m")
public class Leave {

    @TableId("Lam_id")
    private Integer lamId; // 留言 ID（主键）

    @TableField("uid_1")
    private Long uid1; // 留言人 ID（外键）

    @TableField("uid_2")
    private Long uid2; // 被留言者 ID（外键）

    @TableField("p_message")
    private String pMessage; // 留言内容

    @TableField("p_likes")
    private String pLikes; // 留言点赞信息

    // Getter 和 Setter 方法
    public Integer getLamId() {
        return lamId;
    }

    public void setLamId(Integer lamId) {
        this.lamId = lamId;
    }

    public Long getUid1() {
        return uid1;
    }

    public void setUid1(Long uid1) {
        this.uid1 = uid1;
    }

    public Long getUid2() {
        return uid2;
    }

    public void setUid2(Long uid2) {
        this.uid2 = uid2;
    }

    public String getpMessage() {
        return pMessage;
    }

    public void setpMessage(String pMessage) {
        this.pMessage = pMessage;
    }

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pLikes = pLikes;
    }
}
