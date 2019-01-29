package org.keijack.database.hibernate.internal;

import java.util.Collection;
import java.util.Map;

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
	public void generateHqlFragment(QueryCriterionInfo annoInfo, Object param, StringBuilder where,
			Map<String, Object> params) {
		where.append("(1 = 0");
		int i = 0;
		for (Object obj : (Collection<?>) param) {
			String key = annoInfo.getParamKey() + (i++);
			StringBuilder filedWithSqlFunction = getHqlFieldWithSqlFunction(annoInfo);
			where.append(" or ").append(filedWithSqlFunction).append(" = :").append(key);
			params.put(key, obj);
		}
		where.append(")");

	}

}