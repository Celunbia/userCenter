package com.userCenter;

import java.util.regex.Pattern;


import com.userCenter.mapper.UserMapper;
import com.userCenter.model.domain.User;
import com.userCenter.service.UserService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;


@SpringBootTest
class UserCenterApplicationTests {

    @Resource
    UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Resource
    UserService userService;


    @Test
     void testAddUser() {
        User user = new User();

//        user.setUserName("li");
//        user.setUserAccount("abcd");
//        user.setAvatarUrl("https://cdn.auth0.com/avatars/default.png");
//        user.setGender(0);
//        user.setUserPassword("123456");
//        user.setCreateTime(new Date());
//        user.setUpdateTime(new Date());
//
//        boolean result = userService.save(user);
//        System.out.println(user.getId());
//        Assertions.assertTrue(result);


    }

    /*@Test
    void testValid() {

        String userAccount = "admin";
        String password = "12345678";
        String checkPassword = "12345678";
        String planetCode = "1234";
        String regex = "^[a-zA-Z0-9_]+$";
        boolean isValid = Pattern.matches(regex,"user_Account");
        Assertions.assertTrue(isValid) ;

        // 1. 测试账号是否为空
        userAccount = "";
        long result  = userService.registeredUser(userAccount,password,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount = "admin";

        // 1. 测试密码是否为空
        password = "";
        result  = userService.registeredUser(userAccount,password,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        password = "12345678";

        checkPassword = "";
        result  = userService.registeredUser(userAccount,password,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        checkPassword = "12345678";


        // 2.测试账号小于4位
        userAccount = "abc";
        result  = userService.registeredUser(userAccount,password,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount = "admin";


        // 3.测试密码小于8位
        password = "1234567";
        result  = userService.registeredUser(userAccount,password,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        password = "12345678";


        // 4.测试账号小于等于4
        userAccount = "abc";
        result  = userService.registeredUser(userAccount,password,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount = "admin";

        // 5.测试密码和校验密码不一致
        checkPassword = "12345679";
        result  = userService.registeredUser(userAccount,password,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        checkPassword = "12345678";


        // 6.账号重复
        userAccount = "okuzu";
        result  = userService.registeredUser(userAccount,password,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount = "admin";

        //星球编号重复
        planetCode="123" ;
        result  = userService.registeredUser(userAccount,password,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);




    }

    @Test
    void testMethod() {
//
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("id", 1);
//
//        System.out.println(userMapper.selectById(1));
// 打印参数值
        // 打印 SQL 片段
        String password = "12345678";
        String s = DigestUtils.md5DigestAsHex((UserService.SALT + password).getBytes());
        System.out.println(s);
    }
    @Test
    void createUser(){
        User user = new User();
        user.setUserName("test");
        user.setUserAccount("test");
        user.setAvatarUrl("https://assets.msn.com/weathermapdata/1/static/weather/Icons/taskbar_v10/Condition_Card/D200PartlySunnyV2.svg");
        user.setGender(0);
        user.setUserPassword(DigestUtils.md5DigestAsHex((UserService.SALT+"12345678").getBytes()));
        user.setUserRole(1);
        user.setIsDelete(0);
        user.setPlanetCode("12345");

        int insert = userMapper.insert(user);
        System.out.println(insert);

    } */

}
