package com.main.manage.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterKey {

    /** 用户登陆 */
	private static String  LOGIN = "/user/login";

	/** 用户退出 */
	private static String  LOGOUT = "/user/logout";

	/** 获取验证码*/
	private static String  PHONE_CODE = "/system/seller/getSmsCode";

	/**
	 * 请求url白名单
	 */
	public static final List<String> URL_WHITE_LIST = new ArrayList<>(
			Arrays.asList(
					LOGIN,
					LOGOUT,
					PHONE_CODE
			)
	);




}
