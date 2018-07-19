package org.keijack.database.hibernate.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keijack.database.hibernate.stereotype.ComparisonType;
import org.keijack.database.hibernate.stereotype.QueryCondition;

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
    private static final Map<ComparisonType, String> LOGICMAP = new HashMap<ComparisonType, String>();

    static {
	LOGICMAP.put(ComparisonType.EQUAL, "=");
	LOGICMAP.put(ComparisonType.NOTEQUAL, "!=");
	LOGICMAP.put(ComparisonType.MORE, ">");
	LOGICMAP.put(ComparisonType.MOREEQUAL, ">=");
	LOGICMAP.put(ComparisonType.LESS, "<");
	LOGICMAP.put(ComparisonType.LESSEQUAL, "<=");
	LOGICMAP.put(ComparisonType.LIKE, "like");
	LOGICMAP.put(ComparisonType.NOTLIKE, "not like");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateHqlFragment(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	StringBuilder filedWithSqlFunction = getHqlFieldWithSqlFunction(conditionAnno);
	where.append(filedWithSqlFunction).append(" ")
		.append(LOGICMAP.get(conditionAnno.comparison())).append(" ?");

	if (param instanceof String) {
	    params.add(conditionAnno.preString() + param + conditionAnno.postString());
	} else {
	    params.add(param);
	}
    }
}