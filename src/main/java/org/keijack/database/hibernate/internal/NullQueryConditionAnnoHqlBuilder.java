package org.keijack.database.hibernate.internal;

import java.util.List;

import org.keijack.database.hibernate.stereotype.QueryCondition;

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
    public void generateHqlFragment(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	String notString;
	if (!(Boolean) param) {
	    notString = "not ";
	} else {
	    notString = "";
	}
	where.append(super.getAlias()).append(".")
		.append(conditionAnno.field()).append(" is ").append(notString)
		.append("null");
    }
}