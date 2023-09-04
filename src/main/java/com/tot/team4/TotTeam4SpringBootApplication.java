package com.tot.team4;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author guohb-a
 * @time 2023.08.30
 * @desc 项目启动类
 */
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.tot.team4.mapper")
@Log4j2
//@EnableApolloConfig
public class TotTeam4SpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(TotTeam4SpringBootApplication.class, args);
        log.info("tot23-team4 backend started!");
    }
}

