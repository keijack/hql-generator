package org.keijack.database.hibernate.internal;

import java.util.Map;

/**
 * 
 * @author Keijack
 * 
 */
public class NullQueryCriterionAnnoHqlBuilder extends QueryCriterionAnnoHqlBuilder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateHqlFragment(QueryCriterionInfo annoInfo, Object param,
			StringBuilder where, Map<String, Object> params) {
		String notString;
		if (!(Boolean) param) {
			notString = "not ";
		} else {
			notString = "";
		}
		where.append(super.getAlias()).append(".")
				.append(annoInfo.getField()).append(" is ").append(notString)
				.append("null");
	}
}