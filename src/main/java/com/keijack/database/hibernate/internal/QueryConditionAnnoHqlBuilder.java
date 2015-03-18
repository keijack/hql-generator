package com.keijack.database.hibernate.internal;

import java.util.List;

import com.keijack.database.hibernate.stereotype.QueryCondition;

/**
 * 
 * @author Keijack
 * 
 */
public abstract class QueryConditionAnnoHqlBuilder {

    /**
     * 类别名
     */
    private String alias;

    protected String getAlias() {
	return alias;
    }

    public void setAlias(String alias) {
	this.alias = alias;
    }

    /**
     * 获得由 SQLFunction 组成的属性查询条件左边
     * 
     * @param conditionAnno
     * @return
     */
    protected StringBuilder getHqlFieldWithSqlFunction(
	    QueryCondition conditionAnno) {
	StringBuilder filedWithSqlFunction = new StringBuilder();
	filedWithSqlFunction
		.append(conditionAnno.hqlFunction().getName())
		.append("(").append(this.getAlias()).append(".")
		.append(conditionAnno.field()).append(")");
	return filedWithSqlFunction;
    }

    /**
     * 
     * @param conditionAnno
     *            条件标注
     * @param param
     *            值
     * @param where
     *            Hql收集参数
     * @param params
     *            参数收集参数
     */
    public abstract void generateHql(QueryCondition conditionAnno,
	    Object param, StringBuilder where, List<Object> params);
}