/**
 * 
 */
package com.keijack.database.hibernate.hqlconditions;

/**
 * @author Keijack
 * 
 */
public enum ConditionLogicType {
    /**
     * =
     */
    equal,
    /**
     * !=
     */
    notEqual,
    /**
     * >
     */
    more,
    /**
     * >=
     */
    moreEqual,
    /**
     * <
     */
    less,
    /**
     * <=
     */
    lessEqual,
    /**
     * in
     */
    in,
    /**
     * not in
     */
    notIn,
    /**
     * like
     */
    like,
    /**
     * not like
     */
    notLike,
    /**
     * in elements
     */
    contains,
    /**
     * not in elements
     */
    notContains,
    /**
     * 空
     */
    isNull

}