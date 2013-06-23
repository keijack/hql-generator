package com.keijack.database.hibernate.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 判断一个属性是用来排序的
 * 
 * @author Keijack
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OrderBy {
    /**
     * 字段
     * 
     * @return
     */
    String field();

    /**
     * 排序
     * 
     * @return
     */
    SortOrder orderBy() default SortOrder.ASC;
}
