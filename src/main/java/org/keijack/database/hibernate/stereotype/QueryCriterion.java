package org.keijack.database.hibernate.stereotype;

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
public @interface QueryCriterion {

	/**
	 * 这个查询是哪个查询
	 * 
	 * @return 这个查询是哪个查询
	 */
	RestrictionType restriction();

	/**
	 * 用于哪个Filed
	 * 
	 * @return 用于哪个Filed
	 */
	String field();

	/**
	 * 内嵌查询，如果一个 field 传入多个参数，将会生产成(field=list(0) and/or field=list(1)) 的条件
	 * 
	 * @return 内嵌查询类
	 */
	EmbedType embedType() default EmbedType.NONE;

	/**
	 * 额外增加前缀
	 * 
	 * @return 前缀
	 */
	String preString() default "";

	/**
	 * 额外增加的后缀
	 * 
	 * @return 后缀
	 */
	String postString() default "";

	/**
	 * 需要使用的SQL函数
	 * 
	 * @return SQL函数
	 */
	HqlFunctions hqlFunction() default HqlFunctions.DEFAULT;

	/**
	 * 是否过滤空，如果过滤空，则 "" 等同于NULL，空列表等同于 NULL
	 * 
	 * @return 是否过滤空
	 */
	boolean emptyAsNull() default false;
}
