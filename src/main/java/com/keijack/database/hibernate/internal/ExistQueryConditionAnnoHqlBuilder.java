package com.keijack.database.hibernate.internal;

import java.util.List;

import com.keijack.database.hibernate.stereotype.ComparisonType;
import com.keijack.database.hibernate.stereotype.QueryCondition;

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
	if (conditionAnno.comparison().equals(ComparisonType.NOTCONTAINS)) {
	    notString = "not ";
	}
	where.append(" and ? ").append(notString).append("in elements (")
		.append(super.getAlias()).append(".")
		.append(conditionAnno.field()).append(")");
	params.add(param);
    }

}