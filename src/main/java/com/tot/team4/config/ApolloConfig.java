package com.tot.team4.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 阿波罗配置类
 * @author: liujh-r
 * @create: 2023-09-01 17:59
 **/
//@Configuration
public class ApolloConfig {

    @Getter
    @Value("${storage.switch}")
    private String flag;

    public ApolloConfig() {
        Config config = ConfigService.getAppConfig();
        flag = config.getProperty("storage.switch", flag);

        // 添加配置变更监听器
        config.addChangeListener(changeEvent -> {
            if (changeEvent.isChanged("storage.switch")) {
                flag = changeEvent.getChange("storage.switch").getNewValue();
            }
        });
    }

}
