/**
 * 
 */
package org.keijack.database.hibernate.stereotype;

/**
 * @author Keijack
 * 
 */
public enum RestrictionType {
	/**
	 * =
	 */
	EQUAL,
	/**
	 * !=
	 */
	NOT_EQUAL,
	/**
	 * >
	 */
	MORE,
	/**
	 * >=
	 */
	MORE_EQUAL,
	/**
	 * <
	 */
	LESS,
	/**
	 * <=
	 */
	LESS_EQUAL,
	/**
	 * in
	 */
	IN,
	/**
	 * not in
	 */
	NOT_IN,
	/**
	 * like
	 */
	LIKE,
	/**
	 * not like
	 */
	NOT_LIKE,
	/**
	 * in elements
	 */
	CONTAINS,
	/**
	 * not in elements
	 */
	NOT_CONTAINS,
	/**
	 * null
	 */
	IS_NULL

}