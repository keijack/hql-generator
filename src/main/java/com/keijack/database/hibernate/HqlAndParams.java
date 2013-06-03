package com.keijack.database.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 拼成一个所必须有的参数
 * 
 * @author Keijack
 * 
 */
public class HqlAndParams {
    /**
     * from 语句
     */
    private String from = "";

    /**
     * where语句
     */
    private String where = "";

    /**
     * orderBy 语句
     */
    private String orderBy = "";

    /**
     * 参数
     */
    private Object[] params = {};

    public String getFrom() {
	return from;
    }

    public void setFrom(String from) {
	this.from = from;
    }

    public String getWhere() {
	return where;
    }

    public void setWhere(String where) {
	this.where = where;
    }

    public String getOrderBy() {
	return orderBy;
    }

    public void setOrderBy(String orderBy) {
	this.orderBy = orderBy;
    }

    public Object[] getParams() {
	return params;
    }

    public void setParams(Object[] params) {
	this.params = params;
    }

    /**
     * @return 参数列表
     */
    public List<Object> getParamList() {
	List<Object> paramList = new ArrayList<Object>();
	paramList.addAll(Arrays.asList(this.params));
	return paramList;
    }

    /**
     * 
     * @return Hql语句
     */
    public String getHql() {
	return from + " " + where + " " + orderBy;
    }
}
