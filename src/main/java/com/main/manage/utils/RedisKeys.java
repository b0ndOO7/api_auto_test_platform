package com.main.manage.utils;

public class RedisKeys {

    public static String LOGIN_TOKEN = "test:user:token:";
//    public static String LOGIN_TOKEN = "user:token:"; // 目录格式存储

    // token过期时间，TTL
    public static long TOKEN_TTL = 60*30;
}
