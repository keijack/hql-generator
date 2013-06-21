package com.keijack.database.hibernate.impl;

import java.util.Collection;
import java.util.List;

import com.keijack.database.hibernate.hqlconditions.QueryCondition;

public class NotInQueryConditionAnnoHqlBuilder extends
	QueryConditionAnnoHqlBuilder {

    @Override
    public void generateHql(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	where.append(" and (1 = 1");
	for (Object obj : (Collection<?>) param) {
	    StringBuilder filedWithSqlFunction = getHqlFieldWithSqlFunction(conditionAnno);
	    where.append(" and ").append(filedWithSqlFunction).append(" != ?");
	    params.add(obj);
	}
	where.append(")");
    }

}
