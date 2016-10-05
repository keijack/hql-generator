package com.keijack.database.hibernate.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
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
    public void generateHqlFragment(QueryCondition conditionAnno, Object param,
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
	StringBuilder wh = new StringBuilder("(");
	List<Object> ps = new LinkedList<>();
	for (Object p : objs) {
	    if (wh.toString().length() > 1) {
		wh.append(" and ");
	    }
	    wh.append("? ").append(notString).append("in elements (")
		    .append(super.getAlias()).append(".")
		    .append(conditionAnno.field()).append(")");
	    ps.add(p);
	}
	wh.append(")");

	where.append(wh.toString());
	params.addAll(ps);
    }

}