package com.userCenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.userCenter.anno.OperateLogAnno;
import com.userCenter.common.BaseResponse;
import com.userCenter.common.ErrorCode;
import com.userCenter.common.ResultUtils;

import com.userCenter.exception.BusinessException;
import com.userCenter.model.domain.request.UserLoginRequest;
import com.userCenter.model.domain.request.UserRegisterRequest;
import com.userCenter.model.domain.User;
import com.userCenter.service.UserService;
import com.userCenter.utils.JsonFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.userCenter.contant.userContant.*;

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
    @OperateLogAnno
    BaseResponse<Long> registeredUser(@RequestBody UserRegisterRequest userRegisterRequest) {

        if(userRegisterRequest == null) {
           throw new BusinessException(ErrorCode.NULL_ERROR,"请求封装对象为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getUserPassword() ;
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();

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

    BaseResponse<User> loginUser(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if(userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求封装对象为空") ;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getUserPassword();



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
    public String currentUser(HttpServletRequest request) throws JsonProcessingException {
        //todo 校验是否合法
        Object object = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User)object;
        if(currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN,"未登录") ;
        }
        long id = currentUser.getId();
        User user=  userService.getSafetyUser(userService.getById(id));
        return JsonFormatter.getInstance().writeValueAsString(ResultUtils.success(user));

    }

    /**
     * 管理员查找用户
     *
     */
    @GetMapping("/search")
    String searchUser(String username , HttpServletRequest request   ) throws JsonProcessingException {

        if(username == null ) {
//            ArrayList<User> objects = new ArrayList<>();
//            objects.add(new User());
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求中存在字段为空") ;

        }
        if( UserController.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"未登录/无权限") ;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();


        if(StringUtils.isNotBlank(username)) {
            queryWrapper.like("user_name", username);
        }

        List<User> list =  userService.list(queryWrapper).stream().map(
                user -> {
                    userService.getSafetyUser(user);
                    return user;
                }
        ).collect(Collectors.toList());
        return JsonFormatter.getInstance().writeValueAsString(ResultUtils.success(list)) ;
    }

    /**
     *更新
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/update")
    @OperateLogAnno
    public String updateUser(@RequestBody User user, HttpServletRequest request) throws JsonProcessingException {
        // 1. 检查用户权限
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无操作权限");
        }

        // 2. 检查参数合法性
        if (user == null || user.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误：用户信息不合法");
        }

        // 3. 调用服务层更新用户信息
        boolean isUpdated = userService.updateUser(user);

        // 4. 返回操作结果
        if (isUpdated) {
            return JsonFormatter.getInstance().writeValueAsString(ResultUtils.success(true)) ;
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户信息更新失败");
        }
    }
    /**
     *  管理员删除用户
     *
     */
    @PostMapping("/delete")
    @OperateLogAnno
    String  deleteUser(long id, HttpServletRequest request) throws JsonProcessingException {
        boolean result ;

        if(id<=0 ) {

           throw new BusinessException(ErrorCode.PARAMS_ERROR,"id不得小于0") ;

        }
        if( !isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"未登录/无权限") ;
        }


         result = userService.removeById(id) ;

        return JsonFormatter.getInstance().writeValueAsString(ResultUtils.success(result)) ;


    }
    private static boolean isAdmin(HttpServletRequest request){

       Object object = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User)object;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }


}
