package com.keijack.database.hibernate.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Keijack
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryCondition {
    /**
     * 这个查询是哪个查询
     * 
     * @return
     */
    ComparisonType comparison();

    /**
     * 用于哪个Filed
     * 
     * @return
     */
    String field();

    /**
     * 额外增加前缀
     * 
     * @return
     */
    String preString() default "";

    /**
     * 额外增加的后缀
     * 
     * @return
     */
    String postString() default "";

    /**
     * 需要使用的SQL函数
     * 
     * @return
     */
    HqlFunctions hqlFunction() default HqlFunctions.DEFAULT;

    /**
     * 是否过滤空，如果过滤空，则 "" 等同于NULL，空列表等同于 NULL
     * 
     * @return
     */
    boolean emptyAsNull() default false;
}
