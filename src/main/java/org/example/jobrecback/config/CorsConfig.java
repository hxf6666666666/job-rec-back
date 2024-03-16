package org.example.jobrecback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")//配置映射
                        .allowedOriginPatterns("*")//设置允许那些域来访问
                        .allowedHeaders("*")//请求头字段
                        .allowedMethods("GET","POST","PUT","DELETE","HEAD","OPTIONS")//请求方式(GET,POST,DELETE,PUT)
                        .allowCredentials(true)//是否允许携带cookie
                        .maxAge(3600);//设置3600秒，表示3600秒之内浏览器不必再次询问
            }

        };
    }
}