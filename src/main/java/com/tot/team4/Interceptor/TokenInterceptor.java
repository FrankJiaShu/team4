package com.tot.team4.Interceptor;

/**
 * @description: token拦截器
 * @author: shicb-a
 * @create: 2023-08-30 19:38
 **/

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.tot.team4.entity.User;
import com.tot.team4.util.LoginUserContextHolder;
import com.tot.team4.util.RedisUtils;
import com.tot.team4.util.ResultObject;
import com.tot.team4.util.TokenUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义拦截器
 * 自定义拦截器后，需要配置进Spring
 *
 * 拦截器Interceptor可以拿到原始的HTTP请求和响应的信息，
 *    也可以拿到你真正处理请求方法的信息，但是拿不到传进参数的那个值。
 *
 *拦截顺序：filter—>Interceptor-->ControllerAdvice-->@Aspect -->Controller
 */
@Component
@Log4j2
public class TokenInterceptor implements HandlerInterceptor {

	@Resource
	private RedisUtils redisUtils;

	@Resource
	private RedisTemplate redisTemplate;

	@Resource
	private TokenUtils tokenUtils;

	/**
	 * 在访问Controller某个方法之前这个方法会被调用。
	 * @param request
	 * @param response
	 * @param handler
	 * @return false则表示不执行postHandle方法,true 表示执行postHandle方法
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info("Token Interceptor preHandle {}","");
		String token = request.getHeader("token");
		log.info("Token Interceptor preHandle token :{}",token);
		log.info("Token Interceptor preHandle uri {}",request.getRequestURL().toString());
		// TODO: 如果不是映射到方法不拦截 直接通过
		/*if (!(handler instanceof HandlerMethod)) {
			return true;
		}*/


		/* token情况:
		   1. token 为空null或者token解析非法
		   2. token没在缓存里，缓存已过期
		   3. token在缓存里
		*/
		if(!StringUtils.isEmpty(token) && !ObjectUtil.isNull(tokenUtils.getUserIdFromToken(token))){
			String username = tokenUtils.getUsernameFromToken(token);
			Long userId = tokenUtils.getUserIdFromToken(token);
			log.info("token信息---用户名:{}, 用户id:{}",username, userId);

			// redis key = String(userId)
			Object value = redisUtils.getValue(userId.toString());
			log.info("根据token信息获取对象: {}",value);
			if (ObjectUtil.isNotNull(value)){
				// 方便其他接口取到用户信息, 比如在接口逻辑校验权限
				LoginUserContextHolder.set((User)value);
				// 拦截器放行
				return true;
			}
		}

		// 向response里写入数据
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try (PrintWriter writer = response.getWriter()) {
			writer.print(JSON.toJSONString(ResultObject.error("用户未登录！", HttpStatus.UNAUTHORIZED.value())));
		} catch (Exception e) {
			log.error("login token error is {}", e.getMessage());
		}
		//false则表示不执行postHandle方法,不执行下一步chain链，直接返回response
		return false;
	}

	/**
	 * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
	 * preHandle方法处理之后这个方法会被调用，如果控制器Controller出现了异常，则不会执行此方法
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		log.info("Token Interceptor postHandle");
	}

	/**
	 * 不管有没有异常，这个afterCompletion都会被调用
	 * 整个请求结束之后被调用，也就是在DispatchServlet渲染了对应的视图之后执行（主要用于进行资源清理工作）
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @throws Exception
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		log.info("Token Interceptor afterCompletion");
		// 清除全局变量
		LoginUserContextHolder.remove();
	}

}
