package org.keijack.database.hibernate.internal;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.keijack.database.hibernate.HqlGeneratException;
import org.keijack.database.hibernate.internal.util.ReflectionUtil;
import org.keijack.database.hibernate.stereotype.QueryCondition;
import org.keijack.database.hibernate.stereotype.QueryCriterion;
import org.keijack.database.hibernate.stereotype.QueryFormula;
import org.keijack.database.hibernate.stereotype.QueryParamsFor;
import org.keijack.database.hibernate.stereotype.RestrictionType;

/**
 * 生成Where语句的类
 * 
 * @author keijack.wu
 * 
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

	private final Logger logger = Logger.getLogger(getClass().getName());

	private String where;

	private Object[] params;

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

	public Object[] getParams() {
		if (params == null) {
			generate();
		}
		return params;
	}

	/**
	 * @param queryParamsObj ListCall
	 * @param hqlResult      收集参数
	 * @param allFields      所有的 field
	 * @throws HqlGeneratException
	 */
	private void generate() {
		StringBuilder where = new StringBuilder("where 1 = 1");
		List<Object> params = new LinkedList<>();

		generateWhereFormQueryCriteria(where, params);

		generateWhereFormQueryFormula(where, params);

		this.where = where.toString();
		this.params = params.toArray();
	}

	private void generateWhereFormQueryFormula(StringBuilder where, List<Object> params) {
		List<Field> queryConditionFields = ReflectionUtil
				.getFieldsWithGivenAnnotationRecursively(queryParamsObj.getClass(), QueryFormula.class);

		for (Field field : queryConditionFields) {
			generateQueryFormulaFieldHql(field, where, params);
		}
	}

	private void generateQueryFormulaFieldHql(Field field, StringBuilder where, List<Object> params) {
		QueryFormula formula = field.getAnnotation(QueryFormula.class);
		boolean emptyAsNull = formula.emptyAsNull();
		Object param = ReflectionUtil.getFieldValueViaGetMethod(queryParamsObj, field.getName());

		if (param == null) {
			return;
		}
		if (emptyAsNull && "".equals(param)) {
			return;
		}
		if (emptyAsNull && Collection.class.isInstance(param) && ((Collection<?>) param).isEmpty()) {
			return;
		}

		String formulaValue = formula.value();
		where.append(" and (").append(formulaValue).append(")");
		if (!formula.appendValue()) {
			return;
		}
		int requiredParamCount = getParamsCount(formulaValue);
		if (Collection.class.isInstance(param)) {
			Collection<?> paramValues = (Collection<?>) param;
			int paramValuesSize = paramValues.size();
			if (paramValuesSize < requiredParamCount) {
				throw new HqlGeneratException(
						"Not enough parameters in Query parameter bean's field ["
								+ field.getName() + "].");
			} else {
				if (paramValuesSize > requiredParamCount) {
					logger.warning("Query parameter bean's field ["
							+ field.getName()
							+ "] paremeters is more than required.Need: ["
							+ requiredParamCount + "] but [" + paramValuesSize
							+ "].");
				}
				int i = 0;
				for (Object paramItem : paramValues) {
					params.add(this.wrapParam(paramItem, formula.preString(), formula.postString()));
					i++;
					if (i == requiredParamCount) {
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < requiredParamCount; i++) {
				params.add(this.wrapParam(param, formula.preString(), formula.postString()));
			}
		}
	}

	private Object wrapParam(Object param, String pre, String post) {
		if (!String.class.isInstance(param)) {
			return param;
		} else {
			return pre + (String) param + post;
		}
	}

	private int getParamsCount(String formulaValue) {
		int count = 0;
		for (char c : formulaValue.toCharArray()) {
			if (c == '?') {
				count++;
			}
		}
		return count;
	}

	private void generateWhereFormQueryCriteria(StringBuilder where, List<Object> params) {
		List<Field> queryCriteria = ReflectionUtil
				.getFieldsWithGivenAnnotationRecursively(queryParamsObj.getClass(), QueryCriterion.class);
		queryCriteria.addAll(ReflectionUtil.getFieldsWithGivenAnnotationRecursively(queryParamsObj.getClass(),
				QueryCondition.class));

		for (Field criterionField : queryCriteria) {
			generateQueryCriterionHql(criterionField, where, params);
		}
	}

	/**
	 * @param queryParamsObj 对象
	 * @param field          域
	 * @param where          搜集参数
	 * @param params         搜集参数
	 * @return
	 * @throws HqlGeneratException
	 */
	private void generateQueryCriterionHql(Field field, StringBuilder where, List<Object> params) {
		QueryCriterionInfo creterionInfo;
		QueryCriterion anno = field.getAnnotation(QueryCriterion.class);
		if (anno == null) {
			creterionInfo = new QueryCriterionInfo(field.getAnnotation(QueryCondition.class));
		} else {
			creterionInfo = new QueryCriterionInfo(anno);
		}
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
