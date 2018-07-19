package org.keijack.database.hibernate.stereotype;

/**
 * SQL的函数
 * 
 * @author Keijack
 * @version 0.1 只支持 month, day, year 三个函数
 */
public enum HqlFunctions {
    /**
     * 大写函数
     */
    UPPER("upper"),
    /**
     * 小写函数
     */
    LOWER("lower"),
    /**
     * SQL函数, 获得一个日期的年份
     */
    YEAR("year"),
    /**
     * SQL函数, 获得一个日期的月份
     */
    MONTH("month"),
    /**
     * SQL函数, 获得一个日期
     */
    DAY("day"),
    /**
     * 原值
     */
    DEFAULT("");

    private final String name;

    private HqlFunctions(String functionName) {
	this.name = functionName;
    }

    public String getName() {
	return this.name;
    }
}