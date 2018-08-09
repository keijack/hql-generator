package org.keijack.database.hibernate.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.keijack.database.hibernate.stereotype.RestrictionType;

/**
 * 
 * @author Keijack
 * 
 */
public class ExistQueryCriterionAnnoHqlBuilder extends QueryCriterionAnnoHqlBuilder {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void generateHqlFragment(QueryCriterionInfo conditionAnno, Object param, StringBuilder where,
			List<Object> params) {
		String notString = "";
		if (conditionAnno.getRestriction().equals(RestrictionType.NOT_CONTAINS)) {
			notString = "not ";
		}
		List<Object> objs = new ArrayList<>();
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
					.append(conditionAnno.getField()).append(")");
			ps.add(p);
		}
		wh.append(")");

		where.append(wh.toString());
		params.addAll(ps);
	}

}