package org.example.jobrecback.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheableToNotJSON {
    String[] cacheNames() default {};
    String key() default "";
    long time() default 30;
}
