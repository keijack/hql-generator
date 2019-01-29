package org.keijack.database.hibernate.internal;

import java.util.EnumMap;
import java.util.Map;

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
	public void generateHqlFragment(QueryCriterionInfo annoInfo, Object param,
			StringBuilder where, Map<String, Object> params) {
		StringBuilder filedWithSqlFunction = getHqlFieldWithSqlFunction(annoInfo);
		where.append(filedWithSqlFunction).append(" ")
				.append(LOGICMAP.get(annoInfo.getRestriction())).append(" :")
				.append(annoInfo.getParamKey());

		if (param instanceof String) {
			params.put(annoInfo.getParamKey(), annoInfo.getPreString() + param + annoInfo.getPostString());
		} else {
			params.put(annoInfo.getParamKey(), param);
		}
	}
}