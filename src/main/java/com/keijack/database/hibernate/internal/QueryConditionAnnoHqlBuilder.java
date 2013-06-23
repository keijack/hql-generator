package com.keijack.database.hibernate.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keijack.database.hibernate.stereotype.QueryCondition;
import com.keijack.database.hibernate.stereotype.HqlFunctions;

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
     * 配置各个函数的方法
     */
    protected static final Map<HqlFunctions, String> SQLFUNCTIONMAP = new HashMap<HqlFunctions, String>();

    static {

	SQLFUNCTIONMAP.put(HqlFunctions.YEAR, "year");
	SQLFUNCTIONMAP.put(HqlFunctions.MONTH, "month");
	SQLFUNCTIONMAP.put(HqlFunctions.DAY, "day");
	SQLFUNCTIONMAP.put(HqlFunctions.DEFAULT, "");
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
		.append(SQLFUNCTIONMAP.get(conditionAnno.hqlFunction()))
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