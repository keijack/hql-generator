package org.keijack.database.hibernate.stereotype;

/**
 * @author keijack
 *
 */
public enum OrderByLevel {
	/**
	 * 第一级
	 */
	LV1(1),
	/**
	 * @deprecated Use LV1 instead
	 */
	level1(1),
	/**
	 * 第二级
	 */
	LV2(2),
	/**
	 * @deprecated Use LV2 instead
	 */
	level2(2),
	/**
	 * 第三级
	 */
	LV3(3),
	/**
	 * @deprecated Use LV3 instead
	 */
	level3(3),
	/**
	 * 第四级
	 */
	LV4(4),
	/**
	 * @deprecated Use LV4 instead
	 */
	level4(4),
	/**
	 * 第五级
	 */
	LV5(5),
	/**
	 * @deprecated Use LV5 instead
	 */
	level5(5);

	/**
	 * @param index 序号
	 */
	private OrderByLevel(int lv) {
		this.lv = lv;
	}

	/**
	 * 序号
	 */
	private final int lv;

	public int getLv() {
		return lv;
	}

}
