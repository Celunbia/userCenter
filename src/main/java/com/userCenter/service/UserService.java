package com.userCenter.service;

import com.userCenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Ye
* @description 针对表【user(用户信息)】的数据库操作Service
* @createDate 2025-02-10 16:38:43
*/
public interface UserService extends IService<User> {

     String SALT =  "PRIMARY";
    /**
     *  用户注册接口方法
     * @param userAccount 账号
     * @param password 密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 返回id
     */
    long registeredUser(String userAccount, String password,String checkPassword,String planetCode);

    /**
     * 用户登录
     * @param userAccount 账号
     * @param password 密码
     * @param request 请求
     * @return  返回用户信息
     */
    User doLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * 用户信息脱敏
     * @param user 用户
     * @return 返回安全的用户信息
     */
    User getSafetyUser(User user);


    /**
     * 用户注销
     * @param request 请求
     * @return 返回表示数
     */
    int userLogout(HttpServletRequest request);

    boolean updateUser(User user);
}
