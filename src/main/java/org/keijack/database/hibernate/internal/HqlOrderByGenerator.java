package org.keijack.database.hibernate.internal;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keijack.database.hibernate.HqlGeneratException;
import org.keijack.database.hibernate.internal.util.ReflectionUtil;
import org.keijack.database.hibernate.stereotype.OrderBy;
import org.keijack.database.hibernate.stereotype.OrderByLevel;
import org.keijack.database.hibernate.stereotype.QueryParamsFor;

public class HqlOrderByGenerator {

    /**
     * queryParamsFor annotation, 虽然可以通过queryParamsObj获取，但是由于 协作的类需要用到，传入来可以提高效率
     */
    private final QueryParamsFor queryParamsForAnno;
    /**
     * QueryParams object
     */
    private final Object queryParamsObj;

    private String orderBy;

    public HqlOrderByGenerator(QueryParamsFor queryParamsForAnno,
	    Object queryParamsObj) {
	super();
	this.queryParamsForAnno = queryParamsForAnno;
	this.queryParamsObj = queryParamsObj;
    }

    public String getOrderBy() throws HqlGeneratException {
	if (orderBy == null) {
	    generate();
	}
	return orderBy;
    }

    /**
     * 
     * @param queryParamsObj
     *            listCall
     * @param hqlResult
     *            收集参数
     * @param allDeclareFields
     *            所有定义的 field
     * @throws HqlGeneratException
     */
    private void generate() throws HqlGeneratException {
	Map<OrderByLevel, StringBuilder> orderBySubStrs = getOrderBySubStringMap();

	if (orderBySubStrs.isEmpty()) {
	    this.orderBy = "";
	    return;
	}
	this.orderBy = getOrderByStringWithSortLevel(orderBySubStrs);
    }

    /**
     * @param orderBySubStrs
     *            一个无序的各个等级OrderBy的Map
     * @return 有序的OrderBy字符串
     */
    private String getOrderByStringWithSortLevel(
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
     * @param queryParamsObj
     *            ListCall
     * @param allDeclareFields
     *            所有定义的 field
     * @return 一个无序的各个等级OrderBy的Map
     * @throws HqlGeneratException
     */
    private Map<OrderByLevel, StringBuilder> getOrderBySubStringMap()
	    throws HqlGeneratException {
	List<Field> orderByFields = ReflectionUtil
		.getFieldsWithGivenAnnotationRecursively(
			queryParamsObj.getClass(), OrderBy.class);

	Map<OrderByLevel, StringBuilder> orderBySubStrs = new HashMap<OrderByLevel, StringBuilder>();
	String alias = queryParamsForAnno.alias();
	for (Field field : orderByFields) {
	    OrderBy orderByAnno = field.getAnnotation(OrderBy.class);
	    Object param = ReflectionUtil.getFieldValueViaGetMethod(queryParamsObj, field.getName());
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
    private StringBuilder getOrderByStringViaAnno(OrderBy orderByAnno,
	    String alias) {
	StringBuilder orderBySubStr = new StringBuilder();
	orderBySubStr.append(alias).append(".").append(orderByAnno.field())
		.append(" ").append(orderByAnno.orderBy());
	return orderBySubStr;
    }
}
