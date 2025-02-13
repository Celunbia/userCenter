package com.userCenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.userCenter.common.BaseResponse;
import com.userCenter.common.ErrorCode;
import com.userCenter.common.ResultUtils;

import com.userCenter.exception.BusinessException;
import com.userCenter.model.domain.request.LoginUser;
import com.userCenter.model.domain.request.RegisteredUser;
import com.userCenter.model.domain.User;
import com.userCenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.userCenter.contant.userContant.USER_LOGIN_STATUS;
import static com.userCenter.contant.userContant.USER_ROLE;

/**
 * 用户请求
 * @author Ye
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {


    @Resource
    private UserService userService;

    /**
     * 注册响应请求
     *
     */
    @PostMapping("/register")
    BaseResponse<Long> registeredUser(@RequestBody RegisteredUser registeredUser) {

        if(registeredUser == null) {
           throw new BusinessException(ErrorCode.NULL_ERROR,"请求封装对象为空");
        }
        String userAccount = registeredUser.getUserAccount();
        String password = registeredUser.getPassword();
        String checkPassword = registeredUser.getCheckPassword();
        String planetCode = registeredUser.getPlanetCode();

        if(StringUtils.isAnyBlank(userAccount, password, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求中存在字段为空") ;
        }

       long result =  userService.registeredUser(userAccount, password, checkPassword,planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 登录用户请求
     *
     *
     */
    @PostMapping("/login")
    BaseResponse<User> loginUser(@RequestBody LoginUser loginUser , HttpServletRequest request) {

        if(loginUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求封装对象为空") ;
        }
        String userAccount = loginUser.getUserAccount();
        String password = loginUser.getPassword();



        if(StringUtils.isAnyBlank(userAccount, password)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求中存在字段为空") ;
        }
        User user = userService.doLogin(userAccount, password, request) ;
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN,"账号未注册") ;

        }

        return ResultUtils.success(user) ;

    }

    /**
     * 用户注销
     *
     */
    @PostMapping("/logout")
    BaseResponse<Integer> logoutUser(HttpServletRequest request) {
        if(request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求为空") ;
        }
        Integer result =  userService.userLogout(request) ;
        return  ResultUtils.success(result);



    }


    /**
     * 查找信息
     */
    @GetMapping("/current")
    public BaseResponse<User> currentUser(HttpServletRequest request) {
        //todo 校验是否合法
        Object object = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User)object;
        if(currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN,"未登录") ;
        }
        long id = currentUser.getId();
        User user=  userService.getSafetyUser(userService.getById(id));
        return ResultUtils.success(user) ;

    }

    /**
     * 管理员查找用户
     *
     */
    @GetMapping("/search")
    BaseResponse<List<User>> searchUser(String userName , HttpServletRequest request   ) {

        if(userName == null ) {
//            ArrayList<User> objects = new ArrayList<>();
//            objects.add(new User());
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求中存在字段为空") ;

        }
        if( UserController.roleCheck(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"未登录/无权限") ;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();


        if(StringUtils.isNotBlank(userName)) {
            queryWrapper.like("user_name", userName);
        }

        List<User> list =  userService.list(queryWrapper).stream().map(
                user -> {
                    userService.getSafetyUser(user);
                    return user;
                }
        ).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     *  管理员删除用户
     *
     */
    @PostMapping("/delete")
    BaseResponse<Boolean> deleteUser(long id, HttpServletRequest request) {
        boolean result ;
//        if(id<=0|| UserController.roleCheck(request)) {
//            return ResultUtils.success(false) ;
//        }
        if(id<=0 ) {
//            ArrayList<User> objects = new ArrayList<>();
//            objects.add(new User());
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"id不得小于0") ;

        }
        if( UserController.roleCheck(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"未登录/无权限") ;
        }


         result = userService.removeById(id) ;
        return ResultUtils.success(result);


    }
    private static boolean roleCheck( HttpServletRequest request){

       Object object = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User)object;
        return user == null || user.getUserRole() == USER_ROLE;
    }


}
