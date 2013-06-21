package com.keijack.database.hibernate.impl;

import java.util.List;

import com.keijack.database.hibernate.hqlconditions.QueryCondition;

/**
 * 
 * @author Keijack
 * 
 */
public class NullQueryConditionAnnoHqlBuilder extends
	QueryConditionAnnoHqlBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateHql(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	String notString = "";
	if (!(Boolean) param) {
	    notString = "not ";
	}
	where.append(" and ").append(super.getAlias()).append(".")
		.append(conditionAnno.field()).append(" is ").append(notString)
		.append("null");
    }
}