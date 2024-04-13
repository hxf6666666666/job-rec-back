package org.example.jobrecback.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import jakarta.annotation.Resource;
import org.example.jobrecback.dao.UserRepository;
import org.example.jobrecback.pojo.User;
import org.example.jobrecback.service.AuthService;
import org.example.jobrecback.utils.JwtUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.example.jobrecback.utils.RedisConstant.LOGIN_USER_KEY;
import static org.example.jobrecback.utils.RedisConstant.LOGIN_USER_TTL;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public String login(String username, String password) {
        User user = userRepository.findUserByUserName(username);
//        if (user != null && user.getUserPassword().equals(password) && user.getIsDisabled()!=1)
        if (user != null && BCrypt.checkpw(password,user.getUserPassword()) && user.getIsDisabled()!=1)
        {
            // 返回一个 accessToken, 简单起见，直接使用用户id
//            return ""+user.getId();
            System.out.println("Bcrypt:"+user.getUserPassword()+" 输入密码:"+password);
            System.out.println("密码是否正确:"+BCrypt.checkpw(password,user.getUserPassword()));
            Map<String, Object> userMap = Map.of("userId", user.getId().toString(), "username", username);
            String token = JwtUtils.createToken(username, userMap, LOGIN_USER_TTL);
            System.out.println(token);
            //保存用户到redis
            System.out.println("redis准备保存信息");
            stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY+token,userMap);
            stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL/60000, TimeUnit.MINUTES);
            //返回token给客户端
            return token;
        }
        return null;
    }
}
