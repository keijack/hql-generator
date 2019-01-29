package org.keijack.database.hibernate.stereotype;

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
	 * 要select的属性
	 * 
	 * @return 要select的属性
	 */
	String[] fields() default {};

	/**
	 * @return 是否对某些属性进行 distinct 查询
	 */
	boolean distinct() default false;

	/**
	 * 用来查询什么类
	 * 
	 * @return 用来查询什么类
	 */
	Class<?> value();

	/**
	 * 别名, 默认是 a
	 * 
	 * @return 别名
	 */
	String alias() default "a";
}
