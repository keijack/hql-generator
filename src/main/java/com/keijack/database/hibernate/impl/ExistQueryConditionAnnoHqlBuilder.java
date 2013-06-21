package com.keijack.database.hibernate.impl;

import java.util.List;

import com.keijack.database.hibernate.hqlconditions.ConditionLogicType;
import com.keijack.database.hibernate.hqlconditions.QueryCondition;

/**
 * 
 * @author Keijack
 * 
 */
public class ExistQueryConditionAnnoHqlBuilder extends
	QueryConditionAnnoHqlBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateHql(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	String notString = "";
	if (conditionAnno.logicType().equals(ConditionLogicType.notContains)) {
	    notString = "not ";
	}
	where.append(" and ? ").append(notString).append("in elements (")
		.append(super.getAlias()).append(".")
		.append(conditionAnno.field()).append(")");
	params.add(param);
    }

}