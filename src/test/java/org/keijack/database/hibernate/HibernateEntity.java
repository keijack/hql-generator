package org.keijack.database.hibernate;

import java.util.Date;
import java.util.Set;

/**
 * 这是一个映射到数据库的 Bean，并且这个 Bean 有继承关系（至于如何实现继承，请参考 Hibernate 相关的文档）
 * 
 * @author Keijack
 * 
 */
// @Entity
// @Table
public class HibernateEntity extends HibernateEntityParent {
    /**
     * int 测试
     */
	// @Id
    private int id;

    /**
     * 字符串测试
     */
    private String strValue;

    /**
     * 日期测试
     */
    private Date date;

    /**
     * 测试引入
     */
    // @ManyToOne
    private HibernateEntityParent parent;

    /**
     * 测试子属性
     */
    // @OneToMany
    private Set<HibernateEntityItem> modelItems;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getStrValue() {
	return strValue;
    }

    public void setStrValue(String strValue) {
	this.strValue = strValue;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public HibernateEntityParent getParent() {
	return parent;
    }

    public void setParent(HibernateEntityParent parent) {
	this.parent = parent;
    }

    public Set<HibernateEntityItem> getModelItems() {
	return modelItems;
    }

    public void setModelItems(Set<HibernateEntityItem> modelItems) {
	this.modelItems = modelItems;
    }

}