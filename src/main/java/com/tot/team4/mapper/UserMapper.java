package com.tot.team4.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tot.team4.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author guohb-a
 * @time 2023.08.30
 */
@Mapper //指定这是一个操作数据库的mapper
@Component
public interface UserMapper extends BaseMapper<User> {
    User login(String username, String password);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAll(int pageStart, int pageSize);

    /**
     * 根据用户名查询用户
     * @return 用户
     */
    User findByUsername(String username);

    /**
     * 获取用户的数量
     * @return 用户数量
     */
    int getUserAmount();


}