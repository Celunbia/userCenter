package com.userCenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户信息
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 性别 1-male 0-female
     */
    private Integer gender;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户状态 0-正常 1-异常
     */
    private Integer userStatus;

    /**
     * 用户角色 0-普通用户 1-管理员
     */
    private Integer userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除状态 0 -未删除 1-删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 星球编号用于鉴权
     */
    private String planetCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}