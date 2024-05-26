package org.example.jobrecback.utils;

public class RedisConstant {
    public static final String LOGIN_USER_KEY = "login:user:";
    public static final long LOGIN_USER_TTL = 1000 * 60 * 60 * 24 * 7L;//7天
    public static final String LOCK_SHOP_KEY = "lock:shop:";

    public static final long CACHE_NULL_TTL = 2L;   //minutes
}
