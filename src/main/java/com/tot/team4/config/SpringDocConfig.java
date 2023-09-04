package com.tot.team4.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @Author: liujh-r
 * @Date: 2023/8/30 09:27
 * @Desc: springdoc配置类
 **/
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TOT23-Team4-API")
                        .description("这里是我们的后端项目API")
                        .version("v1.0.0")
                        .license(new License()
                                .name("许可协议")
                                .url("null"))
                        .contact(new Contact()
                                .name("admin")
                                .email("null")))
                .externalDocs(new ExternalDocumentation()
                        .description("接口文档")
                        .url("null"));
    }
}
