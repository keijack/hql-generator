package org.keijack.database.hibernate.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	public void generateHqlFragment(QueryCriterionInfo annoInfo, Object param, StringBuilder where,
			Map<String, Object> params) {
		String notString = "";
		if (annoInfo.getRestriction().equals(RestrictionType.NOT_CONTAINS)) {
			notString = "not ";
		}
		List<Object> objs = new ArrayList<>();
		if (param instanceof Collection) {
			objs.addAll((Collection) param);
		} else {
			objs.add(param);
		}
		StringBuilder wh = new StringBuilder("(");
		Map<String, Object> ps = new LinkedHashMap<>();
		int i = 0;
		for (Object p : objs) {
			String key = annoInfo.getParamKey() + (i++);
			if (wh.toString().length() > 1) {
				wh.append(" and ");
			}
			wh.append(":").append(key).append(" ").append(notString)
					.append("in elements (")
					.append(super.getAlias()).append(".")
					.append(annoInfo.getField()).append(")");
			ps.put(key, p);
		}
		wh.append(")");

		where.append(wh.toString());
		params.putAll(ps);
	}

}