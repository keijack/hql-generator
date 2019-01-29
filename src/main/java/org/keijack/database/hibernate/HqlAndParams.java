package org.keijack.database.hibernate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 拼成一个所必须有的参数
 *
 * @author Keijack
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
	private Map<String, Object> params = new LinkedHashMap<>();

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

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	/**
	 * @return Hql语句
	 */
	public String getHql() {
		return from + " " + where + " " + orderBy;
	}
}
