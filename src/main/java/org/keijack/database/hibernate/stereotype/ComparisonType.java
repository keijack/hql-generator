/**
 * 
 */
package org.keijack.database.hibernate.stereotype;

/**
 * @author Keijack
 * 
 */
public enum ComparisonType {
    /**
     * =
     */
    EQUAL,
    /**
     * !=
     */
    NOTEQUAL,
    /**
     * >
     */
    MORE,
    /**
     * >=
     */
    MOREEQUAL,
    /**
     * <
     */
    LESS,
    /**
     * <=
     */
    LESSEQUAL,
    /**
     * in
     */
    IN,
    /**
     * not in
     */
    NOTIN,
    /**
     * like
     */
    LIKE,
    /**
     * not like
     */
    NOTLIKE,
    /**
     * in elements
     */
    CONTAINS,
    /**
     * not in elements
     */
    NOTCONTAINS,
    /**
     * null
     */
    ISNULL

}