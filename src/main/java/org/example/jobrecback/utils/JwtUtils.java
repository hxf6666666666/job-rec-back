package org.example.jobrecback.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * jwt工具类
 */
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private static final String SECRET = "S/4AN9IsSRUC~{0c4]y#$F2XbV8^`#a14vawn<~Kr@(D%3TF-p1s/h{Y9k7y((rR";
    private static final long defaultExpire = 1000 * 60 * 60 * 24 * 7L;//7天
    //创建一个jwt密钥 加密和解密都需要用这个玩意
    private static final SecretKey key = Jwts.SIG.HS256.key()
            .random(new SecureRandom(SECRET.getBytes(StandardCharsets.UTF_8)))
            .build();

    private JwtUtils() {
    }

    /**
     * 使用默认过期时间（7天），生成一个JWT
     *
     * @param username 用户名
     * @param claims   JWT中的数据
     * @return
     */
    public static String createToken(String username, Map<String, Object> claims) {
        return createToken(username, claims, defaultExpire);
    }

    /**
     * 生成token
     *
     * @param username 用户名
     * @param claims   请求体数据
     * @param expire   过期时间 单位：毫秒
     * @return token
     */
    public static String createToken(String username, Map<String, Object> claims, Long expire) {
        JwtBuilder builder = Jwts.builder();
        Date now = new Date();
        // 生成token
        builder.id(UUID.randomUUID().toString()) //id 这个可以不填，但是建议填
                .issuer("Galaxy") //签发者
                .claims(claims) //数据
                .subject(username) //主题
                .issuedAt(now) //签发时间
                .expiration(new Date(now.getTime() + expire)) //过期时间
                .signWith(key); //签名方式
        builder.header().add("JWT", "JSpWdhuPGblNZApVclmX");
        return builder.compact();
    }

    /**
     * 解析token
     *
     * @param token jwt token
     * @return Claims
     */
    public static Claims claims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                //现在不需要使用 claims.getExpiration().before(new Date());
                // 判断JWT是否过期了 如果过期会抛出ExpiredJwtException异常
                System.out.println("token已过期");
            }
            if (e instanceof JwtException) {
                System.out.println("token已失效");
            }
            logger.error("jwt解析失败" + e);
            System.out.println("token解析失败");
        }
        return null;
    }


    public static void main(String[] args) {
        Map<String, Object> claims = Map.of("name", "张三");
        String token = createToken("mysterious", claims);
        System.out.println("token: "+token);
        Claims claims1 = claims(token);
        System.out.println("claims1: "+claims1);
    }
}
