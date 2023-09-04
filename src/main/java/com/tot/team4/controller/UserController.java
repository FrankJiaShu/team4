package com.tot.team4.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tot.team4.entity.User;
import com.tot.team4.service.UserService;
import com.tot.team4.util.LoginUserContextHolder;
import com.tot.team4.util.RedisUtils;
import com.tot.team4.util.TokenDetailImpl;
import com.tot.team4.util.TokenUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * @author guohb-a
 * @time 2023.08.30
 */

@RestController
@RequestMapping("/user")
@Tag(name="用户控制器")
@Log4j2
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private TokenUtils tokenUtils;

    @Resource
    private RedisUtils redisUtils;

    @RequestMapping("/login")
    @Operation(description="用户登录")
    public Map<String, Object> login(String username, String password) {
        // 根据用户名密码进行查询用户
//         User user = userService.login(username.trim(), DigestUtils.sha256Hex(password.trim()));
        User user = userService.login(username.trim(), password.trim());
        // 封装返回结果
        Map<String, Object> result = new HashMap<>();
        // 如果查到用户，说明登陆成功
        if (user != null) {
            // 生成 token
            String token = tokenUtils.generateToken(new TokenDetailImpl(user.getId(), username));
            log.info("token: \n"+token);
            result.put("token", token);
            result.put("code", 1200);
            result.put("message", "登录成功");
            result.put("data", user);

            // 登录成功时将token存入redis并设置默认过期时间,redis key = String(userId)
            redisUtils.cacheValue(user.getId().toString(), user);
        }
        // 否则返回登陆失败
        else {
            log.info("登录失败");
            result.put("code", 1202);
            result.put("message", "用户名或密码错误");
            result.put("data", null);
        }

        return result;
    }

    @RequestMapping("/info")
    @Operation(description="用户测试")
    public String getUserInfo() {
        return JSON.toJSONString("User Info");
    }

    @RequestMapping("/logout")
    @Operation(description="用户登出")
    public String logout() {
        // 清除redis缓存
        User user = LoginUserContextHolder.get();
        log.info("清除redis缓存时--用户名:{}, 用户id:{}", user.getUsername(),user.getId());
        redisUtils.removeValue(user.getId().toString());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 2200);
        result.put("message", "退出登录成功");
        result.put("data", "success");
        return JSON.toJSONString(result);
    }

    /**
     * 增加用户
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @PostMapping("/add")
    @Operation(description="新增用户")
    public Map<String, Object> addUser(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        if (userService.findByUsername(username) == null) {
            // password = DigestUtils.sha256Hex(password.trim())
            password = password.trim();
            User user = new User(username, password);
            // 默认设置为普通用户(0)
            user.setPermissions(0);
            if (userService.save(user)) {
                result.put("code", 1200);
                result.put("message", "注册成功");
                result.put("data", null);
            } else {
                result.put("code", 1202);
                result.put("message", "注册失败");
                result.put("data", "success");
            }
        }else {
            result.put("code", 1202);
            result.put("message", "用户名重复");
            result.put("data", null);
        }
        return result;
    }


//    @RequestMapping("/getUsers")
//    public String findAll(QueryInfo queryInfo) {
//
//        int total = userService.getUserAmount();
//        int pageStart = (queryInfo.getPageStart() - 1) * queryInfo.getPageSize();
//
//        List<User> users = userService.findAll(pageStart, queryInfo.getPageSize());
//
//        Map<String, Object> result = new HashMap<>();
//        Map<String, Object> data = new HashMap<>();
//        data.put("data", users);
//        data.put("total", total);
//        result.put("code", 1200);
//        result.put("message", "获取用户列表成功");
//        result.put("data", data);
//        return JSON.toJSONString(result);
//
//    }

}