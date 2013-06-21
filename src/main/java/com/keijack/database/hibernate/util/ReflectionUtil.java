package com.keijack.database.hibernate.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.keijack.database.hibernate.HqlGeneratException;

/**
 * Util Class
 * 
 * @author keijack.wu
 * 
 */
public final class ReflectionUtil {

    /**
     * Util, cannot be instanced.
     */
    private ReflectionUtil() {
	super();
    }

    /**
     * 获得一个类给定类型的标注
     * 
     * @param Object
     *            需要获取标注的对象
     * @param requiredAnnoType
     *            需要获取的标注
     * @return
     */
    public static <T extends Annotation> T getClassAnnotationRecursively(
	    Object Object, Class<T> requiredAnnoType) {
	Class<?> currentClass = Object.getClass();
	while (!currentClass.equals(Object.class)) {
	    T anno = currentClass.getAnnotation(requiredAnnoType);
	    if (anno != null) {
		return anno;
	    }
	    currentClass = currentClass.getSuperclass();
	}
	return null;
    }

    public static List<Field> getFieldsWithGivenAnnotationRecursively(
	    Class<?> objectClass, Class<? extends Annotation> givenAnnotation) {
	final List<Field> fields = new ArrayList<Field>();
	Class<?> currentClass = objectClass;
	while (!currentClass.equals(Object.class)) {
	    final Field[] declaredFields = currentClass.getDeclaredFields();
	    for (Field field : declaredFields) {
		if (field.getAnnotation(givenAnnotation) != null) {
		    fields.add(field);
		}
	    }
	    currentClass = currentClass.getSuperclass();
	}
	return fields;
    }

    /**
     * @param obj
     *            对象
     * @param field
     *            域
     * @return
     * @throws
     */
    public static Object getFieldValueViaGetMethod(Object obj, Field field)
	    throws HqlGeneratException {
	String fildName = field.getName();
	fildName = fildName.substring(0, 1).toUpperCase()
		+ fildName.substring(1);
	Method getMethod;
	try {
	    getMethod = obj.getClass().getMethod("get" + fildName);
	    return getMethod.invoke(obj);
	} catch (Exception e) {
	    throw new HqlGeneratException(e);
	}
    }
}
