package com.userCenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author 李嘉烨
 * @Date 2025/2/10 21:36
 * @注释
 */
@Data
public class RegisteredUser implements Serializable {

    private static final long serialVersionUID = 398625280621179597L;

    private String userAccount;

    private String password;

    private String checkPassword;

    private String planetCode ;


}
