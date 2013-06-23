package com.keijack.database.hibernate.stereotype;

/**
 * SQL的函数
 * 
 * @author Keijack
 * @version 0.1 只支持 month, day, year 三个函数
 */
public enum HqlFunctions {
    /**
     * SQL函数, 获得一个日期的年份
     */
    YEAR,
    /**
     * SQL函数, 获得一个日期的月份
     */
    MONTH,
    /**
     * SQL函数, 获得一个日期
     */
    DAY,
    /**
     * 原值
     */
    DEFAULT
}
