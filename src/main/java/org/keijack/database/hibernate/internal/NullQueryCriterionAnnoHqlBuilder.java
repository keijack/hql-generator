package org.keijack.database.hibernate.internal;

import java.util.List;

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
	public void generateHqlFragment(QueryCriterionInfo conditionAnno, Object param,
			StringBuilder where, List<Object> params) {
		String notString;
		if (!(Boolean) param) {
			notString = "not ";
		} else {
			notString = "";
		}
		where.append(super.getAlias()).append(".")
				.append(conditionAnno.getField()).append(" is ").append(notString)
				.append("null");
	}
}