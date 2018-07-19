package org.keijack.database.hibernate.internal;

import java.util.Collection;
import java.util.List;

import org.keijack.database.hibernate.stereotype.QueryCondition;

/**
 * 
 * @author Keijack
 * 
 */
public class InQueryConditionAnnoHqlBuilder extends
	QueryConditionAnnoHqlBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateHqlFragment(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	where.append("(1 = 0");
	for (Object obj : (Collection<?>) param) {
	    StringBuilder filedWithSqlFunction = getHqlFieldWithSqlFunction(conditionAnno);
	    where.append(" or ").append(filedWithSqlFunction).append(" = ?");
	    params.add(obj);
	}
	where.append(")");

    }

}