package com.keijack.database.hibernate.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keijack.database.hibernate.stereotype.ConditionLogicType;
import com.keijack.database.hibernate.stereotype.QueryCondition;

/**
 * 
 * @author Keijack
 * 
 */
public class DefaultQueryConditionAnnoHqlBuilder extends
	QueryConditionAnnoHqlBuilder {

    /**
     * 配置各个逻辑的符号
     */
    private static final Map<ConditionLogicType, String> LOGICMAP = new HashMap<ConditionLogicType, String>();

    static {
	LOGICMAP.put(ConditionLogicType.equal, "=");
	LOGICMAP.put(ConditionLogicType.notEqual, "!=");
	LOGICMAP.put(ConditionLogicType.more, ">");
	LOGICMAP.put(ConditionLogicType.moreEqual, ">=");
	LOGICMAP.put(ConditionLogicType.less, "<");
	LOGICMAP.put(ConditionLogicType.lessEqual, "<=");
	LOGICMAP.put(ConditionLogicType.like, "like");
	LOGICMAP.put(ConditionLogicType.notLike, "not like");
    }

    /**
     * {@inheritDoc}
     */
    public void generateHql(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	StringBuilder filedWithSqlFunction = getHqlFieldWithSqlFunction(conditionAnno);
	where.append(" and ").append(filedWithSqlFunction).append(" ")
		.append(LOGICMAP.get(conditionAnno.logicType())).append(" ?");

	if (param instanceof String) {
	    params.add(conditionAnno.prefix() + param + conditionAnno.suffix());
	} else {
	    params.add(param);
	}
    }
}