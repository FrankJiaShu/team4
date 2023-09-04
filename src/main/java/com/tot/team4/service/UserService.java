package com.tot.team4.service;

import com.baomidou.mybatisplus.core.injector.methods.Insert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tot.team4.entity.File;
import com.tot.team4.entity.User;

import java.util.HashMap;
import java.util.List;

/**
 * @author guohb-a
 * @time 2023.08.30
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录接口
     * @return 查询登录的用户
     */
    User login(String username, String password);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAll(int pageStart, int pageSize);

    /**
     * 获取用户的数量
     * @return 用户数量
     */
    int getUserAmount();

    /**
     * 根据用户名获取用户对象
     * @param username 用户名
     * @return user
     */
	User findByUsername(String username);
}