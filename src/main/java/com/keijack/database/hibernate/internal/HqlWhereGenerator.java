package com.keijack.database.hibernate.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.keijack.database.hibernate.HqlGeneratException;
import com.keijack.database.hibernate.internal.util.ReflectionUtil;
import com.keijack.database.hibernate.stereotype.ComparisonType;
import com.keijack.database.hibernate.stereotype.QueryCondition;
import com.keijack.database.hibernate.stereotype.QueryFormula;
import com.keijack.database.hibernate.stereotype.QueryParamsFor;

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
    private static final Map<ComparisonType, QueryConditionAnnoHqlBuilder> CONDITIONHQLBUILDERS = new HashMap<ComparisonType, QueryConditionAnnoHqlBuilder>();

    static {
	CONDITIONHQLBUILDERS.put(ComparisonType.EQUAL,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.NOTEQUAL,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.MORE,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.MOREEQUAL,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.LESS,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.LESSEQUAL,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.IN,
		new InQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.NOTIN,
		new NotInQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.LIKE,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.NOTLIKE,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.CONTAINS,
		new ExistQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.NOTCONTAINS,
		new ExistQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ComparisonType.ISNULL,
		new NullQueryConditionAnnoHqlBuilder());
    }

    private final QueryParamsFor queryParamsForAnno;

    private final Object queryParamsObj;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private String where;

    private Object[] params;

    public HqlWhereGenerator(QueryParamsFor queryParamsFor,
	    Object queryParamsForAnno) {
	super();
	this.queryParamsForAnno = queryParamsFor;
	this.queryParamsObj = queryParamsForAnno;
    }

    public String getWhere() throws HqlGeneratException {
	if (where == null) {
	    generate();
	}
	return where;
    }

    public Object[] getParams() throws HqlGeneratException {
	if (params == null) {
	    generate();
	}
	return params;
    }

    /**
     * @param queryParamsObj
     *            ListCall
     * @param hqlResult
     *            收集参数
     * @param allFields
     *            所有的 field
     * @throws HqlGeneratException
     */
    private void generate() throws HqlGeneratException {
	StringBuilder where = new StringBuilder("where 1 = 1");
	List<Object> params = new ArrayList<Object>();

	generateWhereFormQueryCondition(where, params);

	generateWhereFormQueryFormula(where, params);

	this.where = where.toString();
	this.params = params.toArray();
    }

    private void generateWhereFormQueryFormula(StringBuilder where, List<Object> params) throws HqlGeneratException {
	List<Field> queryConditionFields = ReflectionUtil
		.getFieldsWithGivenAnnotationRecursively(
			queryParamsObj.getClass(), QueryFormula.class);

	for (Field field : queryConditionFields) {
	    generateQueryFormulaFieldHql(field, where, params);
	}
    }

    private void generateQueryFormulaFieldHql(Field field, StringBuilder where, List<Object> params)
	    throws HqlGeneratException {
	QueryFormula formula = field.getAnnotation(QueryFormula.class);
	boolean emptyAsNull = formula.emptyAsNull();
	Object param = ReflectionUtil.getFieldValueViaGetMethod(queryParamsObj, field);

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

    private void generateWhereFormQueryCondition(StringBuilder where,
	    List<Object> params) throws HqlGeneratException {
	List<Field> queryConditionFields = ReflectionUtil
		.getFieldsWithGivenAnnotationRecursively(
			queryParamsObj.getClass(), QueryCondition.class);

	for (Field field : queryConditionFields) {
	    generateQueryConditionFieldHql(field, where, params);
	}
    }

    /**
     * @param queryParamsObj
     *            对象
     * @param field
     *            域
     * @param where
     *            搜集参数
     * @param params
     *            搜集参数
     * @return
     * @throws HqlGeneratException
     */
    private void generateQueryConditionFieldHql(Field field,
	    StringBuilder where, List<Object> params)
		    throws HqlGeneratException {
	QueryCondition conditionAnno = field
		.getAnnotation(QueryCondition.class);
	Object param = ReflectionUtil.getFieldValueViaGetMethod(queryParamsObj,
		field);
	if (param == null) {
	    return;
	}
	if (conditionAnno.emptyAsNull()) {
	    if (String.class.isInstance(param) && "".equals(param)) {
		return;
	    }
	    if (Collection.class.isInstance(param)
		    && Collection.class.cast(param).isEmpty()) {
		return;
	    }
	}
	QueryConditionAnnoHqlBuilder hqlBuilder = CONDITIONHQLBUILDERS
		.get(conditionAnno.comparison());
	String alias = queryParamsForAnno.alias();
	hqlBuilder.setAlias(alias);
	hqlBuilder.generateHql(conditionAnno, param, where, params);
    }

}
