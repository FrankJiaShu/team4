package com.tot.team4.config;

import com.tot.team4.Interceptor.TokenInterceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @description: 拦截器校验token
 * @author: shicb-a
 * @create: 2023-08-30 19:34
 **/
@Configuration
@Log4j2
public class TokenConfig implements WebMvcConfigurer {

	@Resource
	TokenInterceptor tokenInterceptor;

	/**
	 * 添加拦截器
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("添加tokenInterceptor拦截器");
		registry.addInterceptor(tokenInterceptor)
				.addPathPatterns("/**")//指定该类拦截的url
				.excludePathPatterns(
						//登录
						"/user/login",
						"/user/add",
						//退出登录
						// "/user/logout",
						//获取验证码
						"/getCode",
						//发送短信
						"/sendshortMessage",
						//重置账号
						"/unsealaccount",
						//文件上传
						"/uploadImg",
						//html静态资源
						"/**/*.html",
						//js静态资源
						"/**/*.js",
						//css静态资源
						"/**/*.css",
						// swagger放行
						"/swagger-ui.html/**",
						"/swagger-resources/**",
						"/v3/**",
						"/swagger-ui/**"
						// 文件处理测试 暂时放行
//						"/files/*/**",
//						"/files/page"
//						"/files/queryFiles"
				);
	}
}
