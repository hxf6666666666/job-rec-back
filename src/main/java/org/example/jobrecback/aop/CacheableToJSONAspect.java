package org.example.jobrecback.aop;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.jobrecback.annotation.CacheableToJSON;
import org.example.jobrecback.annotation.CacheableToNotJSON;
import org.example.jobrecback.pojo.Recruitment;
import org.example.jobrecback.utils.CacheClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class CacheableToJSONAspect {
    @Resource
    public StringRedisTemplate stringRedisTemplate;
    @Resource
    public CacheClient cacheClient;
    @Pointcut("@annotation(org.example.jobrecback.annotation.CacheableToJSON)")
    public void cacheableToJSON() {}
    @Around("cacheableToJSON()")
    public Object checkCacheAndReturn(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        java.lang.reflect.Method method = signature.getMethod();
        CacheableToJSON annotation = method.getAnnotation(CacheableToJSON.class);
        String[] cacheNames = annotation.cacheNames();
        String cacheKey = annotation.key();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < args.length; i++) context.setVariable(parameters[i].getName(), args[i]);
        ExpressionParser parser = new SpelExpressionParser();
        String key = "";
        if(StringUtils.hasText(cacheKey)) key = parser.parseExpression(cacheKey).getValue(context, String.class);
        Type genericReturnType = method.getGenericReturnType();
        System.out.println("genericReturnType:"+genericReturnType);
        Object result = null;
        try{
            for (String cacheName: cacheNames) {
                String cacheValue = stringRedisTemplate.opsForValue().get(cacheName + ":" + key);
                if (StringUtils.hasText(cacheValue)) {
                    if (genericReturnType.getTypeName().contains("ResponseEntity")) {
                        JSONObject jsonObject = JSONUtil.parseObj(cacheValue);
                        JSONObject bodyObject = jsonObject.getJSONObject("body");
                        Recruitment recruitment = JSONUtil.toBean(bodyObject, Recruitment.class);
                        return new ResponseEntity<>(recruitment, HttpStatus.OK);
                    } else return JSONUtil.toBean(cacheValue, genericReturnType.getClass());
                }
            }
            // 缓存未命中，继续执行方法
            result = joinPoint.proceed();
            //得到返回值后，将返回值存到redis中
            long time = annotation.time();
            String json = JSONUtil.toJsonStr(result);
            for (String cacheName: cacheNames){
                stringRedisTemplate.opsForValue().set(cacheName + ":" + key, json,time, TimeUnit.SECONDS);
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return result;
    }
    @Pointcut("@annotation(org.example.jobrecback.annotation.CacheableToNotJSON)")
    public void cacheableToNotJSON() {}
    @Around("cacheableToNotJSON()")
    public Object checkCacheAndReturnNotJSON(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        java.lang.reflect.Method method = signature.getMethod();
        CacheableToNotJSON annotation = method.getAnnotation(CacheableToNotJSON.class);
        String[] cacheNames = annotation.cacheNames();
        String cacheKey = annotation.key();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < args.length; i++) context.setVariable(parameters[i].getName(), args[i]);
        ExpressionParser parser = new SpelExpressionParser();
        String key = "";
        if(StringUtils.hasText(cacheKey)) key = parser.parseExpression(cacheKey).getValue(context, String.class);
        Object result = null;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            for (String cacheName: cacheNames) {
                String cacheValue = stringRedisTemplate.opsForValue().get(cacheName + ":" + key);
                if (StringUtils.hasText(cacheValue)) {
                    Type genericReturnType = method.getGenericReturnType();
                    JavaType javaType = objectMapper.getTypeFactory().constructType(genericReturnType);
                    result = objectMapper.readValue(cacheValue, javaType);
                    return result;
                }
            }
            // 缓存未命中，继续执行方法
            result = joinPoint.proceed();
            long time = annotation.time();
            Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,Object.class);
            byte[] jsonBytes = serializer.serialize(result);
            assert jsonBytes != null;
            String jsonString = new String(jsonBytes);
            for (String cacheName: cacheNames){
                stringRedisTemplate.opsForValue().set(cacheName + ":" + key, jsonString,time, TimeUnit.SECONDS);
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return result;
    }
}
