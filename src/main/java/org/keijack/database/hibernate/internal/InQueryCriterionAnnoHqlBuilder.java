package org.keijack.database.hibernate.internal;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Keijack
 * 
 */
public class InQueryCriterionAnnoHqlBuilder extends QueryCriterionAnnoHqlBuilder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateHqlFragment(QueryCriterionInfo conditionAnno, Object param, StringBuilder where,
			List<Object> params) {
		where.append("(1 = 0");
		for (Object obj : (Collection<?>) param) {
			StringBuilder filedWithSqlFunction = getHqlFieldWithSqlFunction(conditionAnno);
			where.append(" or ").append(filedWithSqlFunction).append(" = ?");
			params.add(obj);
		}
		where.append(")");

	}

}