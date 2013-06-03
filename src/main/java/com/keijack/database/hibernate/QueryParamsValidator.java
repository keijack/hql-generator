package com.keijack.database.hibernate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.keijack.database.hibernate.hqlconditions.ConditionLogicType;
import com.keijack.database.hibernate.hqlconditions.OrderBy;
import com.keijack.database.hibernate.hqlconditions.OrderByLevel;
import com.keijack.database.hibernate.hqlconditions.QueryCondition;
import com.keijack.database.hibernate.hqlconditions.QueryParamsFor;

public class QueryParamsValidator {

    public boolean validate(Class<?> queryParamsClass) {
	try {
	    checkClassAnno(queryParamsClass);
	    Class<?> modelClass = queryParamsClass.getAnnotation(
		    QueryParamsFor.class).value();
	    Class<?> currentClass = queryParamsClass;
	    while (!currentClass.equals(Object.class)) {
		checkFieldGetMethodExist(currentClass);
		checkFieldTypeMatch(currentClass);
		checkModelFieldExist(currentClass, modelClass);
		checkOrderByTypeMatch(currentClass);
		currentClass = currentClass.getSuperclass();
	    }
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }

    private void checkFieldTypeMatch(Class<?> queryParamsClass)
	    throws Exception {
	StringBuilder conditionTypeError = new StringBuilder();
	for (Field callField : queryParamsClass.getDeclaredFields()) {
	    QueryCondition conditionAnno = callField
		    .getAnnotation(QueryCondition.class);
	    if (conditionAnno == null) {
		continue;
	    }
	    if (callField.getType().isPrimitive()) {
		conditionTypeError.append("QueryCondition field error: "
			+ callField.getName() + " is primitive.\r\n");
	    }
	    if (ConditionLogicType.isNull.equals(conditionAnno.logicType())
		    && !Boolean.class.equals(callField.getType())) {
		conditionTypeError.append("QueryCondition field error: "
			+ callField.getName()
			+ "isNull filed must be declared as type Boolean\r\n");
	    }
	}
	if (!conditionTypeError.toString().isEmpty()) {
	    throw new Exception(conditionTypeError.toString());
	}
    }

    private void checkOrderByTypeMatch(Class<?> queryParamsClass)
	    throws Exception {
	StringBuilder orderByErrorMsg = new StringBuilder();
	for (Field callField : queryParamsClass.getDeclaredFields()) {
	    OrderBy orderByAnno = callField.getAnnotation(OrderBy.class);
	    if (orderByAnno == null) {
		continue;
	    }
	    if (!callField.getType().equals(OrderByLevel.class)) {
		orderByErrorMsg.append("OrderBy field error:"
			+ callField.getName()
			+ "must be declared as type OrderByLevel\r\n");
	    }
	}
	if (!orderByErrorMsg.toString().isEmpty()) {
	    throw new Exception(orderByErrorMsg.toString());
	}
    }

    private void checkModelFieldExist(Class<?> queryParamsClass,
	    Class<?> modelClass) throws Exception {

	StringBuilder modelFieldErroMsg = new StringBuilder();

	for (Field callField : queryParamsClass.getDeclaredFields()) {
	    QueryCondition conditionAnno = callField
		    .getAnnotation(QueryCondition.class);
	    OrderBy orderByAnno = callField.getAnnotation(OrderBy.class);
	    if (conditionAnno == null && orderByAnno == null) {
		continue;
	    }

	    String fieldName = "";
	    if (conditionAnno != null && orderByAnno == null) {
		fieldName = conditionAnno.field();
	    } else if (orderByAnno != null) {
		fieldName = orderByAnno.field();
	    }

	    modelFieldErroMsg.append(this
		    .checkFieldExist(fieldName, modelClass));
	}
	if (!modelFieldErroMsg.toString().isEmpty()) {
	    throw new Exception(modelFieldErroMsg.toString());
	}
    }

    private StringBuilder checkFieldExist(String fieldName, Class<?> modelClass)
	    throws Exception {
	String[] fieldArray = fieldName.split("\\.");
	String realField = fieldArray[0];

	try {
	    String getMethodName = "get"
		    + realField.substring(0, 1).toUpperCase()
		    + realField.substring(1);
	    Method getMethod = modelClass.getMethod(getMethodName);
	    StringBuilder result = new StringBuilder();
	    if (fieldArray.length > 1) {
		int index = fieldName.indexOf(fieldArray[0])
			+ fieldArray[0].length() + ".".length();
		String beanFiledName = fieldName.substring(index);
		result.append(this.checkFieldExist(beanFiledName,
			getMethod.getReturnType()));
	    }
	    return result;
	} catch (NoSuchMethodException ne) {
	    return new StringBuilder(modelClass.getName())
		    .append("Can not find Field: ").append(realField)
		    .append(" in model class. \r\n");
	}

    }

    private void checkFieldGetMethodExist(Class<?> queryParamsClass)
	    throws Exception {
	StringBuilder fieldErrorMsg = new StringBuilder();
	for (Field callField : queryParamsClass.getDeclaredFields()) {
	    QueryCondition conditionAnno = callField
		    .getAnnotation(QueryCondition.class);
	    OrderBy orderByAnno = callField.getAnnotation(OrderBy.class);
	    if (conditionAnno == null && orderByAnno == null) {
		continue;
	    }
	    String fildName = callField.getName();
	    fieldErrorMsg.append(checkFieldExistGetMethod(fildName,
		    queryParamsClass));
	}
	if (!fieldErrorMsg.toString().isEmpty()) {
	    throw new Exception(fieldErrorMsg.toString());
	}
    }

    private String checkFieldExistGetMethod(String fname, Class<?> modelClass) {
	String fieldName = fname;
	fieldName = fieldName.substring(0, 1).toUpperCase()
		+ fieldName.substring(1);
	try {
	    Method getMethod = modelClass.getMethod("get" + fieldName);
	    if (getMethod == null) {
		return ("Can not find Get method for field " + fieldName + " \r\n");
	    }
	    return "";
	} catch (NoSuchMethodException ne) {
	    return ("Can not find Get method for field " + fieldName + " \r\n");
	}
    }

    private void checkClassAnno(Class<?> queryParamsClass) throws Exception {
	QueryParamsFor classAnno = queryParamsClass
		.getAnnotation(QueryParamsFor.class);
	if (classAnno == null) {
	    throw new Exception(
		    "Can not find Annotation QueryParamsFor in this Class ");
	}
    }

}
