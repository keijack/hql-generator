package org.keijack.database.hibernate.stereotype;

/**
 * 
 * @author Keijack
 * 
 */
public enum SortOrder {
	/**
	 * 升序排列
	 */
	ASC,
	/**
	 * 降序排列
	 */
	DESC;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
