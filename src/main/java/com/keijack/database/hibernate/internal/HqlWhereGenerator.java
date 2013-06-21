package com.keijack.database.hibernate.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keijack.database.hibernate.HqlGeneratException;
import com.keijack.database.hibernate.stereotype.ConditionLogicType;
import com.keijack.database.hibernate.stereotype.QueryCondition;
import com.keijack.database.hibernate.stereotype.QueryParamsFor;
import com.keijack.database.hibernate.util.ReflectionUtil;

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
    private static final Map<ConditionLogicType, QueryConditionAnnoHqlBuilder> CONDITIONHQLBUILDERS = new HashMap<ConditionLogicType, QueryConditionAnnoHqlBuilder>();

    static {
	CONDITIONHQLBUILDERS.put(ConditionLogicType.equal,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.notEqual,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.more,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.moreEqual,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.less,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.lessEqual,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.in,
		new InQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.notIn,
		new NotInQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.like,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.notLike,
		new DefaultQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.contains,
		new ExistQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.notContains,
		new ExistQueryConditionAnnoHqlBuilder());
	CONDITIONHQLBUILDERS.put(ConditionLogicType.isNull,
		new NullQueryConditionAnnoHqlBuilder());
    }

    private final QueryParamsFor queryParamsForAnno;

    private final Object queryParamsObj;

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

	List<Field> queryConditionFields = ReflectionUtil
		.getFieldsWithGivenAnnotationRecursively(
			queryParamsObj.getClass(), QueryCondition.class);

	for (Field field : queryConditionFields) {
	    generateWhereFieldHql(field, where, params);
	}

	this.where = where.toString();
	this.params = params.toArray();
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
    private void generateWhereFieldHql(Field field, StringBuilder where,
	    List<Object> params) throws HqlGeneratException {
	QueryCondition conditionAnno = field
		.getAnnotation(QueryCondition.class);
	Object param = ReflectionUtil.getFieldValueViaGetMethod(queryParamsObj,
		field);
	if (param == null) {
	    return;
	}
	if (conditionAnno.emptyAsNull()) {
	    if (param instanceof String && "".equals(param)) {
		return;
	    }
	    if (param instanceof Collection<?>
		    && ((Collection<?>) param).isEmpty()) {
		return;
	    }
	}
	QueryConditionAnnoHqlBuilder hqlBuilder = CONDITIONHQLBUILDERS
		.get(conditionAnno.logicType());
	String alias = queryParamsForAnno.alias();
	hqlBuilder.setAlias(alias);
	hqlBuilder.generateHql(conditionAnno, param, where, params);
    }

}
