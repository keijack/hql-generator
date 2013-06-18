package com.keijack.database.hibernate.hqlconditions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标注一个类是否是 List 查询的
 * 
 * @author Keijack
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface QueryParamsFor {
    /**
     * 用来查询什么类
     * 
     * @return
     */
    Class<?> value();

    /**
     * 别名, 默认是 a
     * 
     * @return
     */
    String alias() default "a";
}