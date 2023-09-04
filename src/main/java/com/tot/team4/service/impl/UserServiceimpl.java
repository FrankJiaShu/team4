package com.tot.team4.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tot.team4.entity.User;
import com.tot.team4.mapper.UserMapper;
import com.tot.team4.service.UserService;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service("userService")
public class UserServiceimpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    public User login(String username, String password){
        return userMapper.login(username, password);
    }

    @Override
    public List<User> findAll(int pageStart, int pageSize) {
        return userMapper.findAll(pageStart, pageSize);
    }

    @Override
    public int getUserAmount() {
        return userMapper.getUserAmount();
    }


    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

}
