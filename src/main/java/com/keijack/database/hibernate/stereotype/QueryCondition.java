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
    ConditionLogicType logicType();

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
    String prefix() default "";

    /**
     * 额外增加的后缀
     * 
     * @return
     */
    String suffix() default "";

    /**
     * 需要使用的SQL函数
     * 
     * @return
     */
    SqlFunctions sqlFunction() default SqlFunctions.originalValue;

    /**
     * 是否过滤空，如果过滤空，则 "" 等同于NULL，空列表等同于 NULL
     * 
     * @return
     */
    boolean emptyAsNull() default false;
}
