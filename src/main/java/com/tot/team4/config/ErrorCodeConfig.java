package com.tot.team4.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liujh-r
 * @Date: 2023/8/31 10:33
 * @Desc: 错误码配置类
 **/
@Component
@ConfigurationProperties(prefix = "error")
public class ErrorCodeConfig {
    private Map<String, Integer> code = new HashMap<>();
    private Map<String, String> msg = new HashMap<>();

    public Map<String, Integer> getCode() {
        return code;
    }

    public Map<String, String> getMsg() {
        return msg;
    }
}