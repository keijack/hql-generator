package org.keijack.database.hibernate;

/**
 * 测试父类
 * 
 * @author Keijack
 * 
 */
public class TestModelParent {
    /**
     * 父类id
     */
    private int parentId;
    /**
     * 父类字符串的值
     */
    private String parentStrValue;

    public int getParentId() {
	return parentId;
    }

    public void setParentId(int parentId) {
	this.parentId = parentId;
    }

    public String getParentStrValue() {
	return parentStrValue;
    }

    public void setParentStrValue(String parentStrValue) {
	this.parentStrValue = parentStrValue;
    }

}