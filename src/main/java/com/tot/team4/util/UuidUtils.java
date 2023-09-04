package com.tot.team4.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: weixy-h
 * @Date: 2023/8/31 14:39
 * @Desc:
 **/
@Component
public class UuidUtils {
     public  String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
