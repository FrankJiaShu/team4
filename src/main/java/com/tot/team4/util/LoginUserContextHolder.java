package com.tot.team4.util;

import com.tot.team4.entity.User;

/**
 * @description: 方便其他接口取到用户信息
 * @author: shicb-a
 * @create: 2023-08-30 21:00
 **/
public class LoginUserContextHolder {
	private static final ThreadLocal<User> loginUserThreadLocal = new ThreadLocal<>();

	public static void set(User loginUser) {
		loginUserThreadLocal.set(loginUser);
	}

	public static User get() {
		return loginUserThreadLocal.get();
	}

	public static void remove() {
		loginUserThreadLocal.remove();
	}

}
