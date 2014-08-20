package com.keijack.database.hibernate.internal;

import java.util.ArrayList;
import java.util.Collection;
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void generateHql(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	String notString = "";
	if (conditionAnno.comparison().equals(ComparisonType.NOTCONTAINS)) {
	    notString = "not ";
	}
	List<Object> objs = new ArrayList<Object>();
	if (param instanceof Collection) {
	    objs.addAll((Collection) param);
	} else {
	    objs.add(param);
	}

	for (Object p : objs) {
	    where.append(" and ? ").append(notString).append("in elements (")
		    .append(super.getAlias()).append(".")
		    .append(conditionAnno.field()).append(")");
	    params.add(p);
	}
    }

}