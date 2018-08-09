package org.keijack.database.hibernate.internal;

import java.util.EnumMap;
import java.util.List;

import org.keijack.database.hibernate.stereotype.RestrictionType;

/**
 * 
 * @author Keijack
 * 
 */
public class DefaultQueryCriterionAnnoHqlBuilder extends QueryCriterionAnnoHqlBuilder {

	/**
	 * 配置各个逻辑的符号
	 */
	private static final EnumMap<RestrictionType, String> LOGICMAP = new EnumMap<>(RestrictionType.class);

	static {
		LOGICMAP.put(RestrictionType.EQUAL, "=");
		LOGICMAP.put(RestrictionType.NOT_EQUAL, "!=");
		LOGICMAP.put(RestrictionType.MORE, ">");
		LOGICMAP.put(RestrictionType.MORE_EQUAL, ">=");
		LOGICMAP.put(RestrictionType.LESS, "<");
		LOGICMAP.put(RestrictionType.LESS_EQUAL, "<=");
		LOGICMAP.put(RestrictionType.LIKE, "like");
		LOGICMAP.put(RestrictionType.NOT_LIKE, "not like");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateHqlFragment(QueryCriterionInfo conditionAnno, Object param,
			StringBuilder where, List<Object> params) {
		StringBuilder filedWithSqlFunction = getHqlFieldWithSqlFunction(conditionAnno);
		where.append(filedWithSqlFunction).append(" ")
				.append(LOGICMAP.get(conditionAnno.getRestriction())).append(" ?");

		if (param instanceof String) {
			params.add(conditionAnno.getPreString() + param + conditionAnno.getPostString());
		} else {
			params.add(param);
		}
	}
}