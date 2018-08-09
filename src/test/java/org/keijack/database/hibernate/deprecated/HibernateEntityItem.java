package org.keijack.database.hibernate.deprecated;

/**
 * 子类
 * 
 * @author Keijack
 * 
 */
// @Entity
// @Table
public class HibernateEntityItem {
    /**
     * 子类id
     */
    private int itemId;
    /**
     * 子类字符串名
     */
    private String itemStrValue;

    public int getItemId() {
	return itemId;
    }

    public void setItemId(int itemId) {
	this.itemId = itemId;
    }

    public String getItemStrValue() {
	return itemStrValue;
    }

    public void setItemStrValue(String itemStrValue) {
	this.itemStrValue = itemStrValue;
    }

}