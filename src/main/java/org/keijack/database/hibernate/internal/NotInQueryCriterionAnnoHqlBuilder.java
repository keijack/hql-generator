package org.keijack.database.hibernate.internal;

import java.util.Collection;
import java.util.List;

public class NotInQueryCriterionAnnoHqlBuilder extends
		QueryCriterionAnnoHqlBuilder {

	@Override
	public void generateHqlFragment(QueryCriterionInfo conditionAnno, Object param,
			StringBuilder where, List<Object> params) {
		where.append("(1 = 1");
		for (Object obj : (Collection<?>) param) {
			StringBuilder filedWithSqlFunction = getHqlFieldWithSqlFunction(conditionAnno);
			where.append(" and ").append(filedWithSqlFunction).append(" != ?");
			params.add(obj);
		}
		where.append(")");
	}

}
