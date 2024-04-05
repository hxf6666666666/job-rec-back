package org.example.jobrecback.config;

import org.springframework.context.annotation.Configuration;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;
import java.util.concurrent.TimeUnit;

//import static com.example.module1.constant.RedisConstant.LOGIN_USER_KEY;
//import static com.example.module1.constant.RedisConstant.LOGIN_USER_TTL;

@Configuration
public class testInterceptor {
//    @Resource
//    private StringRedisTemplate stringRedisTemplate;
//    @Bean
//    public HandlerInterceptor loginInterceptor(){
//        return new HandlerInterceptor() {
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                System.out.println("拦截器启动！");
//                //获取请求头的token
//                String token = request.getHeader("Authorization");     //前端发的请求头:Authorization
//                if(token == null){
//                    response.setStatus(401);
//                    return false;
//                }
//                System.out.println("拦截器检测到的token:"+token);
//
//                //基于token获取redis中的用户
//                String key = LOGIN_USER_KEY + token;
//                Map<Object, Object> userMap = stringRedisTemplate.opsForHash()
//                        .entries(key);
//
//                //判断用户是否存在
//                if(userMap.isEmpty()){
//                    //不存在，拦截，返回401
//                    response.setStatus(401);
//                    return false;
//                }
//                //将查询到的hash数据转为DTO对象
//                String username = userMap.get("username").toString();
//                String password = userMap.get("password").toString();
//
//                //存在，保存用户信息到ThreadLocal
//
//                //刷新token有效期
//                stringRedisTemplate.expire(key,LOGIN_USER_TTL, TimeUnit.MICROSECONDS);
//                //放行
//                return true;
//            }
//
//            @Override
//            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//                HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//            }
//        };
//    }
//    @Bean
//    public WebMvcConfigurer loginConfig(){
//        return new WebMvcConfigurer() {
//            @Override
//            public void addInterceptors(InterceptorRegistry registry) {
////                registry.addInterceptor(loginInterceptor()).
////                        addPathPatterns("/login").excludePathPatterns("/login.html","/","/user/login","/static/**");
//            }
//        };
//    }
}
