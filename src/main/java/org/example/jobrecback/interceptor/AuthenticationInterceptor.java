package org.example.jobrecback.interceptor;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationInterceptor {
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
//                String ipAddr = IpUtils.getIpAddr(request);
//                System.out.println("ip地址："+ipAddr);
//                String header = request.getHeader("User-Agent");
//                System.out.println("User-Agent:"+header);
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
//                registry.addInterceptor(loginInterceptor()).
//                        addPathPatterns("/**").excludePathPatterns("/login.html","/","/auth/login","/static/**");
//            }
//        };
//    }
}
