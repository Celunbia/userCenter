package com.userCenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author 李嘉烨
 * @Date 2025/2/10 21:44
 * @注释
 */
@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 5027300685488024669L;

    private String userAccount;

    private String password;



}
