package com.keijack.database.hibernate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keijack.database.hibernate.hqlconditions.ConditionLogicType;
import com.keijack.database.hibernate.hqlconditions.OrderBy;
import com.keijack.database.hibernate.hqlconditions.OrderByLevel;
import com.keijack.database.hibernate.hqlconditions.QueryCondition;
import com.keijack.database.hibernate.hqlconditions.QueryParamsFor;
import com.keijack.database.hibernate.hqlconditions.SqlFunctions;

/**
 * 
 * @author Keijack
 * 
 */
public final class HqlGenerator {

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
		new InQueryConditionAnnoHqlBuilder());
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

    /**
     * 不能被实例化
     */
    private HqlGenerator() {
	super();
    }

    /**
     * 
     * @param listParamsObj
     *            获得一个Hql, 这个类必须被标注为 ListParams
     * @return
     * @throws Exception
     */
    public static HqlAndParams generateHql(Object listParamsObj)
	    throws Exception {

	HqlAndParams hqlResult = new HqlAndParams();

	List<Field> allDeclareFields = getCurrentClassAndSuperClassDeclareFields(listParamsObj);

	setFromToHqlResult(listParamsObj, hqlResult);
	setWhereToHqlResult(listParamsObj, hqlResult, allDeclareFields);
	setOrderByToHqlResult(listParamsObj, hqlResult, allDeclareFields);

	return hqlResult;
    }

    /**
     * 
     * @param listParamsObj
     *            listCall
     * @param hqlResult
     *            收集参数
     * @param allDeclareFields
     *            所有定义的 field
     * @throws Exception
     */
    private static void setOrderByToHqlResult(Object listParamsObj,
	    HqlAndParams hqlResult, List<Field> allDeclareFields)
	    throws Exception {
	Map<OrderByLevel, StringBuilder> orderBySubStrs = getOrderBySubStringMap(
		listParamsObj, allDeclareFields);
	if (orderBySubStrs.isEmpty()) {
	    return;
	}
	String orderBy = getOrderByStringWithSortLevel(orderBySubStrs);

	hqlResult.setOrderBy(orderBy);
    }

    /**
     * @param orderBySubStrs
     *            一个无序的各个等级OrderBy的Map
     * @return 有序的OrderBy字符串
     */
    private static String getOrderByStringWithSortLevel(
	    Map<OrderByLevel, StringBuilder> orderBySubStrs) {
	StringBuilder orderByStr = new StringBuilder("order by ");
	for (OrderByLevel level : OrderByLevel.values()) {
	    if (!orderBySubStrs.containsKey(level)) {
		continue;
	    }
	    orderByStr.append(orderBySubStrs.get(level)).append(", ");
	}
	String orderBy = orderByStr.toString();
	orderBy = orderBy.substring(0, orderBy.lastIndexOf(", "));
	return orderBy;
    }

    /**
     * @param listParamsObj
     *            ListCall
     * @param allDeclareFields
     *            所有定义的 field
     * @return 一个无序的各个等级OrderBy的Map
     * @throws Exception
     */
    private static Map<OrderByLevel, StringBuilder> getOrderBySubStringMap(
	    Object listParamsObj, List<Field> allDeclareFields)
	    throws Exception {
	Map<OrderByLevel, StringBuilder> orderBySubStrs = new HashMap<OrderByLevel, StringBuilder>();
	String alias = getListParamsFor(listParamsObj).alias();
	for (Field field : allDeclareFields) {
	    OrderBy orderByAnno = field.getAnnotation(OrderBy.class);
	    if (orderByAnno == null) {
		continue;
	    }
	    Object param = getFieldValue(listParamsObj, field);
	    if (param == null) {
		continue;
	    }
	    OrderByLevel level = (OrderByLevel) param;
	    if (orderBySubStrs.containsKey(level)) {
		orderBySubStrs.get(level).append(", ").append(alias)
			.append(".").append(orderByAnno.field()).append(" ")
			.append(orderByAnno.orderBy());
	    } else {
		StringBuilder orderBySubStr = getOrderByStringViaAnno(
			orderByAnno, alias);
		orderBySubStrs.put(level, orderBySubStr);
	    }
	}
	return orderBySubStrs;
    }

    /**
     * @param orderByAnno
     *            OrderBy的标注
     * @param alias
     *            别名
     * @return
     */
    private static StringBuilder getOrderByStringViaAnno(OrderBy orderByAnno,
	    String alias) {
	StringBuilder orderBySubStr = new StringBuilder();
	orderBySubStr.append(alias).append(".").append(orderByAnno.field())
		.append(" ").append(orderByAnno.orderBy());
	return orderBySubStr;
    }

    /**
     * @param listParamsObj
     *            ListCall
     * @param hqlResult
     *            收集参数
     * @param allFields
     *            所有的 field
     * @throws Exception
     */
    private static void setWhereToHqlResult(Object listParamsObj,
	    HqlAndParams hqlResult, List<Field> allFields) throws Exception {
	StringBuilder where = new StringBuilder("where 1 = 1");
	List<Object> params = new ArrayList<Object>();

	for (Field field : allFields) {
	    generateWhereFieldHql(listParamsObj, field, where, params);
	}

	hqlResult.setWhere(where.toString());
	hqlResult.setParams(params.toArray());
    }

    /**
     * @param listParamsObj
     *            ListCall
     * @return
     */
    private static List<Field> getCurrentClassAndSuperClassDeclareFields(
	    Object listParamsObj) {
	final List<Field> allFields = new ArrayList<Field>();
	Class<?> currentClass = listParamsObj.getClass();
	while (!currentClass.equals(Object.class)) {
	    final Field[] declaredFields = currentClass.getDeclaredFields();
	    if (declaredFields != null && declaredFields.length > 0) {
		allFields.addAll(Arrays.asList(declaredFields));
	    }
	    currentClass = currentClass.getSuperclass();
	}
	return allFields;
    }

    /**
     * @param listParamsObj
     *            对象
     * @param field
     *            域
     * @param where
     *            搜集参数
     * @param params
     *            搜集参数
     * @return
     * @throws Exception
     */
    private static void generateWhereFieldHql(Object listParamsObj,
	    Field field, StringBuilder where, List<Object> params)
	    throws Exception {
	QueryCondition conditionAnno = field
		.getAnnotation(QueryCondition.class);
	if (conditionAnno == null) {
	    return;
	}
	Object param = getFieldValue(listParamsObj, field);
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
	String alias = getListParamsFor(listParamsObj).alias();
	hqlBuilder.setAlias(alias);
	hqlBuilder.generateHql(conditionAnno, param, where, params);
    }

    /**
     * @param obj
     *            对象
     * @param field
     *            域
     * @return
     * @throws
     */
    private static Object getFieldValue(Object obj, Field field)
	    throws Exception {
	String fildName = field.getName();
	fildName = fildName.substring(0, 1).toUpperCase()
		+ fildName.substring(1);
	Method getMethod = obj.getClass().getMethod("get" + fildName);
	return getMethod.invoke(obj);
    }

    /**
     * @param listParamsObj
     *            ListCall
     * @param hqlResult
     *            收集参数
     */
    private static void setFromToHqlResult(Object listParamsObj,
	    HqlAndParams hqlResult) throws Exception {
	QueryParamsFor listParamsAnno = getListParamsFor(listParamsObj);
	String alias = listParamsAnno.alias();
	Class<?> modelClass = listParamsAnno.value();
	String from = "from " + modelClass.getName() + " " + alias;
	hqlResult.setFrom(from);
    }

    /**
     * @param listParamsObj
     *            类
     * @return
     */
    private static QueryParamsFor getListParamsFor(Object listParamsObj) {
	Class<?> currentClass = listParamsObj.getClass();
	while (!currentClass.equals(Object.class)) {
	    QueryParamsFor anno = currentClass
		    .getAnnotation(QueryParamsFor.class);
	    if (anno != null) {
		return anno;
	    }
	    currentClass = currentClass.getSuperclass();
	}
	return null;
    }
}

/**
 * 
 * @author Keijack
 * 
 */
abstract class QueryConditionAnnoHqlBuilder {

    /**
     * 类别名
     */
    private String alias;

    protected String getAlias() {
	return alias;
    }

    public void setAlias(String alias) {
	this.alias = alias;
    }

    /**
     * 配置各个逻辑的符号
     */
    protected static final Map<ConditionLogicType, String> LOGICMAP = new HashMap<ConditionLogicType, String>();
    /**
     * 配置各个函数的方法
     */
    protected static final Map<SqlFunctions, String> SQLFUNCTIONMAP = new HashMap<SqlFunctions, String>();

    static {
	LOGICMAP.put(ConditionLogicType.equal, "=");
	LOGICMAP.put(ConditionLogicType.notEqual, "!=");
	LOGICMAP.put(ConditionLogicType.more, ">");
	LOGICMAP.put(ConditionLogicType.moreEqual, ">=");
	LOGICMAP.put(ConditionLogicType.less, "<");
	LOGICMAP.put(ConditionLogicType.lessEqual, "<=");
	LOGICMAP.put(ConditionLogicType.like, "like");
	LOGICMAP.put(ConditionLogicType.notLike, "not like");
	LOGICMAP.put(ConditionLogicType.contains, "in elements");
	LOGICMAP.put(ConditionLogicType.notContains, "not in elements");

	SQLFUNCTIONMAP.put(SqlFunctions.year, "year");
	SQLFUNCTIONMAP.put(SqlFunctions.month, "month");
	SQLFUNCTIONMAP.put(SqlFunctions.day, "day");
	SQLFUNCTIONMAP.put(SqlFunctions.originalValue, "");
    }

    /**
     * 
     * @param conditionAnno
     *            条件标注
     * @param param
     *            值
     * @param where
     *            Hql收集参数
     * @param params
     *            参数收集参数
     */
    abstract void generateHql(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params);
}

/**
 * 
 * @author Keijack
 * 
 */
class DefaultQueryConditionAnnoHqlBuilder extends QueryConditionAnnoHqlBuilder {

    /**
     * {@inheritDoc}
     */
    void generateHql(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	where.append(" and ")
		.append(SQLFUNCTIONMAP.get(conditionAnno.sqlFunction()))
		.append("(").append(super.getAlias()).append(".")
		.append(conditionAnno.field()).append(") ")
		.append(LOGICMAP.get(conditionAnno.logicType())).append(" ?");

	if (param instanceof String) {
	    params.add(conditionAnno.prefix() + param + conditionAnno.suffix());
	} else {
	    params.add(param);
	}
    }
}

/**
 * 
 * @author Keijack
 * 
 */
class InQueryConditionAnnoHqlBuilder extends QueryConditionAnnoHqlBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    void generateHql(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	Collection<?> fieldValues = (Collection<?>) param;
	if (ConditionLogicType.in.equals(conditionAnno.logicType())) {
	    where.append(" and (1 = 0");
	    for (Object obj : fieldValues) {
		where.append(" or ").append(super.getAlias()).append(".")
			.append(conditionAnno.field()).append(" = ?");
		params.add(obj);
	    }
	    where.append(")");
	} else if (ConditionLogicType.notIn.equals(conditionAnno.logicType())) {
	    where.append(" and (1 = 1");
	    for (Object obj : fieldValues) {
		where.append(" and ").append(super.getAlias()).append(".")
			.append(conditionAnno.field()).append(" != ?");
		params.add(obj);
	    }
	    where.append(")");
	}
    }
}

/**
 * 
 * @author Keijack
 * 
 */
class ExistQueryConditionAnnoHqlBuilder extends QueryConditionAnnoHqlBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    void generateHql(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	where.append(" and ? ").append(LOGICMAP.get(conditionAnno.logicType()))
		.append(" (").append(super.getAlias()).append(".")
		.append(conditionAnno.field()).append(")");
	params.add(param);
    }

}

/**
 * 
 * @author Keijack
 * 
 */
class NullQueryConditionAnnoHqlBuilder extends QueryConditionAnnoHqlBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    void generateHql(QueryCondition conditionAnno, Object param,
	    StringBuilder where, List<Object> params) {
	Boolean paramValue = (Boolean) param;
	if (paramValue) {
	    where.append(" and ").append(super.getAlias()).append(".")
		    .append(conditionAnno.field()).append(" is null");
	} else {
	    where.append(" and ").append(super.getAlias()).append(".")
		    .append(conditionAnno.field()).append(" is not null");
	}
    }
}
