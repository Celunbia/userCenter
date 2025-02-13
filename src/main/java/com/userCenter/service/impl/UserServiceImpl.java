package com.userCenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.userCenter.common.ErrorCode;
import com.userCenter.exception.BuinessException;
import com.userCenter.model.domain.User;
import com.userCenter.service.UserService;
import com.userCenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import static com.userCenter.contant.userContant.USER_LOGIN_STATUS;

/**
* @author Ye
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2025-02-10 16:38:43
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    /**
     * 盐值，混淆密码
     */

    /**
     * 校验密码为数字+字母+下划线
     */
    private static final String REGEX = "^[a-zA-Z0-9_]+$";




    @Resource
    private UserMapper userMapper;



    @Override
    public long registeredUser(String userAccount, String password, String checkPassword,String planetCode) {

        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount,password,checkPassword,planetCode)){
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"参数存在空值") ;
        }
        if(userAccount.length()<4 )
        {
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"账号长度小于4") ;
        }
        if(password.length()<8 || checkPassword.length()<8){
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"密码/校验密码长度小于8") ;
        }
        if(!checkPassword.equals(password)){
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不同") ;
        }
        if(planetCode.length()>5){
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"编号长度大于5") ;
        }
        boolean isValid = Pattern.matches(REGEX,userAccount);
        if(!isValid){
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"存在特殊字符") ;
        }
        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long countUserAccount = this.count(queryWrapper);
        if(countUserAccount>0){
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"账号不能重复") ;
        }
        //星球编号不重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planet_code",planetCode);
        long countPlanetCode = this.count(queryWrapper);
        if(countPlanetCode>0){
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"编号不能重复") ;
        }
        //2.加密
        String hexPassword = DigestUtils.md5DigestAsHex((SALT+password).getBytes());
        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(hexPassword);
        user.setPlanetCode(planetCode);
        boolean result =  this.save(user);
        if(!result){
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"插入错误") ;
        }
        return 0 ;
    }

    @Override
    public User doLogin(String userAccount, String password, HttpServletRequest request) {

        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount,password)){
            return null;
        }
        if(userAccount.length()<4 )
        {
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"账号长度小于4") ;
        }
        if(password.length()<8 ){
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"密码长度小于8") ;
        }


        boolean isValid = Pattern.matches(REGEX,userAccount);
        if(!isValid){
            throw new BuinessException(ErrorCode.PARAMS_ERROR,"账号不得包含特殊字符串") ;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        User user = userMapper.selectOne(queryWrapper);
        if(user==null||user.getIsDelete()==1){

            throw new BuinessException(ErrorCode.PARAMS_ERROR,"账号/密码不匹配") ;
        }

        //2.加密
        String hexPassword = DigestUtils.md5DigestAsHex((SALT+password).getBytes());
        //3.匹配
        if(!user.getUserPassword().equals(hexPassword)){

            throw new BuinessException(ErrorCode.PARAMS_ERROR,"账号/密码不匹配") ;
        }

        //4.脱敏
        User safetyUser = getSafetyUser(user);

        //5.记录登录信息
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);
        return  safetyUser ;





    }

    /**
     *  用于脱敏的安全对象
     * @param user 用户
     * @return 返回脱敏后用户信息
     */
    @Override
    public User getSafetyUser(User user) {
        if(user==null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setPlanetCode(user.getPlanetCode());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setCreateTime(user.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
      request.getSession().removeAttribute(USER_LOGIN_STATUS);

      return 1;

    }


}




