package org.keijack.database.hibernate.internal;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.keijack.database.hibernate.HqlGeneratException;
import org.keijack.database.hibernate.internal.util.ReflectionUtil;
import org.keijack.database.hibernate.stereotype.QueryCriterion;
import org.keijack.database.hibernate.stereotype.QueryParamsFor;
import org.keijack.database.hibernate.stereotype.RestrictionType;

/**
 * 生成Where语句的类
 *
 * @author keijack.wu
 */
public class HqlWhereGenerator {

	/**
	 * 使用什么HqlGenerator来生成
	 */
	private static final Map<RestrictionType, Class<? extends QueryCriterionAnnoHqlBuilder>> CONDITIONHQLBUILDERS = new EnumMap<>(
			RestrictionType.class);

	static {
		CONDITIONHQLBUILDERS.put(RestrictionType.IN, InQueryCriterionAnnoHqlBuilder.class);
		CONDITIONHQLBUILDERS.put(RestrictionType.NOT_IN, NotInQueryCriterionAnnoHqlBuilder.class);
		CONDITIONHQLBUILDERS.put(RestrictionType.CONTAINS, ExistQueryCriterionAnnoHqlBuilder.class);
		CONDITIONHQLBUILDERS.put(RestrictionType.NOT_CONTAINS, ExistQueryCriterionAnnoHqlBuilder.class);
		CONDITIONHQLBUILDERS.put(RestrictionType.IS_NULL, NullQueryCriterionAnnoHqlBuilder.class);
	}

	private final QueryParamsFor queryParamsForAnno;

	private final Object queryParamsObj;

	private String where;

	private Map<String, Object> params;

	public HqlWhereGenerator(QueryParamsFor queryParamsFor, Object queryParamsForAnno) {
		super();
		this.queryParamsForAnno = queryParamsFor;
		this.queryParamsObj = queryParamsForAnno;
	}

	public String getWhere() {
		if (where == null) {
			generate();
		}
		return where;
	}

	public Map<String, Object> getParams() {
		if (params == null) {
			generate();
		}
		return params;
	}

	/**
	 * @throws HqlGeneratException
	 */
	private void generate() {
		StringBuilder whereHql = new StringBuilder("where 1 = 1");
		Map<String, Object> paramsMap = new LinkedHashMap<>();

		generateWhereFormQueryCriteria(whereHql, paramsMap);

		this.where = whereHql.toString();
		this.params = paramsMap;
	}

	private void generateWhereFormQueryCriteria(StringBuilder where, Map<String, Object> params) {
		List<Field> queryCriteria = ReflectionUtil
				.getFieldsWithGivenAnnotationRecursively(queryParamsObj.getClass(), QueryCriterion.class);

		for (Field criterionField : queryCriteria) {
			generateQueryCriterionHql(criterionField, where, params);
		}
	}

	/**
	 * @param field  域
	 * @param where  搜集参数
	 * @param params 搜集参数
	 * @return
	 * @throws HqlGeneratException
	 */
	private void generateQueryCriterionHql(Field field, StringBuilder where, Map<String, Object> params) {
		QueryCriterionInfo creterionInfo;

		creterionInfo = new QueryCriterionInfo(field);

		Object param = ReflectionUtil.getFieldValueViaGetMethod(queryParamsObj, field.getName());
		if (param == null) {
			return;
		}
		if (creterionInfo.isEmptyAsNull()) {
			if (String.class.isInstance(param) && "".equals(param)) {
				return;
			}
			if (Collection.class.isInstance(param)
					&& Collection.class.cast(param).isEmpty()) {
				return;
			}
		}
		QueryCriterionAnnoHqlBuilder hqlBuilder = getAnnoHqlBuilder(creterionInfo.getRestriction());
		String alias = queryParamsForAnno.alias();
		hqlBuilder.setAlias(alias);

		hqlBuilder.generateHql(creterionInfo, param, where, params);
	}

	private QueryCriterionAnnoHqlBuilder getAnnoHqlBuilder(RestrictionType restrictionType) {
		Class<? extends QueryCriterionAnnoHqlBuilder> clz = CONDITIONHQLBUILDERS.get(restrictionType);
		if (clz == null)
			clz = DefaultQueryCriterionAnnoHqlBuilder.class;
		try {
			return clz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new HqlGeneratException(e);
		}
	}

}
