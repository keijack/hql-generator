package com.keijack.database.hibernate.hqlconditions;

/**
 * SQL的函数
 * 
 * @author Keijack
 * @version 0.1 只支持 month, day, year 三个函数
 */
public enum SqlFunctions {
    /**
     * SQL函数, 获得一个日期的年份
     */
    year,
    /**
     * SQL函数, 获得一个日期的月份
     */
    month,
    /**
     * SQL函数, 获得一个日期
     */
    day,
    /**
     * 原值
     */
    originalValue
}
