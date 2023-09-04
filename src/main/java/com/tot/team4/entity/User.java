package com.tot.team4.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author guohb-a
 * @time 2023.08.30
 */
@Data
@JsonIgnoreProperties
@JsonFormat
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;//编号
    private String username;//用户名
    private String password;//密码
    private Integer permissions;
    private Integer capicity;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", permissions=" + permissions +
                ", capicity=" + capicity +
                '}';
    }
}
